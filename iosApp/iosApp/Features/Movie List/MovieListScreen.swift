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
    
    var body: some View {
        
        NavigationStack(path: $navigationPath) {
            
            ScrollView {
                if viewModel.movieUiState.error != nil && viewModel.movieUiState.movies.isEmpty {
                    VStack(spacing: 12) {
                        Text(viewModel.movieUiState.error ?? String(localized: "movies.error.generic"))
                            .multilineTextAlignment(.center)
                            .foregroundStyle(.secondary)
                        Button(String(localized: "common.retry")) {
                            viewModel.onEvent(MovieListEvent.LoadMovies())
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
                        
                        ForEach(viewModel.movieUiState.movies, id: \.id){ movie in
                            MovieRow(movie: movie)
                                .onTapGesture {
                                    navigationPath.append(movie)
                                }
                                .onAppear {
                                    if movie.id == viewModel.movieUiState.movies.last?.id {
                                        if !viewModel.movieUiState.isLoading && !viewModel.movieUiState.isEndReached {
                                            viewModel.onEvent(MovieListEvent.LoadNextPage())
                                        }
                                    }
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
            .navigationDestination(for: Movie_.self) { movie in
                MovieDetailsScreen(movie: movie)
            }
            .onAppear {
                if viewModel.movieUiState.movies.isEmpty {
                    viewModel.onEvent(MovieListEvent.LoadMovies())
                }
            }
        }
    }
}

#Preview {
    MovieListScreen()
}
