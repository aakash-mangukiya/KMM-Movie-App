//
//  MovieListViewModelWrapper.swift
//  iosApp
//
//  Created by Aakash Mangukiya on 08/03/26.
//

import SwiftUI
import Shared

@MainActor
class MovieListViewModelWrapper: ObservableObject {
    
    let movieListViewModel: MovieListViewModel
    @Published var movieUiState: MovieUiState
    
    init() {
        let koin = KoinKt.getKoinInstance()
        self.movieListViewModel = koin.getMovieListViewModel()
        self.movieUiState = MovieUiState(isLoading: false, movies: [], nowPlayingMovies: [], error: nil, currentPage: 1, isEndReached: false)
        
        Task {
            let sequence = SkieSwiftFlow(movieListViewModel.uiState)
            for await state in sequence {
                self.movieUiState = state
            }
        }
    }
    
    func onEvent(_ event: MovieListEvent) {
        movieListViewModel.handleEvent(event: event)
    }
}
