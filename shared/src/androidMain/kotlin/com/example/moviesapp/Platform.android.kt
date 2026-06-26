package com.example.moviesapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.example.moviesapp.domain.model.ImageData
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.util.UUID
import kotlin.coroutines.resume

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual class ImagePicker(
    private val activity: ComponentActivity
) {
    actual suspend fun pickImageFromGallery(): ImageData? =
        suspendCancellableCoroutine { cont ->
            val key = UUID.randomUUID().toString()
            lateinit var launcher: ActivityResultLauncher<PickVisualMediaRequest>
            launcher = activity.activityResultRegistry.register(
                key,
                ActivityResultContracts.PickVisualMedia()
            ) { uri ->
                launcher.unregister()
                if (cont.isActive) {
                    cont.resume(uri?.let { activity.toImageData(it) })
                }
            }
            cont.invokeOnCancellation { launcher.unregister() }
            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

    actual suspend fun captureImageFromCamera(): ImageData? =
        suspendCancellableCoroutine { cont ->
            val key = UUID.randomUUID().toString()
            val tempImageUri = createTempImageUri(activity)
            lateinit var launcher: ActivityResultLauncher<Uri>
            launcher = activity.activityResultRegistry.register(
                key,
                ActivityResultContracts.TakePicture()
            ) { success ->
                launcher.unregister()
                if (cont.isActive) {
                    if (success) {
                        cont.resume(activity.toImageData(tempImageUri))
                    } else {
                        cont.resume(null)
                    }
                }
            }
            cont.invokeOnCancellation { launcher.unregister() }
            launcher.launch(tempImageUri)
        }
}

actual class PermissionManager(
    private val activity: ComponentActivity
) {
    actual suspend fun requestCameraPermission(): Boolean =
        suspendCancellableCoroutine { cont ->
            val key = UUID.randomUUID().toString()
            lateinit var launcher: ActivityResultLauncher<String>
            launcher = activity.activityResultRegistry.register(
                key,
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                launcher.unregister()
                if (cont.isActive) {
                    cont.resume(isGranted)
                }
            }
            cont.invokeOnCancellation { launcher.unregister() }
            launcher.launch(Manifest.permission.CAMERA)
        }

    actual suspend fun requestGalleryPermission(): Boolean = true
}

class AndroidImageCropper(
    private val activity: ComponentActivity
) : ImageCropper {

    override suspend fun crop(image: ImageData): ImageData? =
        suspendCancellableCoroutine { cont ->
            val key = UUID.randomUUID().toString()
            val source = image.uri.toUri()
            val dest = Uri.fromFile(File(activity.filesDir, "crop_${System.currentTimeMillis()}.jpg"))
            val intent = UCrop.of(source, dest).getIntent(activity)

            lateinit var launcher: ActivityResultLauncher<Intent>
            launcher = activity.activityResultRegistry.register(
                key,
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                launcher.unregister()
                if (cont.isActive) {
                    if (result.resultCode == Activity.RESULT_OK) {
                        val uri = UCrop.getOutput(result.data!!)
                        cont.resume(uri?.let { activity.toImageData(it) })
                    } else {
                        cont.resume(null)
                    }
                }
            }
            cont.invokeOnCancellation { launcher.unregister() }
            launcher.launch(intent)
        }
}
