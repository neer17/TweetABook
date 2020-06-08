package com.example.tweetabook.common.di;


import com.example.tweetabook.api.MyBackendApi
import com.example.tweetabook.common.Constants
import me.linshen.retrofit2.adapter.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

public class CompositionRoot {

    private lateinit var retrofit: Retrofit;

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build();
    }

    fun getBackendApi(): MyBackendApi {
        return getRetrofit().create(MyBackendApi::class.java)
    }
}
