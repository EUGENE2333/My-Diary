package com.example.mydiary.presentation.compose.mainComposables2.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.repository.Resources
import com.example.mydiary.data.utils.Utils
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.animations.bouncingClickable
import com.example.mydiary.presentation.compose.mainComposables.DrawerContent
import com.google.firebase.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Home(
    homeViewModel: HomeViewModel?,
    viewModel: DiaryViewModel,
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
    val context = LocalContext.current

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
                        text = "Home",
                        color = Color.White,
                        fontFamily = selectedFontTheme
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
                        /*
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp)
                    ){

                      items(homeUiState.notesList.data ?: emptyList()){note->
                          NoteItem(
                              notes =note,
                              viewModel = viewModel,
                              onLongClick = {
                                  openDialog = true
                                  selectedNote = note
                              }) {
                                  onNoteClick.invoke(note.documentId)
                              }
                      }
                    }  */
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
                    fontFamily = selectedFontTheme
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














