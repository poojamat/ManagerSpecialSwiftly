package com.example.managerspecial

import com.example.managerspecial.network.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single { HttpClientFactory() }
    single { OkHttpRestClientBuilder()}
    single { Repository()}
    viewModel{ ManagerSpecialViewModel() }
}