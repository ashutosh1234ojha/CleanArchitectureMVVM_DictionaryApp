package com.example.dictionary.feature_dictionary.data.repository

import com.example.dictionary.core.util.Resource
import com.example.dictionary.feature_dictionary.data.local.WordInfoDao
import com.example.dictionary.feature_dictionary.data.remote.DictionaryApi
import com.example.dictionary.feature_dictionary.domain.model.WordInfo
import com.example.dictionary.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordInfoRepositoryImpl(private val api: DictionaryApi, private val dao: WordInfoDao) :
    WordInfoRepository {
    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {
        emit(Resource.Loading())

        val wordInfo = dao.getWordInfos(word).map { it.toWordInfo() }


        try {

            val remoteWordInfo = api.getWordInfo(word)
            dao.deleteWordInfos(remoteWordInfo.map { it.word })
            dao.insertWordInfos(remoteWordInfo.map { it.toWordInfoEntity() })
        } catch (e: HttpException) {
            emit(Resource.Failure(wordInfo, "Something went wrong"))
        } catch (e: IOException) {
            emit(
                Resource.Failure(
                    wordInfo,
                    "Couldn't reach server, check your internet connection"
                )
            )

        }

        val newWordInfo = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Success(newWordInfo))

    }
}