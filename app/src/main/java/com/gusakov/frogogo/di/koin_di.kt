package com.gusakov.frogogo.di

import com.gusakov.frogogo.BuildConfig
import com.gusakov.frogogo.model.User
import com.gusakov.frogogo.repostiory.UserApiService
import com.gusakov.frogogo.repostiory.UserRepository
import com.gusakov.frogogo.repostiory.UserRepositoryImpl
import com.gusakov.frogogo.repostiory.dto.UserDto
import com.gusakov.frogogo.repostiory.mapper.NullableInputListMapper
import com.gusakov.frogogo.repostiory.mapper.UserDtoToUserMapper
import com.gusakov.frogogo.ui.create_user.CreateOrModifyUserViewModel
import com.gusakov.frogogo.ui.users.UsersViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    factory<UserRepository> { UserRepositoryImpl(get(), get()) }
    factory<NullableInputListMapper<UserDto, User>> { UserDtoToUserMapper() }
}

val viewModelModule = module {
    viewModel { UsersViewModel(get()) }
    viewModel { CreateOrModifyUserViewModel(get()) }
}

val networkModule = module {
    factory { provideOkHttpClient() }
    factory { provideUserApiService(get()) }
    single { provideRetrofit(get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.API_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideOkHttpClient(): OkHttpClient {
    val builder = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(httpLoggingInterceptor)
    }
    return builder.build()
}

fun provideUserApiService(retrofit: Retrofit): UserApiService =
    retrofit.create(UserApiService::class.java)
