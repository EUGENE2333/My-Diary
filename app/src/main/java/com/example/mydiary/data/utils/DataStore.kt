package com.example.mydiary.data.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


//create an instance of Datastore<Preferences>.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")   //

class PreferencesManager @Inject constructor(context: Context) {

    private object PreferencesKeys {
        val ENABLED = booleanPreferencesKey("enabled")
        val isFormat = booleanPreferencesKey("format")
    }
    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun setEnabled(enabled: Boolean) {
        try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.ENABLED] = enabled
            }
        } catch (e: IOException) {
            // Handle exception
        }
    }

    suspend fun isNoteFormat(isNoteFormat: Boolean) {
        try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.isFormat] =isNoteFormat
            }
        } catch (e: IOException) {
            // Handle exception
        }
    }



    val enabledFlow: Flow<Boolean> = dataStore.data
        .catch { exception ->
            // Handle exception
            emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[PreferencesKeys.ENABLED] ?: false
        }

    val isFormatFlow: Flow<Boolean> = dataStore.data
        .catch { exception ->
            // Handle exception
            emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[PreferencesKeys.isFormat] ?: false
        }
}




/**
 * PreferencesKeys object: It defines a companion object within PreferencesManager that holds
 * the keys used for storing and retrieving values from the preferences.

dataStore property: It initializes a DataStore<Preferences> using the context.dataStore extension
property, which is an extension property added to the Context class using the preferencesDataStore
function provided by the DataStore library. This creates a DataStore instance with the name "settings"
for storing preferences.

setEnabled function: It is a suspend function that allows you to set the value of the "enabled"
preference. It uses the dataStore.edit function to modify the preferences atomically. Within the edit
block, the "enabled" value is updated based on the provided boolean parameter.

enabledFlow property: It is a Flow<Boolean> property that represents the flow of the "enabled"
preference value. It uses the dataStore.data property, which is a flow of the current preferences
stored in the DataStore. The flow emits the preferences whenever they change. The flow is also configured
to catch any exceptions that occur during emission and emit an empty set of preferences in case of
an error. The flow is then mapped to extract the "enabled" value from the preferences, defaulting to
false if the key doesn't exist.

Overall, this code sets up a preferences manager that allows you to store and retrieve the
"enabled" preference using the DataStore library. The enabledFlow property provides a convenient
way to observe changes to the "enabled" preference value as a Flow
 * */
