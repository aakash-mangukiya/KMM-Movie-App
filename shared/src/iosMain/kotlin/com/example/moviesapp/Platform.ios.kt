package com.example.moviesapp

import com.example.moviesapp.domain.model.ImageData
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.requestAccessForMediaType
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
import platform.UIKit.UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
import platform.UIKit.UINavigationController
import platform.UIKit.UITabBarController
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual class ImagePicker {

    actual suspend fun pickImageFromGallery(): ImageData? =
        suspendCancellableCoroutine { cont ->
            presentPicker(
                sourceType = UIImagePickerControllerSourceTypePhotoLibrary,
                onImagePicked = { image -> cont.resume(image?.toImageData()) },
                onPresentationFailed = { cont.resume(null) }
            )
        }

    actual suspend fun captureImageFromCamera(): ImageData? =
        suspendCancellableCoroutine { cont ->
            if (!UIImagePickerController.isSourceTypeAvailable(UIImagePickerControllerSourceTypeCamera)) {
                cont.resume(null)
                return@suspendCancellableCoroutine
            }

            presentPicker(
                sourceType = UIImagePickerControllerSourceTypeCamera,
                onImagePicked = { image -> cont.resume(image?.toImageData()) },
                onPresentationFailed = { cont.resume(null) }
            )
        }
}

private fun presentPicker(
    sourceType: platform.UIKit.UIImagePickerControllerSourceType,
    onImagePicked: (platform.UIKit.UIImage?) -> Unit,
    onPresentationFailed: () -> Unit
) {
    val presenter = UIApplication.sharedApplication.activeViewController()
    if (presenter == null) {
        onPresentationFailed()
        return
    }

    val picker = UIImagePickerController()
    picker.sourceType = sourceType

    lateinit var delegate: ImagePickerDelegate
    delegate = ImagePickerDelegate { image ->
        releaseImagePickerDelegate(delegate)
        onImagePicked(image)
    }

    picker.delegate = delegate
    retainImagePickerDelegate(delegate)
    presenter.presentViewController(picker, true, null)
}

private fun UIApplication.activeViewController(): UIViewController? {
    val window = connectedScenes
        .filterIsInstance<platform.UIKit.UIWindowScene>()
        .firstOrNull { it.activationState == platform.UIKit.UISceneActivationStateForegroundActive }
        ?.windows
        ?.filterIsInstance<UIWindow>()
        ?.firstOrNull { it.isKeyWindow() }
        ?: connectedScenes
            .filterIsInstance<platform.UIKit.UIWindowScene>()
            .flatMap { it.windows }
            .filterIsInstance<UIWindow>()
            .firstOrNull { it.isKeyWindow() }

    return window?.rootViewController?.topMostViewController()
}

private fun UIViewController.topMostViewController(): UIViewController {
    presentedViewController?.let { return it.topMostViewController() }

    if (this is UINavigationController) {
        visibleViewController?.let { return it.topMostViewController() }
    }

    if (this is UITabBarController) {
        selectedViewController?.let { return it.topMostViewController() }
    }

    return this
}

actual class PermissionManager {

    actual suspend fun requestCameraPermission(): Boolean = suspendCancellableCoroutine { cont ->
        AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
            cont.resume(granted)
        }
    }

    actual suspend fun requestGalleryPermission(): Boolean = suspendCancellableCoroutine { cont ->
        PHPhotoLibrary.requestAuthorizationForAccessLevel(platform.Photos.PHAccessLevelReadWrite) { status ->
            cont.resume(status == PHAuthorizationStatusAuthorized)
        }
    }
}
