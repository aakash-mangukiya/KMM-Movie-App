package com.example.moviesapp

import com.example.moviesapp.domain.model.ImageData
import platform.Foundation.NSDate
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.timeIntervalSince1970
import platform.Foundation.writeToFile
import platform.UIKit.UIImage
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject

class ImagePickerDelegate(
    val onImagePicked: (UIImage?) -> Unit
) : NSObject(), UIImagePickerControllerDelegateProtocol,
    UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        onImagePicked(image)
        picker.dismissViewControllerAnimated(true, null)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        onImagePicked(null)
        picker.dismissViewControllerAnimated(true, null)
    }
}

private val activeImagePickerDelegates = mutableSetOf<ImagePickerDelegate>()

fun retainImagePickerDelegate(delegate: ImagePickerDelegate) {
    activeImagePickerDelegates += delegate
}

fun releaseImagePickerDelegate(delegate: ImagePickerDelegate) {
    activeImagePickerDelegates -= delegate
}

fun UIImage.toImageData(): ImageData {
    val data = platform.UIKit.UIImageJPEGRepresentation(this, 1.0)
    val fileName = "image_${NSDate().timeIntervalSince1970}.jpg"

    val path = NSTemporaryDirectory() + fileName
    val nsData = data!!
    nsData.writeToFile(path, true)

    return ImageData(
        uri = path,
        fileName = fileName,
        mimeType = "image/jpeg",
        size = nsData.length.toLong()
    )
}
