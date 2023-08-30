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



class PasswordManager(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "MyDiaryPrefs"
        private const val PASSWORD_KEY = "password"
        private const val APP_LOCKED_KEY = "app_locked"
        private const val COLOR_THEME_KEY = "color_theme"
        private const val FONT_THEME_KEY = "font_theme"
        private const val SECURITY_QUESTION_KEY = "answer"
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

    fun setQuestionAnswer(answer: String) {
        prefs.edit().putString(SECURITY_QUESTION_KEY,answer).apply()
    }

    private fun getQuestionAnswer(): String? {
        return prefs.getString(SECURITY_QUESTION_KEY, "")
    }

    fun verifyQuestionAnswer(answer: String): Boolean {
        return answer == getQuestionAnswer()
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
            FontFamily.SansSerif -> "SansSerif"
            FontFamily.Cursive -> "Cursive"
            FontFamily.Monospace -> "Monospace"
            FontFamily(Font(R.font.caveat)) -> "caveat"
            FontFamily(Font(R.font.conquest)) -> "conquest"
            FontFamily(Font(R.font.kaushan)) -> "kaushan"
            FontFamily(Font(R.font.lobster)) -> "lobster"
            FontFamily(Font(R.font.shadowsintolight)) -> "shadowsintolight"
            FontFamily(Font(R.font.newhearddecorative)) -> "newhearddecorative"
            FontFamily(Font(R.font.quietmeows)) -> "quietmeows"
            FontFamily(Font(R.font.arabia)) -> "arabia"
            FontFamily(Font(R.font.chasinghearts)) -> "chasinghearts"
            FontFamily(Font(R.font.royal)) -> "royal"
            FontFamily(Font(R.font.lovevale)) -> "lovevale"
            FontFamily(Font(R.font.santa)) -> "santa"
            FontFamily(Font(R.font.twirly)) -> "twirly"
            FontFamily(Font(R.font.helloween)) -> "helloween"
            FontFamily(Font(R.font.dj5)) -> "dj5"
            FontFamily(Font(R.font.condensedoblique)) ->  "condensedoblique"
            FontFamily(Font(R.font.dings)) -> "dings"
            FontFamily(Font(R.font.famousfaces)) -> "famousfaces"
            FontFamily(Font(R.font.first_writing)) -> "firstWriting"
            else  ->  "default"
        }
    }

    private fun getFontFamilyFromName(fontName: String?): FontFamily {
        return when (fontName) {
            "default" -> FontFamily.Default
            "serif" -> FontFamily.Serif
            "SansSerif" -> FontFamily.SansSerif
            "Cursive" -> FontFamily.Cursive
            "Monospace" -> FontFamily.Monospace
            "caveat" -> FontFamily(Font(R.font.caveat))
            "conquest" ->  FontFamily(Font(R.font.conquest))
            "kaushan" ->  FontFamily(Font(R.font.kaushan))
            "lobster" ->  FontFamily(Font(R.font.lobster))
            "shadowsintolight" ->   FontFamily(Font(R.font.shadowsintolight))
            "newhearddecorative" -> FontFamily(Font(R.font.newhearddecorative))
            "quietmeows" -> FontFamily(Font(R.font.quietmeows))
            "arabia" -> FontFamily(Font(R.font.arabia))
            "chasinghearts" -> FontFamily(Font(R.font.chasinghearts))
            "royal" ->  FontFamily(Font(R.font.royal))
            "lovevale" ->  FontFamily(Font(R.font.lovevale))
            "santa" -> FontFamily(Font(R.font.santa))
            "twirly" -> FontFamily(Font(R.font.twirly))
            "helloween" -> FontFamily(Font(R.font.helloween))
            "dj5"->  FontFamily(Font(R.font.dj5))
            "condensedoblique" ->  FontFamily(Font(R.font.condensedoblique))
            "dings" -> FontFamily(Font(R.font.dings))
            "famousfaces" -> FontFamily(Font(R.font.famousfaces))
            "firstWriting" ->  FontFamily(Font(R.font.first_writing))
            else -> FontFamily.Default
        }
    }
}
