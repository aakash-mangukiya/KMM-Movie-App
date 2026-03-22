//
//  MovieCarousel.swift
//  iosApp
//
//  Created by Aakash Mangukiya on 22/03/26.
//

import SwiftUI
import Shared

struct ParallaxCarouselView: View {

    let movies: [Movie_]
    var onItemClick: (Movie_) -> Void
    @State private var currentIndex: Int = 0

    var body: some View {
        VStack(spacing: 16) {

            GeometryReader { geo in
                let width = geo.size.width

                TabView(selection: $currentIndex) {
                    ForEach(Array(movies.enumerated()), id: \.element.id) { index, movie in
                        ParallaxCardView(
                            movie: movie,
                            index: index,
                            currentIndex: currentIndex,
                            width: width,
                            onClick: {
                                onItemClick(movie)
                            }
                        )
                        .padding(.horizontal, 16)
                        .tag(index)
                    }
                }
                .tabViewStyle(.page(indexDisplayMode: .never))
            }
            .frame(height: 250)

            // Indicator
            HStack(spacing: 8) {
                ForEach(0..<movies.count, id: \.self) { index in
                    Circle()
                        .fill(index == currentIndex ? Color.blue : Color.gray.opacity(0.4))
                        .frame(width: index == currentIndex ? 10 : 8,
                               height: index == currentIndex ? 10 : 8)
                        .animation(.easeInOut, value: currentIndex)
                }
            }
        }
    }
}

struct ParallaxCardView: View {

    let movie: Movie_
    let index: Int
    let currentIndex: Int
    let width: CGFloat
    var onClick: () -> Void

    var body: some View {
        GeometryReader { geo in
            let cardWidth = geo.size.width

            // Parallax offset
            let minX = geo.frame(in: .global).minX
            let offset = -minX / 10

            ZStack(alignment: .bottomLeading) {

                AsyncImage(url: URL(string: movie.posterImage)) { phase in
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
                .frame(width: cardWidth, height: 250)
                .clipped()
                .offset(x: offset)

                LinearGradient(
                    colors: [Color.black.opacity(0.0), Color.black.opacity(0.7)],
                    startPoint: .center,
                    endPoint: .bottom
                )

                Text(movie.title)
                    .foregroundColor(.white)
                    .font(.headline)
                    .padding()
            }
            .cornerRadius(16)
            .shadow(radius: 5)
            .onTapGesture {
                onClick()
            }
        }
    }
}
