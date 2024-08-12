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
import dev.forcecodes.gitprofile.data.api.ConnectivityInterceptor
import dev.forcecodes.gitprofile.data.api.GithubApiService
import dev.forcecodes.gitprofile.data.api.NetworkStatusProvider
import dev.forcecodes.gitprofile.data.api.interceptors.CacheInterceptor
import dev.forcecodes.gitprofile.data.api.interceptors.HeaderInterceptor
import dev.forcecodes.gitprofile.data.api.interceptors.NextPageInterceptor
import dev.forcecodes.gitprofile.data.extensions.checkMainThread
import dev.forcecodes.gitprofile.data.extensions.delegatingCallFactory
import dev.forcecodes.gitprofile.data.internal.GithubApi
import dev.forcecodes.gitprofile.data.internal.InternalApi
import dev.forcecodes.gitprofile.data.utils.ResponseNextPageLookup
import okhttp3.Cache
import okhttp3.CacheControl
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

    @InternalApi
    @Provides
    internal fun providesMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @InternalApi
    @Provides
    internal fun provideConnectivityInterceptor(
        @InternalApi networkStatusProvider: NetworkStatusProvider
    ): ConnectivityInterceptor = ConnectivityInterceptor(networkStatusProvider)

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
        @InternalApi nextPageInterceptor: NextPageInterceptor,
        @InternalApi cacheInterceptor: CacheInterceptor,
        @InternalApi headerInterceptor: HeaderInterceptor,
        @InternalApi httpLoggingInterceptor: HttpLoggingInterceptor,
        @InternalApi connectivityInterceptor: ConnectivityInterceptor
    ): OkHttpClient = checkMainThread {
        OkHttpClient.Builder().apply {
            connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addNetworkInterceptor(cacheInterceptor)
            cache(cache)

            if (BuildConfig.DEBUG) {
                addInterceptor(httpLoggingInterceptor)
            }

            addInterceptor(nextPageInterceptor)
            addInterceptor(connectivityInterceptor)
            addInterceptor(headerInterceptor)

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
    internal fun providesNextPageInterceptor(
        @InternalApi nextPageLookup: ResponseNextPageLookup
    ): NextPageInterceptor = NextPageInterceptor(nextPageLookup)

    @InternalApi
    @Singleton
    @Provides
    internal fun providesCacheInterceptor(
        @InternalApi cacheControl: CacheControl
    ): CacheInterceptor = CacheInterceptor(cacheControl)

    @InternalApi
    @Singleton
    @Provides
    internal fun providesHeaderInterceptor(): HeaderInterceptor =
        HeaderInterceptor(API_VERSION_HEADER)

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
