package com.example.tweetabook.screens.main.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tweetabook.screens.main.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {
    private val TAG = "AppDebug: MainViewModel"

    private var mainRepository: MainRepository = MainRepository()
    val downloadUri = MutableLiveData<Uri>()

    private fun uploadAndGetDownloadUri(fileUri: Uri) {
        viewModelScope.launch {
            mainRepository.uploadFileToStorage(fileUri)?.let {
                downloadUri.value = it
            }
        }
    }

    fun setFileUri(fileUri: Uri) {
        uploadAndGetDownloadUri(fileUri)
    }
}