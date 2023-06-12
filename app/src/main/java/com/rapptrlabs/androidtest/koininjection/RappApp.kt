package com.rapptrlabs.androidtest.koininjection

import android.app.Application
import com.rapptrlabs.androidtest.remote.Endpoints
import com.rapptrlabs.androidtest.repository.SharedRepository
import com.rapptrlabs.androidtest.repository.SharedViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RappApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@RappApp)
            modules(allModules())
        }
    }

    private fun allModules(): List<Module> {
        val networkModule = module {
            single {
                val okHttpClient = OkHttpClient.Builder().apply {
                    readTimeout(30, TimeUnit.SECONDS)
                    connectTimeout(30, TimeUnit.SECONDS)
                }.build()

                Retrofit.Builder()
                    .baseUrl("http://dev.rapptrlabs.com/Tests/scripts/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(Endpoints::class.java)
            }
        }

        val appModule = module {
            single { SharedRepository(get()) }
            viewModel { SharedViewModel(get()) }
        }

        return listOf(networkModule, appModule)
    }
}