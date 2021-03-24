package com.example.managerspecial.network


import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.KoinComponent
import java.util.concurrent.TimeUnit

class HttpClientFactory: KoinComponent {

        fun createDefaultOkHttpClientBuilder(): OkHttpClient.Builder {
            val builder = OkHttpClient.Builder()
//            if (inject<BuildConfigWrapper>().value.debug) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(logging)
//            }
            builder.readTimeout(TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            builder.connectTimeout(TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            return builder
        }

        val headerInterceptor: Interceptor
            get() = HeaderInterceptor()

        /**
         * Gets auth client with correct headers for oauth request
         *
         * @return OkHttpClient
         */
        val authClient: OkHttpClient
            get() {
                val builder = createDefaultOkHttpClientBuilder()
                return builder.build()
            }

        fun getCertPinner(hostName: String, pins: Array<String>): CertificatePinner {
            return CertificatePinner.Builder()
                .add(hostName, *pins)
                .build()
        }

        class HeaderInterceptor : Interceptor, KoinComponent {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val builder = request.newBuilder()
//                val userSession = inject<UserSession>().value
//                val token = userSession.accessToken
                setAuthHeader(builder, "token")
                return chain.proceed(builder.build())
            }

            private fun setAuthHeader(builder: Request.Builder, token: String?) {
                if (token != null) //Add Auth token to each request if authorized
                    builder.header("Authorization", String.format("Bearer %s", token))
            }
        }

        companion object {
            private const val TIMEOUT_SECONDS = 15
        }
    }





//
//
//
//
//    package com.premera.android.network
//
//    import okhttp3.OkHttpClient
//    import org.koin.core.KoinComponent
//    import org.koin.core.inject
//
//    class OkHttpRestClientBuilder(private val hostName: String, private val certPins: Array<String>) : OkHttpClientBuildInterface, KoinComponent {
//        private val builder: OkHttpClient.Builder
//        private val httpClientFactory by inject<HttpClientFactory>()
//
//        override fun build(): OkHttpClient {
//            builder.addInterceptor(httpClientFactory.headerInterceptor)
//            builder.certificatePinner(httpClientFactory.getCertPinner(hostName, certPins))
//            return builder.build()
//        }
//
//        init {
//            builder = httpClientFactory.createDefaultOkHttpClientBuilder()
//        }
//    }




//    package com.premera.android.network.request
//
//    import com.premera.android.common.IdlingResourceManager.beginNetworkLoad
//    import com.premera.android.common.IdlingResourceManager.endNetworkLoad
//    import com.premera.android.common.LiveDataResource
//    import com.premera.android.common.LiveDataResource.Companion.error
//    import com.premera.android.common.LiveDataResource.Companion.irrecoverableError
//    import com.premera.android.common.LiveDataResource.Companion.success
//    import com.premera.android.common.PMLog
//    import com.premera.android.common.SimpleConsumer
//    import com.premera.android.common.Status
//    import com.premera.android.extensions.toLiveDataResource
//    import com.premera.android.model.ModelValidator
//    import com.premera.android.network.response.RestFailureCode
//    import com.premera.android.network.service.ResultStatusType.CANCELLED
//    import org.koin.core.KoinComponent
//    import org.koin.core.inject
//    import retrofit2.Call
//    import retrofit2.Callback
//    import retrofit2.Response
//
//    class RestRequest<ApiType, ModelType : ModelValidator>(private val apiBuilder: RestCallBuilderInterface<ApiType>,
//                                                           private val dataCompleteCallback: SimpleConsumer<LiveDataResource<ModelType>>,
//                                                           private val apiCallParams: ApiCallFactoryInterface<ApiType, ModelType>,
//                                                           private val requestRetry: RestRequestRetry<ModelType>) : RetryCallbackInterface<ModelType>, Callback<ModelType>, KoinComponent {
//        private val pmLog by inject<PMLog>()
//        private var apiCallWithParams: Call<ModelType>? = null
//        fun fetch() {
//            apiCallWithParams = apiCallParams.create(apiBuilder.build())
//            beginNetworkLoad(apiCallWithParams?.request()?.url?.port ?:0)
//            apiCallWithParams!!.enqueue(this)
//        }
//
//        fun cancel() {
//            if (apiCallWithParams != null) apiCallWithParams?.cancel()
//        }
//
//        override val url: String?
//            get() = apiCallWithParams?.request()?.url?.toString()
//
//        override fun onResponse(call: Call<ModelType>, response: Response<ModelType>) {
//            pmLog.d("response: %s", response)
//            endNetworkLoad(call.request().url.port)
//            handleResponse(response.toLiveDataResource())
//        }
//
//        override fun onFailure(call: Call<ModelType>, t: Throwable) {
//            pmLog.d(t, "failure call: %s", call)
//            endNetworkLoad(call.request().url.port)
//            handleResponse(error(RestFailureCode(call).toStatusType()))
//        }
//
//        override fun retry() {
//            fetch()
//        }
//
//        override fun noRetry(result: LiveDataResource<ModelType>) {
//            if (apiBuilder.tryNextUrl()) {
//                requestRetry.reset() // retry again with new url
//                fetch()
//            } else {
//                dataCompleteCallback.onCallback(result)
//            }
//        }
//
//        fun handleResponse(result: LiveDataResource<ModelType>) {
//            if (result.error === CANCELLED) {
//                return
//            }
//            if (result.status === Status.SUCCESS) {
//                var data = result.data
//                if (data != null) {
//                    data = data.scrub() as ModelType
//                }
//                if (data?.isValid() == true) {
//                    dataCompleteCallback.onCallback(success(data))
//                } else {
//                    dataCompleteCallback.onCallback(irrecoverableError())
//                }
//            } else {
//                requestRetry.evaluate(result, this)
//            }
//        }

//    }
