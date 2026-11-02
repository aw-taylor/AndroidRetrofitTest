package com.awt.retrofittest.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.awt.retrofittest.RetroApplication
import com.awt.retrofittest.data.RetroRepository
import com.awt.retrofittest.network.RetroTracks
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

sealed interface HomeUiState {
    data class Success(val tracks: Response<RetroTracks>) : HomeUiState
    object Error : HomeUiState
    object Loading : HomeUiState
}

class HomeViewModel(
    private val retroRepository: RetroRepository
) : ViewModel() {
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init {
        getTrackData(
            term = "the+beatles",
            country = "GB",
            limit = 18
        )
    }

    fun getTrackData(
        term: String,
        country: String,
        limit: Int
    ) {
        viewModelScope.launch {
            homeUiState = try {
                HomeUiState.Success(retroRepository.getTracks(
                    term,
                    country,
                    limit
                ))
            } catch(e: IOException) {
                HomeUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as RetroApplication)
                val retroRepository = application.container.retroRepository
                HomeViewModel(retroRepository = retroRepository)
            }
        }
    }
}