//
//  MovieRow.swift
//  iosApp
//
//  Created by Aakash Mangukiya on 08/03/26.
//

import SwiftUI
import Shared

struct MovieRow: View {
    let movie: Movie_
    
    var body: some View {
        HStack(alignment: .top, spacing: 0) {
            AsyncImage(url: URL(string: movie.image)) { phase in
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
            .frame(width: 110, height: 150)
            .clipped()
            .cornerRadius(8, corners: [.topLeft, .bottomLeft])
            
            VStack(alignment: .leading, spacing: 6) {
                Text(movie.title)
                    .font(.headline)
                    .fontWeight(.bold)
                    .lineLimit(2)
                Text(movie.overview)
                    .font(.subheadline)
                    .foregroundStyle(.secondary)
                    .lineLimit(3)
            }
            .padding(8)
        }
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(Color.white)
        )
        .shadow(color: Color.black.opacity(0.15), radius: 8, x: 0, y: 4)
        .padding(.horizontal)
    }
}
