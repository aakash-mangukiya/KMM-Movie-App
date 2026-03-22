//
//  MovieRow.swift
//  iosApp
//
//  Created by Aakash Mangukiya on 08/03/26.
//

import SwiftUI
import Shared

struct MovieVerticalRow: View {
    
    let movie: Movie_
    
    var body: some View {
        
        VStack(alignment: .leading) {
            
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
            .frame(width: 140, height: 160)
            .clipped()
            
            Text(movie.title)
                .font(.headline)
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
