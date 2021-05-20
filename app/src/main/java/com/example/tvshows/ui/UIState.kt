package com.example.tvshows.ui

sealed class UIState {
    object Loading : UIState()
    object Success : UIState()
    object Retry : UIState()
    object Default : UIState()
}