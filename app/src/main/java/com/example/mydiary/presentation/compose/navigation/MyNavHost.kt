package com.example.mydiary.presentation.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.drawerComposables.ReccomendApp.RecommendApp
import com.example.mydiary.presentation.compose.drawerComposables.about.About
import com.example.mydiary.presentation.compose.drawerComposables.colorAndStyle.ColorAndStyle
import com.example.mydiary.presentation.compose.drawerComposables.exportEntries.ExportScreen
import com.example.mydiary.presentation.compose.drawerComposables.exportEntries.ExportViewModel
import com.example.mydiary.presentation.compose.drawerComposables.layout.Layout
import com.example.mydiary.presentation.compose.drawerComposables.lockScreen.*
import com.example.mydiary.presentation.compose.drawerComposables.rateAndReview.RateAndReview
import com.example.mydiary.presentation.compose.drawerComposables.reminder.Reminder
import com.example.mydiary.presentation.compose.drawerComposables.userProfile.SignInPage
import com.example.mydiary.presentation.compose.drawerComposables.userProfile.SignUpPage
import com.example.mydiary.presentation.compose.drawerComposables.userProfile.UserProfile
import com.example.mydiary.presentation.compose.mainComposables.DiaryDetailScreen
import com.example.mydiary.presentation.compose.mainComposables.DiaryListScreen
import com.example.mydiary.presentation.compose.mainComposables.NewDiaryEntryScreen
import com.example.mydiary.presentation.compose.mainComposables.SplashScreen
import com.example.mydiary.presentation.compose.mainComposables2.NewEntry
import com.example.mydiary.presentation.compose.mainComposables2.detail.DetailScreen2
import com.example.mydiary.presentation.compose.mainComposables2.detail.DetailViewModel
import com.example.mydiary.presentation.compose.mainComposables2.home.Home
import com.example.mydiary.presentation.compose.mainComposables2.home.HomeViewModel



@Composable
fun MyNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: DiaryViewModel,
    homeViewModel: HomeViewModel,
    detailViewModel: DetailViewModel,
    exportViewModel: ExportViewModel
) {

    val passwordManager = viewModel.passwordManager
    val isNoteFormat by viewModel.isNoteFormat.collectAsState(initial = false)

    var startDestination = Screen.SplashScreen.route


    NavHost(navController = navController, startDestination = startDestination) {

        composable(route = Screen.SplashScreen.route){

            SplashScreen (
                viewModel = viewModel
                    ){
                if(!viewModel.hasUser ){
                    viewModel.passwordManager.setPassword("null")
                    navController.navigate( Screen.SignInPage.route){
                        launchSingleTop = true
                        popUpTo(0){
                            inclusive = true
                        }
                    }
                }
                    else if(viewModel.passwordManager.getPassword() != "null"){
                        navController.navigate(Screen.LockScreenScreen.route){
                            launchSingleTop = true
                            popUpTo(0){
                                inclusive = true
                            }

                        }
                    }
                //    else if (isPasswordSet){ Screen.LockScreenScreen.route }

                else if(isNoteFormat){
                    navController.navigate( Screen.Home.route){
                        launchSingleTop = true
                        popUpTo(0){
                            inclusive = true
                        }
                    }


                } else{
                    navController.navigate( Screen.DiaryList.route){
                        launchSingleTop = true
                        popUpTo(0){
                            inclusive = true
                        }
                    }

                }

            }
        }

        composable(route = Screen.DiaryList.route) {
            DiaryListScreen(
                navController = navController,
                viewModel = viewModel,
                homeViewModel = homeViewModel,
                onNoteClick = { noteId ->
                    navController.navigate(Screen.DiaryDetail.route + "?userid=$noteId")
                },

                navToLoginPage = {
                    navController.navigate(Screen.SignInPage.route){
                        launchSingleTop = true
                        popUpTo(0){
                            inclusive = true
                        }
                    }
                },
                onNavigateToNewDiaryEntryScreen = {
                    navController.navigate(Screen.NewDiaryEntry.route)

                }
            )
        }


        composable(route = Screen.NewDiaryEntry.route) {
            NewDiaryEntryScreen(
                navController = navController,
                viewModel = viewModel,
                detailViewModel = detailViewModel,
                onNavigate = { navController.navigate(Screen.DiaryList.route)}
            )

        }

        composable(
         //   route = Screen.DiaryDetail.route,
            route = Screen.DiaryDetail.route + "?userid={userid}",
            arguments = listOf(navArgument("userid") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {entry->

            DiaryDetailScreen(
                navController = navController,
                viewModel = viewModel ,
                detailViewModel = detailViewModel,
                noteId = entry.arguments?.getString("userid") as String) {
                 navController.navigate(Screen.DiaryList.route)
            }
        }


        composable(route = Screen.LockScreen.route) {

            LockScreen(
                navController = navController,
                viewModel = viewModel,
                onPasswordSet = {
                    passwordManager.setPassword(it).toString()
                },
            )
        }
        composable(route = Screen.SecurityQuestions.route){
            SecurityQuestions(
                navController = navController,
                viewModel = viewModel,
                onQuestionSet ={
                    passwordManager.setQuestionAnswer(it).toString()
                }
            )
        }
        composable(route = Screen.ForgotPassword.route){
            ForgotPassword (navController = navController,viewModel = viewModel)
        }


        composable(route = Screen.LockScreenScreen.route) {

            LockScreenScreen(
                viewModel = viewModel,
                navController = navController,
            )
        }

            composable(route = Screen.ColorAndStyle.route) {
                ColorAndStyle(
                    viewModel = viewModel,
                    navController = navController
                )
        }
        composable(route = Screen.UserProfile.route) {
            UserProfile(
                navController = navController,
                viewModel = viewModel,
                homeViewModel = homeViewModel,
                navToSignUpPage = {
                    navController.navigate(Screen.SignInPage.route){
                        launchSingleTop = true
                        popUpTo(0){
                            inclusive = true
                        }
                    }

                }
            ){
                navController.navigate(Screen.SignInPage.route){
                    launchSingleTop = true
                    popUpTo(0){
                        inclusive = true
                    }
                }
            }
    }

        composable(route = Screen.SignUpPage.route) {
           SignUpPage(navController = navController, viewModel = viewModel, onNavigateToDiaryList = {
               navController.navigate(Screen.DiaryList.route) {
                   launchSingleTop = true
                   popUpTo(route = Screen.SignInPage.route) {
                       inclusive = true
                   }
               }
           })
        }

        composable(route = Screen.SignInPage.route) {
           SignInPage(
               navController = navController,
               viewModel = viewModel,
               onNavigateToNoteHomeScreen = { navController.navigate(Screen.Home.route){
                   launchSingleTop = true
                   popUpTo(0){
                       inclusive = true
                   }
               } }
           ){
               navController.navigate(Screen.DiaryList.route){
                   launchSingleTop = true
                   popUpTo(0){
                       inclusive = true
                   }
               }
           }
        }

        composable(route = Screen.RateAndReview.route) {
            RateAndReview(navController = navController, viewModel = viewModel)
        }

        composable(route = Screen.Reminder.route) {
            Reminder(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.Recommend.route) {
            RecommendApp(navController = navController)
        }

        composable(route = Screen.About.route) {
            About(navController = navController,viewModel = viewModel)
        }

        composable(route = Screen.Export.route) {
                 ExportScreen(navController = navController, viewModel = viewModel, exportViewModel = exportViewModel )
        }

        composable(route = Screen.Layout.route) {
            Layout(
                navController = navController,
                viewModel = viewModel,
                onNavigateToNoteHomeScreen = {
                    navController.navigate(Screen.Home.route){
                        launchSingleTop = true
                        popUpTo(0){
                            inclusive = true
                        }
                    }
                }
            ) {
                navController.navigate(Screen.DiaryList.route){
                    launchSingleTop = true
                    popUpTo(0){
                        inclusive = true
                    }
                }
            }
        }

        composable(route = Screen.Home.route) {
            Home(homeViewModel = homeViewModel,
                viewModel  = viewModel,
                navController = navController,
                onNoteClick = { noteId ->
                    navController.navigate(Screen.DiaryDetail2.route + "?id=$noteId")
                },
               navToNewEntryScreen = {
                   navController.navigate(Screen.NewEntry.route)
               },
                navToLoginPage = {
                     navController.navigate(Screen.SignInPage.route){
                         launchSingleTop = true
                         popUpTo(0){
                             inclusive = true
                         }

                     }
                },
            )
        }

        composable(
            route = Screen.DiaryDetail2.route + "?id={id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { entry->
            DetailScreen2(
                detailViewModel = detailViewModel,
                noteId = entry.arguments?.getString("id") as String
            ) {
                navController.navigateUp()
            }

        }
        composable(route = Screen.NewEntry.route) {
            NewEntry(detailViewModel = detailViewModel, viewModel = viewModel) {
                navController.navigateUp()
            }
        }

        composable(route = Screen.ChangePassword.route) {

           ChangePassword(
                viewModel = viewModel,
                navController = navController,
            )
        }
    }
}
