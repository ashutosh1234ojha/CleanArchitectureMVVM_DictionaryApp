package com.example.dictionary.feature_dictionary.domain.model


data class WordInfo(
    val meanings: List<Meaning>,
    val phonetic: String?,
    val word: String,
    val origin: String?
)