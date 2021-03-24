package com.example.managerspecial.network

import okhttp3.OkHttpClient
import org.koin.core.KoinComponent
import org.koin.core.inject

class OkHttpRestClientBuilder : KoinComponent {
    private val builder: OkHttpClient.Builder
    private val httpClientFactory by inject<HttpClientFactory>()

    fun build(): OkHttpClient {
        //builder.addInterceptor(httpClientFactory.headerInterceptor)
        //builder.certificatePinner(httpClientFactory.getCertPinner(hostName, certPins))
        return builder.build()
    }

    init {
        builder = httpClientFactory.createDefaultOkHttpClientBuilder()
    }
}

