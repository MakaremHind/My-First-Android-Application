package com.automacorp.service
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager
import kotlin.Throws

object ApiServices {
    // Basic Authentication Credentials
    const val API_USERNAME = "user"
    const val API_PASSWORD = "password"

    // Rooms API Service instance
    val roomsApiService: RoomsApiService by lazy {
        val client = getUnsafeOkHttpClient()
            .addInterceptor(BasicAuthInterceptor(API_USERNAME, API_PASSWORD))
            .build()

        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create()) // (1) JSON converter
            .client(client) // Attach the customized OkHttpClient
            .baseUrl("https://automacorp.devmind.cleverapps.io/api/") // (2) API base URL
            .build()
            .create(RoomsApiService::class.java)
    }

    // Interceptor for Basic Authentication
    class BasicAuthInterceptor(val username: String, val password: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
                .newBuilder()
                .header("Authorization", Credentials.basic(username, password))
                .build()
            return chain.proceed(request)
        }
    }

    // Create an unsafe OkHttpClient (for development purposes)
    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder =
        OkHttpClient.Builder().apply {
            val trustManager = object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}

                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            }

            val sslContext = SSLContext.getInstance("SSL").apply {
                init(null, arrayOf(trustManager), java.security.SecureRandom())
            }

            sslSocketFactory(sslContext.socketFactory, trustManager)
            hostnameVerifier { hostname, _ -> hostname.contains("cleverapps.io") }
        }
}
