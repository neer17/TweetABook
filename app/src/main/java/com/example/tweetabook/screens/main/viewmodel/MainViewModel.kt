package com.example.tweetabook.screens.main.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tweetabook.common.UIEvent
import com.example.tweetabook.common.di.ControllerCompositionRoot
import com.example.tweetabook.screens.common.SingleActivity
import com.example.tweetabook.screens.main.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val controllerCompositionRoot: ControllerCompositionRoot) :
    ViewModel() {
    private val TAG = "AppDebug: MainViewModel"

    val downloadUri = MutableLiveData<Uri>()
    val serverResponse = MutableLiveData<String>()
    val dataState = MutableLiveData<UIEvent>()
    val totalFiles: MutableLiveData<Int?> = MutableLiveData()

    private var mainRepository: MainRepository =
        MainRepository(controllerCompositionRoot.getBackendApi(), controllerCompositionRoot.getMySocket())


    private fun uploadAndGetDownloadUri(fileUri: Uri) {
        viewModelScope.launch {
            showProgressBar()
            mainRepository.uploadFileToStorage(fileUri)?.let {
                downloadUri.value = it
            }
            getFilesCount()
            hideProgressBar()
        }
    }

    fun sendDownloadUrlToServer(downloadUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            showProgressBar()
            mainRepository.sendDownloadUrlToServer(downloadUri)
            hideProgressBar()
        }
    }

    //  update the file count in toolbar
    fun getFilesCount() {
        viewModelScope.launch(Dispatchers.IO) {
            val value = mainRepository.getFilesCount()
            withContext(Dispatchers.Main) {
                totalFiles.value = value
            }
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.deleteAll()
            getFilesCount()
        }
    }


    //  SOCKET.IO
    fun socketIOConnection() {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.socketIOConnection()
        }
    }

    fun socketIODisconnection() {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.socketIODisconnection()
        }
    }


    //  SETTERS
    fun setFileUri(fileUri: Uri) {
        uploadAndGetDownloadUri(fileUri)
    }


    //  from controller composition
    private fun getContext(): SingleActivity {
        return controllerCompositionRoot.getActivity()
    }


    //  calls to UIChangeListener
    private fun showProgressBar() {
        getContext().showProgressBar()
    }

    private fun hideProgressBar() {
        getContext().hideProgressBar()
    }
}