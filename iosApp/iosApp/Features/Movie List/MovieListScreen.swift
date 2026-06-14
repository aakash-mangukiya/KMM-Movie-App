//
//  MovieListScreen.swift
//  iosApp
//
//  Created by Aakash Mangukiya on 08/03/26.
//

import SwiftUI
import Shared

struct MovieListScreen: View {
    
    @StateObject private var viewModel = MovieListViewModelWrapper()
    @State private var navigationPath = NavigationPath()
    @State private var showsProfileScreen = false
    
    var body: some View {
        
        NavigationStack(path: $navigationPath) {
            
            ScrollView {
                if viewModel.movieUiState.error != nil && viewModel.movieUiState.movies.isEmpty {
                    VStack(spacing: 12) {
                        Text(viewModel.movieUiState.error ?? String(localized: "movies.error.generic"))
                            .multilineTextAlignment(.center)
                            .foregroundStyle(.secondary)
                        Button(String(localized: "common.retry")) {
                            viewModel.reloadMovies()
                        }
                        .buttonStyle(.borderedProminent)
                    }
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 24)
                    .listRowSeparator(.hidden)
                } else if viewModel.movieUiState.isLoading && viewModel.movieUiState.movies.isEmpty {
                    ProgressView()
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                        .padding()
                } else {
                    LazyVStack(alignment: .leading, spacing:16){
                        
                        HStack {
                            ParallaxCarouselView(
                                movies: viewModel.movieUiState.nowPlayingMovies,
                                onItemClick: { movie in
                                    navigationPath.append(movie)
                                }
                            )
                        }
                        
                        ForEach(viewModel.movieUiState.movies, id: \.id){ movie in
                            MovieRow(movie: movie)
                                .onTapGesture {
                                    navigationPath.append(movie)
                                }
                                .task(id: movie.id) {
                                    viewModel.loadNextPageIfNeeded(currentMovieId: movie.id)
                                }
                        }
                        
                        if viewModel.movieUiState.isLoading {
                            HStack {
                                Spacer()
                                ProgressView(String(localized: "movies.loading"))
                                Spacer()
                            }
                            .padding()
                        }
                    }
                    .padding(.vertical)
                }
            }
            .navigationTitle(String(localized: "movies.title"))
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button {
                        showsProfileScreen = true
                    } label: {
                        Image(systemName: "person.crop.circle")
                    }
                }
            }
            .navigationDestination(isPresented: $showsProfileScreen) {
                ProfileScreen()
            }
            .navigationDestination(for: Movie_.self) { movie in
                MovieDetailsScreen(movie: movie)
            }
            .task {
                viewModel.loadMoviesIfNeeded()
            }
        }
    }
}

#Preview {
    MovieListScreen()
}
