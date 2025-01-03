package com.example.mydiary.data.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.mydiary.R
import javax.inject.Inject


class PasswordManager @Inject constructor(
    private val context: Context
){

    companion object {
        private const val PREFS_NAME = "MyDiaryPrefs"
        private const val PASSWORD_KEY = "password"
        private const val APP_LOCKED_KEY = "app_locked"
        private const val COLOR_THEME_KEY = "color_theme"
        private const val FONT_THEME_KEY = "font_theme"
        private const val SECURITY_QUESTION_KEY = "question"
        private const val SECURITY_ANSWER_KEY = "answer"
        private const val USER_ACCOUNT = "password"
    }

    private val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun setPassword(password: String) {

        prefs.edit().putString(PASSWORD_KEY, password).apply()
    }

    fun isPasswordSet(): Boolean {
        return prefs.contains(PASSWORD_KEY)
    }

    fun verifyPassword(password: String): Boolean {
        return password == getPassword()
    }

    fun getPassword(): String? {
        return prefs.getString(PASSWORD_KEY, "")
    }
    fun isAppLocked(): Boolean {
        return prefs.getBoolean(APP_LOCKED_KEY, false)
    }
    fun lockApp() {
        prefs.edit().putBoolean(APP_LOCKED_KEY, true).apply()
    }
    fun unlockApp(password: String): Boolean {
        if (verifyPassword(password)) {
            prefs.edit().putBoolean(APP_LOCKED_KEY, false).apply()
            return true
        }
        return false
    }
    fun setQuestionAnswer(question: String, answer: String) {
        prefs.edit()
            .putString(SECURITY_QUESTION_KEY, question)
            .putString(SECURITY_ANSWER_KEY, answer)
            .apply()
    }
    fun getQuestion(): String? {
        return prefs.getString(SECURITY_QUESTION_KEY, "")
    }
     fun getAnswer(): String? {
        return prefs.getString(SECURITY_ANSWER_KEY, "")
    }

    fun verifyAnswer(answer: String): Boolean {
        return answer == getAnswer()
    }
    fun isQuestionAnswerSet(): Boolean {
        return prefs.contains(SECURITY_QUESTION_KEY)
    }

    fun setUserAccount(userEmail: String) {
        prefs.edit().putString(USER_ACCOUNT, userEmail).apply()
    }
    fun getUserAccount(): String? {
        return prefs.getString(USER_ACCOUNT, "")
    }
    fun setColorTheme(color: Color) {
        prefs.edit().putInt(COLOR_THEME_KEY, color.toArgb()).apply()
    }
    fun getColorTheme(): Color {
        val colorInt = prefs.getInt(COLOR_THEME_KEY, Color(0xFF1E6D65).toArgb())
        return Color(colorInt)
    }
    fun setFontTheme(font: FontFamily) {
        val fontName = getFontNameFromFamily(font)
        prefs.edit().putString(FONT_THEME_KEY, fontName).apply()
    }

    fun getFontTheme(): FontFamily {
        val fontName = prefs.getString(FONT_THEME_KEY, "default")
        return getFontFamilyFromName(fontName)
    }
    // Helper function to convert FontFamily to font name string
   private  fun getFontNameFromFamily(font: FontFamily): String {
        return when (font) {
            FontFamily.Default -> "default"
            FontFamily.Serif -> "serif"
            FontFamily.Cursive -> "Cursive"
            FontFamily.Monospace -> "Monospace"
            FontFamily(Font(R.font.kaushan)) -> "kaushan"
            FontFamily(Font(R.font.lobster)) -> "lobster"
            FontFamily(Font(R.font.shadowsintolight)) -> "shadowsintolight"
            FontFamily(Font(R.font.quietmeows)) -> "quietmeows"
            FontFamily(Font(R.font.royal)) -> "royal"
            FontFamily(Font(R.font.santa)) -> "santa"
            FontFamily(Font(R.font.dj5)) -> "dj5"
            FontFamily(Font(R.font.condensedoblique)) ->  "condensedoblique"
            FontFamily(Font(R.font.first_writing)) -> "firstWriting"
            FontFamily(Font(R.font.summer_belynda)) -> "summerbelynda"
            FontFamily(Font(R.font.wortl_r)) -> "wortlr"
            FontFamily(Font(R.font.lifesavers_regular)) -> "lifesavers_regular"
            FontFamily(Font(R.font.pizzat)) -> "pizzat"
            FontFamily(Font(R.font.slimshoot)) -> "slimshoot"
            FontFamily(Font(R.font.gnyrwn977)) -> "gnyrwn977"
            else  ->  "default"
        }
    }

    private fun getFontFamilyFromName(fontName: String?): FontFamily {
        return when (fontName) {
            "default" -> FontFamily.Default
            "serif" -> FontFamily.Serif
            "Cursive" -> FontFamily.Cursive
            "Monospace" -> FontFamily.Monospace
            "kaushan" ->  FontFamily(Font(R.font.kaushan))
            "lobster" ->  FontFamily(Font(R.font.lobster))
            "shadowsintolight" ->   FontFamily(Font(R.font.shadowsintolight))
            "quietmeows" -> FontFamily(Font(R.font.quietmeows))
            "royal" ->  FontFamily(Font(R.font.royal))
            "santa" -> FontFamily(Font(R.font.santa))
            "dj5"->  FontFamily(Font(R.font.dj5))
            "condensedoblique" ->  FontFamily(Font(R.font.condensedoblique))
            "firstWriting" ->  FontFamily(Font(R.font.first_writing))
            "summerbelynda" ->   FontFamily(Font(R.font.summer_belynda))
            "wortlr" -> FontFamily(Font(R.font.wortl_r))
            "lifesavers_regular" ->  FontFamily(Font(R.font.lifesavers_regular))
            "pizzat" ->  FontFamily(Font(R.font.pizzat))
            "slimshoot" -> FontFamily(Font(R.font.slimshoot))
            "gnyrwn977" ->  FontFamily(Font(R.font.gnyrwn977))
            else -> FontFamily.Default
        }
    }
}
