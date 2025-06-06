package com.example.dictionary.feature_dictionary.presentation

import com.example.dictionary.feature_dictionary.domain.model.WordInfo

data class WordInfoState(
    val wordInfo: List<WordInfo> = emptyList(),
    val isLoading:Boolean  = false
)