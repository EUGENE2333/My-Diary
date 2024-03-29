package com.example.mydiary.presentation.compose.mainComposables


import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mydiary.R
import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.repository.Resources
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.mainComposables.drawerContent.DrawerContent
import com.example.mydiary.presentation.compose.mainComposables2.home.HomeUiState
import com.example.mydiary.presentation.compose.mainComposables2.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun DiaryListScreen(
    navController: NavController,
    viewModel: DiaryViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    onNoteClick:(String)-> Unit,
    navToLoginPage:() -> Unit,
    onNavigateToNewDiaryEntryScreen: () -> Unit
  ) {


    // Get a ScaffoldState object to control the drawer
    val scaffoldState = rememberScaffoldState()

    // Define a coroutine scope
    val coroutineScope = rememberCoroutineScope()
    var openDialog by remember { mutableStateOf(false) }
    val homeUiState = homeViewModel?.homeUiState ?: HomeUiState()
    var selectedNote: Notes? by remember { mutableStateOf(null) }
   val selectedColorTheme = viewModel.passwordManager.getColorTheme()
    val selectedFontTheme = viewModel.passwordManager.getFontTheme()


    // Define a function to open the drawer
    fun openDrawer() {
        coroutineScope.launch {
            scaffoldState.drawerState.open()
        }
    }

    LaunchedEffect(key1 = Unit){
        homeViewModel?.loadNotes()
    }


    // Handle back button press when the drawer is open
     BackHandler(enabled = scaffoldState.drawerState.isOpen) {
         coroutineScope.launch {
             scaffoldState.drawerState.close()
         }
       }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "My Diary",
                    color = Color.White,
                    fontSize = headerFontSizeBasedOnFontTheme(selectedFontTheme),
                    fontFamily = selectedFontTheme
                )
            },
                backgroundColor = Color(0xFF2C2428),
                navigationIcon = {
                    IconButton(onClick = { openDrawer() }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Open Drawer",tint = Color.White)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNavigateToNewDiaryEntryScreen()
                },
                backgroundColor = selectedColorTheme,
                contentColor = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Entry")
            }
        },
        backgroundColor = selectedColorTheme,

        content = {

            Box(  modifier = Modifier
                .fillMaxSize(),
                
               
            ) {

                when(homeUiState.notesList){
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
                        LazyColumn(
                            contentPadding = PaddingValues(3.dp),
                        
                        ){

                            items(homeUiState.notesList.data){ note ->
                                DiaryEntryItem(
                                    notes = note ,
                                    viewModel = viewModel,
                                    onSwipeToDelete = {
                                        selectedNote = note
                                        openDialog = true
                                    },
                                    onLongClick = {
                                        selectedNote = note
                                        openDialog = true
                                    }
                                ){
                                    onNoteClick.invoke(note.documentId)
                                }

                            }
                        }

                    }

                    else -> {
                        Text(
                            text = homeUiState.notesList.throwable?.localizedMessage ?: "Unknown error",
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
                                            openDialog = false
                                            scaffoldState.snackbarHostState.showSnackbar("Deleted successfully")

                                        } else {
                                            openDialog = false
                                            scaffoldState.snackbarHostState.showSnackbar("Error Deleting Note")

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

            LaunchedEffect(key1 = homeViewModel?.hasUser){
                if(homeViewModel?.hasUser == false){
                    navToLoginPage.invoke()
                }
            }

        },    floatingActionButtonPosition = FabPosition.End,


        drawerContent = { DrawerContent(navController = navController, viewModel = viewModel) },
        drawerBackgroundColor = Color.White,
        drawerContentColor = Color.Black,
        drawerShape = MaterialTheme.shapes.small,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerScrimColor = Color.Transparent,
        // Pass the drawerState to the Scaffold

    )
}







