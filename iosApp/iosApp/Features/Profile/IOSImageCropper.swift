//
//  IOSImageCropper.swift
//  iosApp
//
//  Created by Aakash Mangukiya on 18/04/26.
//

import Foundation
import UIKit
import Shared

class IOSImageCropper: NSObject, ImageCropper {
    func __crop(image: ImageData) async throws -> ImageData? {
        guard let uiImage = UIImage(contentsOfFile: image.uri) else {
            return nil
        }

        return persistImage(uiImage, original: image)
    }

    private func persistImage(_ image: UIImage, original: ImageData) -> ImageData? {
        guard let data = image.jpegData(compressionQuality: 0.9) else {
            return nil
        }

        let fileName = original.fileName.flatMap { $0.isEmpty ? nil : $0 } ?? "crop.jpg"
        let outputURL = URL(fileURLWithPath: NSTemporaryDirectory())
            .appendingPathComponent(fileName)

        do {
            try data.write(to: outputURL, options: .atomic)
            return ImageData(
                uri: outputURL.path,
                fileName: fileName,
                mimeType: "image/jpeg",
                size: KotlinLong(longLong: Int64(data.count))
            )
        } catch {
            return nil
        }
    }
}
