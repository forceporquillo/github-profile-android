package dev.forcecodes.hov.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object LanguageColorModule {

    @Named("jsonColor")
    @Provides
    fun providesLanguageColorFromJson(@ApplicationContext context: Context): JSONObject {
        val json: String? = try {
            val `is`: InputStream = context.assets.open("github_languages.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            throw e
        }
        return JSONObject(json)
    }
}
