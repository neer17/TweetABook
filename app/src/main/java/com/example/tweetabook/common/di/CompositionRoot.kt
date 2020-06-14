package com.example.tweetabook.common.di;


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.example.tweetabook.api.MyBackendApi
import com.example.tweetabook.common.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


public class CompositionRoot {

    private fun getGsonBuilder(): Gson {
        return GsonBuilder().setLenient()
            .create()
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(getGsonBuilder()))
            .build();
    }

    fun getBackendApi(): MyBackendApi {
        return getRetrofit().create(MyBackendApi::class.java)
    }

    fun registerNetworkCallback(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}
