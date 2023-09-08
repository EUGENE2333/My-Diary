package com.example.mydiary.presentation.compose.drawerComposables.exportEntries

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.mydiary.presentation.compose.mainComposables2.home.HomeUiState
import com.example.mydiary.presentation.compose.mainComposables2.home.HomeViewModel
import java.io.File


fun createSampleTxtFile(context: Context, homeViewModel: HomeViewModel?): File {
    val homeUiState = homeViewModel?.homeUiState ?: HomeUiState()
    val notesText = buildString {
        homeUiState.notesList.data?.forEach { note ->
            append("Title: ${note.title}\nDescription: ${note.description}\n\n")
        }
    }
    val fileName = "sample_notes.txt"
    val fileContents = notesText
    val file = File(context.getExternalFilesDir(null), fileName)

    try {
        file.writeText(fileContents) // Write the text to the file directly
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return file
}


fun exportUserNotesAsText(context: Context, file: File, onComplete: (Boolean) -> Unit) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val fileUri =
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri)

        val chooserIntent = Intent.createChooser(shareIntent, "Export Notes as TXT")
        chooserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(chooserIntent)

        onComplete(true)
    }