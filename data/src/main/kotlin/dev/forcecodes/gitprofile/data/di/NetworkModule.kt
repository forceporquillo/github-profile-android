package dev.forcecodes.gitprofile.data.di

import android.content.Context
import androidx.annotation.WorkerThread
import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.forcecodes.gitprofile.data.BuildConfig
import dev.forcecodes.gitprofile.data.api.GithubApiService
import dev.forcecodes.gitprofile.data.extensions.checkMainThread
import dev.forcecodes.gitprofile.data.extensions.containsNextPage
import dev.forcecodes.gitprofile.data.extensions.delegatingCallFactory
import dev.forcecodes.gitprofile.data.internal.GithubApi
import dev.forcecodes.gitprofile.data.internal.InternalApi
import dev.forcecodes.gitprofile.data.utils.ResponseNextPageLookup
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val DEFAULT_TIMEOUT           = 10L // 10 seconds
    private const val CACHE_SIZE                = 50 * 1024 * 1024 // 50 MiB
    private const val API_VERSION_HEADER        = "application/vnd.github.v3+json"
    private const val BASE_URL                  = "https://api.github.com"

    private val credentialsOrNull: String? get() =
        if (BuildConfig.TOKEN.isNotEmpty() || BuildConfig.USERNAME.isNotEmpty()) {
            Credentials.basic(BuildConfig.USERNAME, BuildConfig.TOKEN)
        } else null

    @InternalApi
    @Provides
    internal fun providesMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @InternalApi
    @Provides
    internal fun providesRetrofit(
        @InternalApi moshi: Moshi,
        @InternalApi okHttpClient: Lazy<OkHttpClient>
    ): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            addConverterFactory(MoshiConverterFactory.create(moshi))
            delegatingCallFactory(okHttpClient)
        }.build()
    }

    @InternalApi
    @Provides
    internal fun providesOkHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.HEADERS }
    }

    @InternalApi
    @Singleton
    @Provides
    @WorkerThread
    internal fun providesOkHttpClient(
        @InternalApi cache: Cache,
        @InternalApi cacheInterceptor: Interceptor,
        @InternalApi interceptor: HttpLoggingInterceptor
    ): OkHttpClient = checkMainThread {
        OkHttpClient.Builder().apply {
            connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addNetworkInterceptor(cacheInterceptor)
            cache(cache)

            if (BuildConfig.DEBUG) {
                addInterceptor(interceptor)
            }

            addInterceptor(Interceptor { chain ->
                val builder = chain.request().newBuilder()
                    .addHeader("Accept", API_VERSION_HEADER)
                // [Optional] add token here
                credentialsOrNull?.let { credentials ->
                    builder.addHeader("Authorization", credentials)
                }

                val request = builder.build()
                chain.proceed(request)
            })

        }.build()
    }

    @InternalApi
    @Provides
    internal fun provideCache(@ApplicationContext context: Context): Cache {
        return checkMainThread { Cache(context.cacheDir, CACHE_SIZE.toLong()) }
    }

    @InternalApi
    @Singleton
    @Provides
    fun providesCacheInterceptor(
        @InternalApi responseNextPageLookup: ResponseNextPageLookup,
        @InternalApi cacheControl: CacheControl
    ): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())

            if (response.containsNextPage) {
                responseNextPageLookup.nextIndexFromHeader(response.headers)
            }

            // cache
            response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", cacheControl.toString())
                .build()
        }
    }

    @Provides
    @InternalApi
    @Singleton
    fun providesCacheControl(): CacheControl {
        return CacheControl.Builder()
            .maxAge(2, TimeUnit.MINUTES)
            .onlyIfCached()
            .build()
    }

    @GithubApi
    @Singleton
    @Provides
    internal fun providesGithubApiService(
        @InternalApi retrofit: Retrofit
    ): GithubApiService = retrofit.create()
}

