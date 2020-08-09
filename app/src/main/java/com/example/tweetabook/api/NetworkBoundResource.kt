package com.example.tweetabook.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.tweetabook.common.Constants.NETWORK_TIMEOUT
import com.example.tweetabook.common.Constants.TESTING_NETWORK_DELAY
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

@InternalCoroutinesApi
abstract class NetworkBoundResource<ResponseObject, CacheObject>
    (
    isNetworkAvailable: Boolean,
    isNetworkRequest: Boolean,
    shouldCancelIfNoInternet: Boolean,
    shouldLoadFromCache: Boolean,
    saveToLocalDbAndThenInternetRequest: Boolean
) {
    private val TAG = "AppDebug: NetworkBoundResource"

    //  main live data
    protected val result = MediatorLiveData<ResponseObject>()

    protected lateinit var job: CompletableJob
    protected lateinit var coroutineScope: CoroutineScope

    init {
        setJob(initNewJob())
        setResult(null)

        if (shouldLoadFromCache) {
            // view cache to start
            coroutineScope.launch {
                val dbSource = loadFromCache()

                withContext(Main) {
                    result.addSource(dbSource) {
                        result.removeSource(dbSource)
                        setResult(it)
                    }
                }
            }
        }

        if (isNetworkRequest) {
            if (isNetworkAvailable) {
                doNetworkRequest()
            } else {
                if (shouldCancelIfNoInternet) {
                    Log.e(TAG, "No Internet, can't make request: ")
                } else {
                    doCacheRequest()
                }
            }
        } else {
            doCacheRequest()
        }

        if (saveToLocalDbAndThenInternetRequest) {
            coroutineScope.launch {
                saveLocallyAndThenMakeNetworkRequest()
            }
        }
    }

    private fun doCacheRequest() {
        coroutineScope.launch {
            // View data from cache only and return
            readCacheAndSetResult()
        }
    }

    private fun doNetworkRequest() {
        // launching both the couroutines, the latter will run after a delay of 6000ms cancelling the job

        coroutineScope.launch {
            // simulate a network delay for testing
            delay(TESTING_NETWORK_DELAY)

            withContext(IO) {

                // make network call
                val apiResponse = createCall()

                withContext(Main) {
                    result.addSource(apiResponse) { response ->
                        result.removeSource(apiResponse)

                        coroutineScope.launch {
                            handleNetworkResponse(response)
                        }
                    }
                }
            }
        }

        GlobalScope.launch(IO) {
            delay(NETWORK_TIMEOUT)

            if (!job.isCompleted) {
                Log.e(TAG, "NetworkBoundResource: JOB NETWORK TIMEOUT.")
                job.cancel(CancellationException("Network timeout"))
            }
        }
    }

    suspend fun handleNetworkResponse(response: ResponseObject) {
        handleApiSuccessResponse(response)
    }

    fun onCompleteJob(responseObject: ResponseObject) {
        GlobalScope.launch(Main) {
            job.complete()
            setResult(responseObject)
        }
    }

    fun onErrorReturn(error: Throwable?) {
        Log.e(TAG, "onErrorReturn: ", error)
        job.complete()
    }

    private fun setResult(responseObject: ResponseObject?) {
        result.value = responseObject
    }

    @InternalCoroutinesApi
    private fun initNewJob(): Job {
//        Log.d(TAG, "initNewJob: called.")
        job = Job()
        job.invokeOnCompletion(
            onCancelling = true,
            invokeImmediately = true,
            handler = object : CompletionHandler {
                override fun invoke(cause: Throwable?) {
                    if (job.isCancelled) {
                        Log.e(TAG, "NetworkBoundResource: Job has been cancelled.")
                        cause?.let {
                            onErrorReturn(it)
                        } ?: onErrorReturn(null)
                    } else if (job.isCompleted) {
                        Log.e(TAG, "NetworkBoundResource: Job has been completed.")
                        // Do nothing? Should be handled already
                    }
                }
            })
        coroutineScope = CoroutineScope(IO + job)
        return job
    }

    fun asLiveData() = result as LiveData<ResponseObject>

    abstract suspend fun readCacheAndSetResult()  //  set "result" to the cache returned

    abstract suspend fun handleApiSuccessResponse(response: ResponseObject) //  deals with network data if network call was successful

    abstract suspend fun createCall(): LiveData<ResponseObject> //  make a network call

    abstract suspend fun loadFromCache(): LiveData<ResponseObject>

    abstract suspend fun updateLocalDb(cacheObject: CacheObject?)   //  handleApiSuccessResponse -> updateLocalDb

    abstract fun setJob(job: Job)

    abstract suspend fun saveLocallyAndThenMakeNetworkRequest() //  get data from repository, save locally and then make network request
}















