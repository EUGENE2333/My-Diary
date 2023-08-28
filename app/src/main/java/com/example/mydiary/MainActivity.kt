package com.example.mydiary


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.example.mydiary.data.utils.ReminderForegroundService
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.mainComposables2.detail.DetailViewModel
import com.example.mydiary.presentation.compose.mainComposables2.home.HomeViewModel
import com.example.mydiary.presentation.compose.navigation.MyNavHost
import com.example.mydiary.ui.theme.MyDiaryTheme



class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    private lateinit var diaryViewModel: DiaryViewModel
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var detailViewModel: DetailViewModel

    private val application: MyDiaryApplication by lazy {
        applicationContext as MyDiaryApplication
    }

   // @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        diaryViewModel = ViewModelProvider(
            this,
            application.diaryViewModelFactory
        ).get(DiaryViewModel::class.java)

        homeViewModel = ViewModelProvider(
            this,
        ).get(HomeViewModel::class.java)

        detailViewModel = ViewModelProvider(
            this,
        ).get(DetailViewModel::class.java)

        // Initialize the TextToSpeech engine
        textToSpeech = TextToSpeech(this, this)


        setContent {
                MyDiaryTheme(viewModel = diaryViewModel) {

                    MyNavHost(
                        viewModel = diaryViewModel,
                        homeViewModel = homeViewModel,
                        detailViewModel = detailViewModel,
                        application = application
                    )
                }


            }


        // ensures that the content view is adjusted accordingly when the keyboard is shown

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)){ view, insets ->
               val bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom // accessing the IME insets and retrieving the bottom value,
            view.updatePadding(bottom = bottom)
            insets
        }
    }
    fun createForegroundService(title: String, message: String,imeDelayInSeconds: Long ) {

        val intent = Intent(this, ReminderForegroundService::class.java)
        intent.putExtra("title", "Reminder")
        intent.putExtra("message", "Reminder notification set!!")
        ContextCompat.startForegroundService(this, intent)

        Handler(Looper.getMainLooper()).postDelayed({

            val serviceIntent = Intent(this, ReminderForegroundService::class.java)
            serviceIntent.putExtra("title", title)
            serviceIntent.putExtra("message", message)

            ContextCompat.startForegroundService(this, serviceIntent)
        }, imeDelayInSeconds * 1000)
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
    //    passwordManager.lockApp()
        // Shutdown the TextToSpeech engine
        textToSpeech.shutdown()
    }
}
























