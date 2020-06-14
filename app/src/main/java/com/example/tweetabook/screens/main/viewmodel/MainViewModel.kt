package com.example.tweetabook.screens.main.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tweetabook.common.di.ControllerCompositionRoot
import com.example.tweetabook.screens.main.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val controllerCompositionRoot: ControllerCompositionRoot) :
    ViewModel() {
    private val TAG = "AppDebug: MainViewModel"


    private var mainRepository: MainRepository =
        MainRepository(controllerCompositionRoot.getBackendApi())

    val downloadUri = MutableLiveData<Uri>()
    val resultString = MutableLiveData<String>()

    private fun uploadAndGetDownloadUri(fileUri: Uri) {
        viewModelScope.launch {
            mainRepository.uploadFileToStorage(fileUri)?.let {
                downloadUri.value = it
            }
        }
    }

    fun sendDownloadUrlToServer(downloadUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = mainRepository.sendDownloadUrlToServer(downloadUri)
            withContext(Dispatchers.Main) {
                result?.let {
                    resultString.value = it
                }
            }
        }
    }


    fun setFileUri(fileUri: Uri) {
        uploadAndGetDownloadUri(fileUri)
    }
}