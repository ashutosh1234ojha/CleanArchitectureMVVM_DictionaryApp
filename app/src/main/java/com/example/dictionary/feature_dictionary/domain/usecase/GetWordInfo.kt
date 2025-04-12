package com.example.dictionary.feature_dictionary.domain.usecase

import com.example.dictionary.core.util.Resource
import com.example.dictionary.feature_dictionary.domain.model.WordInfo
import com.example.dictionary.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWordInfo(private val wordInfoRepository: WordInfoRepository) {

    operator fun invoke(word: String): Flow<Resource<List<WordInfo>>> {
        if(word.isBlank()){
              return  flow {  }
        }
       return wordInfoRepository.getWordInfo(word)

    }
}