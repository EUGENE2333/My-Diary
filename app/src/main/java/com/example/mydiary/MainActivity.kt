package com.example.mydiary


import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.drawerComposables.exportEntries.ExportViewModel
import com.example.mydiary.presentation.compose.mainComposables2.detail.DetailViewModel
import com.example.mydiary.presentation.compose.mainComposables2.home.HomeViewModel
import com.example.mydiary.presentation.compose.navigation.MyNavHost
import com.example.mydiary.ui.theme.MyDiaryTheme


class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    private lateinit var diaryViewModel: DiaryViewModel
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var exportViewModel: ExportViewModel

    private val application: MyDiaryApplication by lazy {
        applicationContext as MyDiaryApplication
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        diaryViewModel = ViewModelProvider(this, application.diaryViewModelFactory).get(DiaryViewModel::class.java)

        homeViewModel = ViewModelProvider(this,).get(HomeViewModel::class.java)

        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

       exportViewModel = ViewModelProvider(this,).get(ExportViewModel:: class.java)

        // Initialize the TextToSpeech engine
        textToSpeech = TextToSpeech(this, this)


        setContent {
                MyDiaryTheme(viewModel = diaryViewModel) {

                    MyNavHost(
                        viewModel = diaryViewModel,
                        homeViewModel = homeViewModel,
                        detailViewModel = detailViewModel,
                        exportViewModel = exportViewModel
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