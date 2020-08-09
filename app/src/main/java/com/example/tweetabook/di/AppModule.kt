package com.example.tweetabook.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.room.Room
import com.example.tweetabook.api.MyBackendApi
import com.example.tweetabook.common.Constants
import com.example.tweetabook.db.AppDatabase
import com.example.tweetabook.db.daos.TweetDAO
import com.example.tweetabook.mappers.TweetMappers
import com.example.tweetabook.screens.main.repository.DefaultMainRepository
import com.example.tweetabook.screens.main.repository.MainRepository
import com.example.tweetabook.socket.MySocket
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideTweetMapper(): TweetMappers = TweetMappers()

    @Provides
    fun connectedToNetwork(@ApplicationContext context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}

@Module
@InstallIn(ApplicationComponent::class)
object MainRepositoryModule {
    @Singleton
    @Provides
    fun provideMainRepository(mySocket: MySocket, tweetDAO: TweetDAO, networkPresent: Boolean): MainRepository {
        return DefaultMainRepository(mySocket, tweetDAO, networkPresent)
    }

}

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun provideAppDb(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideTweetDao(db: AppDatabase): TweetDAO {
        return db.getTweetDao()
    }
}

@Module
@InstallIn(ApplicationComponent::class)
object SocketIOModule {

    @Singleton
    @Provides
    fun getSocket(): MySocket = MySocket()
}

@Module
@InstallIn(ApplicationComponent::class)
object RetrofitModule {

    @Provides
    fun getGsonBuilder(): Gson {
        return GsonBuilder().setLenient()
            .create()
    }

    @Provides
    fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Provides
    fun getClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)

        builder.networkInterceptors().add(getHttpLoggingInterceptor())
        return builder.build()
    }

    @Provides
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(getClient())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(getGsonBuilder()))
            .build()
    }

    @Singleton
    @Provides
    fun getBackendApi(): MyBackendApi {
        return getRetrofit()
            .create(MyBackendApi::class.java)
    }

}
