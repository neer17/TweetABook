package com.example.tweetabook.screens.main.viewmodel

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tweetabook.screens.main.repository.MainRepository
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel
@ViewModelInject
constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    @ActivityContext private val context: Context,
    private val mainRepository: MainRepository
) : ViewModel() {
    private val TAG = "AppDebug: MainViewModel"

    //  LIVE DATA
    val totalFiles: MutableLiveData<Int?> = MutableLiveData()

    val serverResponse = mainRepository.socketResponse()

    fun uploadToStorageAndTranslate(
        id: String,
        localImageUri: String
    ) {
        viewModelScope.launch {
            mainRepository.registerJobAndExecuteOnNetworkPresent(id, localImageUri)
            getFilesCount()
        }
    }

    //  update the file count in toolbar
    fun getFilesCount() {
        viewModelScope.launch(IO) {
            val value = mainRepository.getFilesCount()
            withContext(Main) {
                totalFiles.value = value
            }
        }
    }

    fun emptyStorage() {
        viewModelScope.launch(IO) {
            mainRepository.emptyStorage()
            getFilesCount()
        }
    }

    fun deleteAllTweets() {
        viewModelScope.launch(IO) {
            mainRepository.deleteAllTweets()
        }
    }

    fun removeJobWhenTranslationIsDone(id: String) {
        val jobList = mainRepository.exposeJobList()
        jobList.removeIf {
            id == it.id
        }
    }
}