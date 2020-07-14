package com.example.tweetabook.screens.main.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tweetabook.common.di.SingleActivityController
import com.example.tweetabook.screens.common.SingleActivity
import com.example.tweetabook.screens.main.repository.MainRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val singleActivityController: SingleActivityController) :
    ViewModel() {
    private val TAG = "AppDebug: MainViewModel"

    //  LIVE DATA
    val totalFiles: MutableLiveData<Int?> = MutableLiveData()

    private var mainRepository: MainRepository = MainRepository(
        singleActivityController.getBackendApi(),
        singleActivityController.mySocket
    )

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


    //  from mainFragmentController
    private fun getContext(): SingleActivity {
        return singleActivityController.getActivity()
    }


    //  calls to UIChangeListener
    private suspend fun showProgressBar() {
        withContext(Main) {
            getContext().showProgressBar()
        }
    }

    private suspend fun hideProgressBar() {
        withContext(Main) {
            getContext().hideProgressBar()
        }
    }
}