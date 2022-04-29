package dev.forcecodes.hov.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.forcecodes.hov.data.prefs.DataStorePreferenceStorage.PreferencesKeys.PREF_SELECTED_THEME
import dev.forcecodes.hov.data.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface PreferenceStorage {
    suspend fun selectTheme(theme: String)
    val selectedTheme: Flow<String>
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStorePreferenceStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferenceStorage {

    object PreferencesKeys {
        val PREF_SELECTED_THEME = stringPreferencesKey("pref_dark_mode")
    }

    override suspend fun selectTheme(theme: String) {
        dataStore.edit {
            it[PREF_SELECTED_THEME] = theme
        }
    }

    override val selectedTheme =
        dataStore.data.map { it[PREF_SELECTED_THEME] ?: Theme.SYSTEM.storageKey }
}