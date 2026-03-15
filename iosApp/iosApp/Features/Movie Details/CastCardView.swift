//
// Created by Aakash Mangukiya on 15/03/26.
//

import SwiftUI
import Shared

struct CastCardView: View {

    let cast: Cast_

    var body: some View {

        VStack(alignment: .leading) {

            AsyncImage(url: URL(string: cast.profilePath)) { phase in
                switch phase {
                case .empty:
                    ZStack {
                        Color.gray.opacity(0.1)
                        ProgressView()
                    }
                case .success(let image):
                    image
                        .resizable()
                        .scaledToFill()
                case .failure:
                    ZStack {
                        Color.gray.opacity(0.1)
                        Image(systemName: "photo")
                            .resizable()
                            .scaledToFit()
                            .foregroundStyle(.secondary)
                            .padding(16)
                    }
                @unknown default:
                    EmptyView()
                }
            }
            .frame(width: 140, height: 150)
            .clipped()
            .cornerRadius(8, corners: [.topLeft, .bottomLeft])

            Text(cast.name)
                .font(.headline)
                .lineLimit(1)
                .padding(.horizontal, 4)

            Text(cast.character)
                .font(.subheadline)
                .foregroundColor(.gray)
                .lineLimit(1)
                .padding(.horizontal, 4)
                .padding(.bottom, 4)
        }
        .frame(width: 140)
        .background(Color.white)
        .cornerRadius(12)
        .shadow(radius: 3)
    }
}