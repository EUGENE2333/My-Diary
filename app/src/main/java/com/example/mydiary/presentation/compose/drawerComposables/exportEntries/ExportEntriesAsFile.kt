package com.example.mydiary.presentation.compose.drawerComposables.exportEntries

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.Stable
import androidx.core.content.FileProvider
import com.example.mydiary.data.model.Notes
import java.io.File
import java.io.FileWriter

@Stable
fun exportTxTFile(notes:List<Notes>? ,context: Context ) {

    val notesText = buildString {
        notes?.forEach { note ->
            append("Title: ${note.title}\nDescription: ${note.description}\n\n")
        }
    }

    val file = createTxtFile("notes.txt", notesText)

    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.type = "text/plain"
    shareIntent.putExtra(
        Intent.EXTRA_STREAM,
        FileProvider.getUriForFile(context, context.packageName + ".provider", file)
    )
    shareIntent.putExtra(Intent.EXTRA_TEXT, notesText)

    val chooserIntent = Intent.createChooser(shareIntent, "Share Notes")

    context.startActivity(chooserIntent)
}

private fun createTxtFile(fileName: String, content: String): File {
    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fileName)
    try {
        val fileWriter = FileWriter(file)
        fileWriter.use {
            it.write(content)
        }
    } catch (e: Exception) {
        Log.e("createTxtFile", "Error creating .TXT file: ${e.message}")
    }
    return file
}
