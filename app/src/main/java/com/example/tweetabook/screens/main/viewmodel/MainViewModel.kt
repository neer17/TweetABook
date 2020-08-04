package com.example.tweetabook.screens.main.viewmodel

import android.content.Context
import android.net.Uri
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tweetabook.screens.common.showProgressBar
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

    fun uploadImageAndSendDownloadUriToServer(
        id: String,
        resultUri: Uri
    ) {
        viewModelScope.launch {
            mainRepository.uploadedImageAndSendDownloadUriToServer(id, resultUri)
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

    fun deleteAll() {
        viewModelScope.launch(IO) {
            mainRepository.deleteAll()
            getFilesCount()
        }
    }

    private suspend fun showProgressBar() {
        withContext(Main) {
            context.showProgressBar(true)
        }
    }

    private suspend fun hideProgressBar() {
        withContext(Main) {
            context.showProgressBar(false)
        }
    }
}