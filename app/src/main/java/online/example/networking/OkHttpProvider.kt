package online.example.networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object OkHttpProvider {
    val client: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
}
