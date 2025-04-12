package com.example.dictionary.feature_dictionary.data.remote.dto


import com.example.dictionary.feature_dictionary.data.local.entry.WordInfoEntity
import com.example.dictionary.feature_dictionary.domain.model.WordInfo
import com.google.gson.annotations.SerializedName

data class WordInfoDto(
    @SerializedName("license")
    val license: License,
    @SerializedName("meanings")
    val meanings: List<MeaningDto>,
    @SerializedName("phonetic")
    val phonetic: String,
    @SerializedName("phonetics")
    val phonetics: List<PhoneticDto>,
    @SerializedName("sourceUrls")
    val sourceUrls: List<String>,
    @SerializedName("word")
    val word: String,
    @SerializedName("origin")
    val origin: String
) {
    fun toWordInfoEntity(): WordInfoEntity {
        return WordInfoEntity(
            meanings = meanings.map { it.toMeaning() },
            phonetic = phonetic,
            word = word,
            origin = origin
        )
    }
}

