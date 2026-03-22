//
//  MovieDetailsScreen.swift
//  iosApp
//
//  Created by Aakash Mangukiya on 08/03/26.
//

import SwiftUI
import Shared

struct MovieDetailsScreen: View {
    
    @StateObject private var viewModel = MovieDetailsViewModelWrapper()
    
    let movie: Movie_
    
    var body: some View {
        
        Group {
            if viewModel.movieDetailsUiState.isLoading {
                ProgressView(String(localized: "movies.loading"))
            } else if let movieDetails = viewModel.movieDetailsUiState.movieDetails {
                ScrollView {
                    
                    VStack(alignment: .leading, spacing: 16) {
                        
                        AsyncImage(url: URL(string: movieDetails.image)) { phase in
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
                        .frame(maxWidth: .infinity, minHeight: 250)
                        .clipped()
                        
                        VStack(alignment: .leading, spacing: 12) {
                            
                            Text(movieDetails.title)
                                .font(.title)
                                .fontWeight(.bold)
                            
                            Text(movieDetails.tagLine)
                                .italic()
                                .foregroundColor(.gray)
                            
                            Text(movieDetails.overview)
                                .font(.body)
                            
                            HStack {
                                
                                VStack(alignment: .leading) {
                                    Text(String(localized: "movie.details.director"))
                                        .font(.headline)
                                    Text(movieDetails.director)
                                }
                                .frame(maxWidth: .infinity, alignment: .leading)
                                
                                VStack(alignment: .leading) {
                                    Text(String(localized: "movie.details.writer"))
                                        .font(.headline)
                                    Text(movieDetails.writer)
                                }
                                .frame(maxWidth: .infinity, alignment: .leading)
                            }
                            
                            Text(String(localized: "movie.details.cast")).font(.headline)
                            ScrollView(.horizontal, showsIndicators: false) {
                                HStack() {
                                    ForEach(movieDetails.cast, id: \.self) { cast in
                                        CastCardView(cast: cast)
                                    }
                                }.padding(.vertical)
                            }
                            
                            HStack(alignment: .top, spacing: 0) {
                                
                                InfoRowView(
                                    title: String(localized: "movie.details.rate"),
                                    value: String(format: "%.1f / 10",(movieDetails.voteAverage))
                                )
                                .frame(maxWidth: .infinity, alignment: .leading)
                                
                                InfoRowView(
                                    title: String(localized: "movie.details.status"),
                                    value: movieDetails.status
                                )
                                .frame(maxWidth: .infinity, alignment: .leading)
                            }
                            
                            HStack(alignment: .top, spacing: 0) {
                                
                                InfoRowView(
                                    title: String(localized: "movie.details.revenue"),
                                    value: "\(movieDetails.revenue)"
                                )
                                .frame(maxWidth: .infinity, alignment: .leading)
                                
                                InfoRowView(
                                    title: String(localized: "movie.details.budget"),
                                    value: "\(movieDetails.budget)"
                                )
                                .frame(maxWidth: .infinity, alignment: .leading)
                            }
                            
                            Text(String(localized: "movie.details.similar")).font(.headline)
                            ScrollView(.horizontal, showsIndicators: false) {
                                HStack() {
                                    ForEach(movieDetails.similarMovies, id: \.self) { movie in
                                        MovieVerticalRow(movie: movie)
                                    }
                                }.padding(.vertical)
                            }
                        }
                        .padding()
                    }
                }
            } else if let error = viewModel.movieDetailsUiState.error {
                VStack(spacing: 12) {
                    Text(error)
                        .multilineTextAlignment(.center)
                        .foregroundStyle(.secondary)
                    Button(String(localized: "common.retry")) {
                        viewModel.onEvent(MovieDetailsEvent.LoadMovieDetails(movieId: movie.id))
                    }
                    .buttonStyle(.borderedProminent)
                }
                .frame(maxWidth: .infinity)
                .padding(.vertical, 24)
                
                //Text(error).foregroundColor(.red)
            }
        }
        .navigationTitle(movie.title)
        .navigationBarTitleDisplayMode(.inline)
        .onAppear {
            viewModel.onEvent(MovieDetailsEvent.LoadMovieDetails(movieId: movie.id))
        }
    }
}
