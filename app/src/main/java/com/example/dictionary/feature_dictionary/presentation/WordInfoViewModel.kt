package com.example.dictionary.feature_dictionary.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.core.util.Resource
import com.example.dictionary.feature_dictionary.domain.repository.WordInfoRepository
import com.example.dictionary.feature_dictionary.domain.usecase.GetWordInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordInfoViewModel @Inject constructor(val getWordInfo: GetWordInfo) : ViewModel() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _state = mutableStateOf(WordInfoState())
    val state: State<WordInfoState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob: Job? = null

    fun onSearch(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)

            getWordInfo.invoke(query).onEach {
                when (it) {
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            wordInfo = it.data ?: emptyList(),
                            isLoading = false
                        )
                    }

                    is Resource.Failure -> {
                        _state.value = state.value.copy(
                            wordInfo = it.data ?: emptyList(),
                            isLoading = false
                        )
                        _eventFlow.emit(UIEvent.ShowSnackBar(it.message ?: "Unknown error"))
                    }

                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            wordInfo = it.data ?: emptyList(),
                            isLoading = true
                        )
                    }
                }
            }.launchIn(this)

        }
    }


    sealed class UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent()
    }
}