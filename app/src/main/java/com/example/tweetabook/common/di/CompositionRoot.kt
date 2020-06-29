package com.example.tweetabook.common.di;


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.example.tweetabook.api.MyBackendApi
import com.example.tweetabook.common.Constants
import com.example.tweetabook.socket.MySocket
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


public class CompositionRoot {

    private fun getGsonBuilder(): Gson {
        return GsonBuilder().setLenient()
            .create()
    }

    private fun getHttpLOgginInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    private fun getClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)

        builder.networkInterceptors().add(getHttpLOgginInterceptor())
        return builder.build()
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(getClient())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(getGsonBuilder()))
            .build()
    }

    fun getBackendApi(): MyBackendApi {
        return getRetrofit().create(MyBackendApi::class.java)
    }

    fun registerNetworkCallback(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }


    //  SOCKET.IO
    fun getMySocket(): MySocket = MySocket()
}
