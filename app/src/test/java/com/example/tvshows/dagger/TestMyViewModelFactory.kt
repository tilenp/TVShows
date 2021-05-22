package com.example.tvshows.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.example.tvshows.ui.showdetails.ShowDetailsViewModel
import com.example.tvshows.ui.showslist.ShowSummariesViewModel
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@Suppress("UNCHECKED_CAST")
class TestMyViewModelFactory @Inject constructor(): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowSummariesViewModel::class.java)) {
            return showSummariesViewModel as T
        }
        if (modelClass.isAssignableFrom(ShowDetailsViewModel::class.java)) {
            return showDetailsViewModel as T
        }
        throw RuntimeException("unknown viewModel")
    }

    companion object {
        lateinit var showSummariesViewModel: ShowSummariesViewModel
        lateinit var showDetailsViewModel: ShowDetailsViewModel
    }
}