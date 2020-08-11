package com.example.tweetabook.screens.main.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.tweetabook.db.daos.TweetDAO
import com.example.tweetabook.db.entities.TweetEntity
import com.example.tweetabook.firebase.deleteAllFiles
import com.example.tweetabook.firebase.filesCount
import com.example.tweetabook.firebase.uploadFile
import com.example.tweetabook.socket.MySocket
import com.example.tweetabook.socket.responses.ServerResponse
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DefaultMainRepository
@Inject
constructor(
    val mySocket: MySocket,
    val tweetDAO: TweetDAO,
    val networkPresent: Boolean
) : MainRepository {
    private val TAG = "AppDebug: MainRepository"

    private val jobList = ArrayList<MyJob>()

    data class MyJob(
        val id: String,
        val localImageUri: String,
        var firebaseUploadDone: Boolean = false,
        var translationDone: Boolean = false
    )


    override suspend fun registerJobAndExecuteOnNetworkPresent(id: String, localImageUri: String) {
        jobList.add(MyJob(id, localImageUri))
        uploadToStorageAndTranslate()
    }

    override suspend fun uploadToStorageAndTranslate() {
        jobList.forEach {
            if (networkPresent) {
                coroutineScope {
                    withContext(Dispatchers.IO) {
                        val storageImageUri = uploadFile(it.localImageUri)
                        it.firebaseUploadDone = true
                        tweetDAO.updateTweet(
                            TweetEntity(
                                id = it.id,
                                imageUri = storageImageUri.toString()
                            )
                        )
                        translateImageToUrl(it.id, storageImageUri.toString())
                    }
                }
            }
        }
    }

    /* @InternalCoroutinesApi
     fun saveToDbAndUploadImageToFirebase(id: String, localImageUri: Uri): LiveData<TweetsEntity> {
         return object : NetworkBoundResource<TweetsEntity, TweetsEntity>(
             networkPresent,
             true,
             true,
             true,
             true
         ) {
             override suspend fun saveLocallyAndThenMakeNetworkRequest() {
                 updateLocalDb(TweetsEntity(id, localImageUri))
                 readCacheAndSetResult()
             }

             override suspend fun readCacheAndSetResult() {
                 withContext(Main) {
                     result.addSource(loadFromCache()) {
                         onCompleteJob(it)
                     }
                 }
             }

             override suspend fun handleApiSuccessResponse(response: TweetsEntity) {
                 TODO("Not yet implemented")
             }

             override suspend fun createCall(): LiveData<TweetsEntity> {
                 //  upload image to Firebase storage
                 uploadFile(localImageUri)?.let { downloadUri ->
                     //   send image to server
                     sendDownloadUrlToServer(id, downloadUri)
                 }
             }

             override suspend fun loadFromCache(): LiveData<TweetsEntity> {
                 return tweetDAO.getAllTweets()
             }

             override suspend fun updateLocalDb(cacheObject: TweetsEntity?) {
                 cacheObject?.let {
                     tweetDAO.insertTweet(it)
                 }
             }

             override fun setJob(job: Job) {
                 TODO("Not yet implemented")
             }
         }.asLiveData()
     }*/

    override fun translateImageToUrl(id: String, downloadUri: String) {
        downloadUri.let {
            val json = JsonObject()
            json.addProperty("id", id)
            json.addProperty("uri", it)
            socketEmitEvent(json)
        }
    }

    override suspend fun getFilesCount(): Int? {
        return filesCount()
    }

    override suspend fun emptyStorage() {
        deleteAllFiles()
    }

    override suspend fun deleteAllTweets() {
        val allTweets= tweetDAO.getAllTweetsWithOutLiveData()
        allTweets?.let {
            val tweetsDeleted = tweetDAO.deleteAll(allTweets)
            Log.d(TAG, "deleteAllTweets: tweetsDeleted: $tweetsDeleted")
        }
    }

    override fun socketEmitEvent(json: JsonObject) {
        mySocket.socketEmitEvent(json)
    }

    override fun socketResponse(): LiveData<ServerResponse> {
        return mySocket.socketResponse
    }

    override fun exposeJobList(): ArrayList<MyJob> = jobList
}