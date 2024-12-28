package com.example.mydiary.presentation.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.example.mydiary.presentation.compose.drawerComposables.layout.Layout
import com.example.mydiary.presentation.compose.drawerComposables.lockScreen.ChangePassword
import com.example.mydiary.presentation.compose.drawerComposables.lockScreen.ForgotPassword
import com.example.mydiary.presentation.compose.drawerComposables.lockScreen.LockScreen
import com.example.mydiary.presentation.compose.drawerComposables.lockScreen.LockScreenScreen
import com.example.mydiary.presentation.compose.drawerComposables.lockScreen.SecurityQuestions
import com.example.mydiary.presentation.compose.drawerComposables.rateAndReview.RateAndReview
import com.example.mydiary.presentation.compose.drawerComposables.reminder.Reminder
import com.example.mydiary.presentation.compose.drawerComposables.userProfile.UserProfile
import com.example.mydiary.presentation.compose.mainComposables.DiaryDetailScreen
import com.example.mydiary.presentation.compose.mainComposables.DiaryListScreen
import com.example.mydiary.presentation.compose.mainComposables.NewDiaryEntryScreen
import com.example.mydiary.presentation.compose.mainComposables.SplashScreen
import com.example.mydiary.presentation.compose.mainComposables.subscription.paywall.SubscriptionScreen
import com.example.mydiary.presentation.compose.mainComposables2.NewEntry
import com.example.mydiary.presentation.compose.mainComposables2.detail.DetailScreen2
import com.example.mydiary.presentation.compose.mainComposables2.home.Home
import com.example.mydiary.presentation.onboarding.LoginOrSignUpViewModel
import com.example.mydiary.presentation.onboarding.SignInPage
import com.example.mydiary.presentation.onboarding.SignUpPage

@Composable
fun MyNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: DiaryViewModel = hiltViewModel(),
    onBoardingViewModel: LoginOrSignUpViewModel = hiltViewModel()
) {

    val passwordManager = viewModel.passwordManager
    val isNoteFormat by viewModel.isNoteFormat.collectAsState(initial = false)

    var startDestination = Screen.SplashScreen.route


    NavHost(navController = navController, startDestination = startDestination) {

        composable(route = Screen.SplashScreen.route){

            SplashScreen{
                if(!viewModel.hasUser ){
                    viewModel.passwordManager.setPassword("null")
                    passwordManager.setQuestionAnswer("","")
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
                noteId = entry.arguments?.getString("userid") as String) {
                 navController.navigate(Screen.DiaryList.route)
            }
        }


        composable(route = Screen.LockScreen.route) {

            LockScreen(
                navController = navController,
                onPasswordSet = {
                    passwordManager.setPassword(it).toString()
                },
            )
        }
        composable(route = Screen.SecurityQuestions.route){
            SecurityQuestions(
                navController = navController,
            )
        }
        composable(route = Screen.ForgotPassword.route){
            ForgotPassword (navController = navController)
        }


        composable(route = Screen.LockScreenScreen.route) {

            LockScreenScreen(
                navController = navController,
            )
        }

            composable(route = Screen.ColorAndStyle.route) {
                ColorAndStyle(
                    navController = navController
                )
        }
        composable(route = Screen.UserProfile.route) {
            UserProfile(
                navController = navController,
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
           SignUpPage(navController = navController, viewModel = onBoardingViewModel, onNavigateToDiaryList = {
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
            RateAndReview(navController = navController)
        }

        composable(route = Screen.Reminder.route) {
            Reminder(navController = navController)
        }
        composable(route = Screen.Recommend.route) {
            RecommendApp(navController = navController)
        }

        composable(route = Screen.About.route) {
            About(navController = navController)
        }

        composable(route = Screen.Export.route) {
                 ExportScreen(navController = navController)
        }

        composable(route = Screen.Subscription.route){
            SubscriptionScreen(onNavigate = {})
        }

        composable(route = Screen.Layout.route) {
            Layout(
                navController = navController,
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
            Home(
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
                noteId = entry.arguments?.getString("id") as String
            ) {
                navController.navigateUp()
            }

        }
        composable(route = Screen.NewEntry.route) {
            NewEntry{
                navController.navigateUp()
            }
        }

        composable(route = Screen.ChangePassword.route) {

           ChangePassword(
                navController = navController,
            )
        }
    }
}
