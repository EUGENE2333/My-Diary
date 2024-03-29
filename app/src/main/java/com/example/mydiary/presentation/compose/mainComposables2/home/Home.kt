package com.example.mydiary.presentation.compose.mainComposables2.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mydiary.R
import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.repository.Resources
import com.example.mydiary.data.utils.Utils
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.animations.bouncingClickable
import com.example.mydiary.presentation.compose.mainComposables.drawerContent.DrawerContent
import com.example.mydiary.presentation.compose.mainComposables.fontSizeBasedOnFontTheme
import com.example.mydiary.presentation.compose.mainComposables.headerFontSizeBasedOnFontTheme
import com.google.firebase.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Home(
    homeViewModel: HomeViewModel = hiltViewModel(),
    viewModel: DiaryViewModel = hiltViewModel(),
    navController: NavController,
    onNoteClick:(String)-> Unit,
    navToNewEntryScreen:()-> Unit,
    navToLoginPage:() -> Unit
) {
    val homeUiState = homeViewModel?.homeUiState ?: HomeUiState()
    var openDialog by remember { mutableStateOf(false) }
    var selectedNote: Notes? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val selectedColorTheme = viewModel.passwordManager.getColorTheme()
    val selectedFontTheme = viewModel.passwordManager.getFontTheme()
    val items = homeUiState.notesList.data ?: emptyList()
    val coroutineScope = rememberCoroutineScope()
    val notesText = remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit) {
        homeViewModel?.loadNotes()
    }

    LaunchedEffect(key1 = homeViewModel?.hasUser) {
        if (homeViewModel?.hasUser == false) {
            navToLoginPage.invoke()
        }
    }

    // Define a function to open the drawer
    fun openDrawer() {
        scope.launch {
            scaffoldState.drawerState.open()
        }
    }

    // Handle back button press when the drawer is open
    BackHandler(enabled = scaffoldState.drawerState.isOpen) {
        scope.launch {
            scaffoldState.drawerState.close()
        }
    }


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { openDrawer() }) {
                        Icon(
                            Icons.Filled.Menu,
                            contentDescription = "Open Drawer",
                            tint = Color.White
                        )
                    }
                },
                backgroundColor = Color(0xFF2C2428),

                title = {
                    Text(
                        text = "Write Note",
                        color = Color.White,
                        fontFamily = selectedFontTheme,
                        fontSize = headerFontSizeBasedOnFontTheme(selectedFontTheme)
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navToNewEntryScreen.invoke()
                },
                backgroundColor = selectedColorTheme,
                contentColor = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Add Entry")
            }
        },
        backgroundColor = Color(0xFF2C2428),

        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {

                when (homeUiState.notesList) {

                    is Resources.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(align = Alignment.Center)
                        )
                    }
                    is Resources.Success -> {

                        if(homeUiState.notesList.data!!.isEmpty()){
                            Image(
                                painter = painterResource(id = R.drawable.brown_vintage_journal_notebook),
                                contentDescription = "journal image",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }

                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Adaptive(150.dp),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(10.dp)
                        ) {
                            items(items) { note ->
                                NoteItem(
                                    notes = note,
                                    viewModel = viewModel,
                                    onLongClick = {
                                        selectedNote = note
                                        openDialog = true
                                    }) {
                                    onNoteClick.invoke(note.documentId)
                                }

                            }

                        }
                    }
                    else -> {
                        Text(
                            text = homeUiState.notesList.throwable?.localizedMessage
                                ?: "Unknown error",
                            color = Color.Red
                        )
                    }
                }

                if (openDialog) {
                    AlertDialog(
                        onDismissRequest = { /* Handle dialog dismiss if needed */ navController.popBackStack() },
                        title = {
                            Text(
                                text = "Delete",
                            )
                        },
                        text = {
                            Text(
                                text = "Delete selected Note?",

                                )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    // Navigate to the feedback screen
                                    homeViewModel?.viewModelScope?.launch(Dispatchers.Main) {
                                        selectedNote?.let { homeViewModel.deleteNote(it.documentId) }
                                        if (homeUiState.noteDeletedStatus) {
                                            scaffoldState.snackbarHostState.showSnackbar("Deleted successfully")
                                            openDialog = false
                                        } else {
                                            scaffoldState.snackbarHostState.showSnackbar("Error Deleting Note")
                                            openDialog = false
                                        }


                                    }
                                }
                            ) {
                                Text(text = "Yes")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { openDialog = false }
                            ) {
                                Text(text = "No")
                            }
                        }
                    )
                }
            }
        },
        drawerContent = { DrawerContent(navController = navController, viewModel = viewModel) },
        drawerBackgroundColor = Color.White,
        drawerContentColor = Color.Black,
        drawerShape = MaterialTheme.shapes.small,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerScrimColor = Color.Transparent,
// Pass the drawerState to the Scaffold
    )

}


@Composable
fun NoteItem(
notes: Notes,
viewModel: DiaryViewModel,
onLongClick:()-> Unit,
onClick: () -> Unit
) {

    val selectedFontTheme = viewModel.passwordManager.getFontTheme()

    Card(
        modifier = Modifier
            .bouncingClickable(
                onLongClick = { onLongClick.invoke() },
                onClick = { onClick.invoke() }
            )
            .padding(8.dp)
            .fillMaxWidth(),
        backgroundColor = Utils.colors[notes.colorIndex],
        elevation = 5.dp
    ) {

        Column {
            Text(
                text = notes.title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                modifier = Modifier.padding(4.dp),
                fontFamily = selectedFontTheme

            )
            Spacer(modifier = Modifier.size(4.dp))
            CompositionLocalProvider(
                LocalContentAlpha provides ContentAlpha.disabled
            ) {
                Text(
                    text = notes.description,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(4.dp),
                    maxLines = 4,
                    fontSize = fontSizeBasedOnFontTheme(selectedFontTheme),
                )
            }

            Spacer(modifier = Modifier.size(4.dp))

            CompositionLocalProvider(
                LocalContentAlpha provides ContentAlpha.disabled
            ) {
                Text(
                    text = formatDate(notes.timestamp),
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.End),
                    maxLines = 1
                )
            }


        }
    }
}


private fun formatDate(timestamp: Timestamp): String {
    val simpleDateFormat = SimpleDateFormat("MM-dd-yy hh:mm", Locale.getDefault())
    return simpleDateFormat.format(timestamp.toDate())
}
