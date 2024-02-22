package com.example.mydiary


import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.compose.rememberNavController
import com.example.mydiary.presentation.compose.navigation.MyNavHost
import com.example.mydiary.ui.theme.MyDiaryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the TextToSpeech engine
        textToSpeech = TextToSpeech(this, this)

        setContent {
                MyDiaryTheme{
                    val navController = rememberNavController()
                    MyNavHost(navController = navController)
                }


            }


        // ensures that the content view is adjusted accordingly when the keyboard is shown

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)){ view, insets ->
               val bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom // accessing the IME insets and retrieving the bottom value,
            view.updatePadding(bottom = bottom)
            insets
        }
    }


    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // TextToSpeech engine is initialized successfully
            // You can use textToSpeech object to perform text-to-speech operations
        } else {
            // TextToSpeech initialization failed
            // Handle the error scenario if needed
        }
    }



     fun speakText(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onStop() {
        super.onStop()
        // Shutdown the TextToSpeech engine
        textToSpeech.shutdown()
    }
}