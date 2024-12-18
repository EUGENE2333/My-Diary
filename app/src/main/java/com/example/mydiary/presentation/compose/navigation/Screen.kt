package com.example.mydiary.presentation.compose.navigation

sealed class Screen(val route: String){
    object  SplashScreen: Screen("splash_screen")
    object DiaryList: Screen("diary_list_screen")
    object NewDiaryEntry: Screen("new_diary_entry")
    object ColorAndStyle: Screen("color_style_screen")
    object LockScreen : Screen("lock_screen")
    object LockScreenScreen: Screen("lock_screen_screen")
    object SecurityQuestions: Screen("security_question")
    object ForgotPassword: Screen("forgot_password")
    object  ChangePassword: Screen("change_password")
    object RateAndReview: Screen("rate_and_review")
    object Feedbackk: Screen("feedback")
    object UserProfile: Screen("user_profile")
    object  SignUpPage: Screen("sign_up_page")
    object  SignInPage: Screen("sign_in_page")
    object  Reminder: Screen("reminder_page")
    object Recommend: Screen("recommend_page")
    object  About: Screen("about_page")
    object  Layout: Screen("layout_page")
    object  Export: Screen("export_page")
    object  Subscription: Screen("subscription_screen")

    object DiaryDetail: Screen("diary_detail_screen/{userid}")

    // Screen2
    object Home: Screen("home_page")
    object DiaryDetail2: Screen("diary_detail_screen2/{id}")
    object NewEntry: Screen("new_entry")
}

/*
  {
  fun createRoute(id: Int): String{
             return "diary_detail_screen/$id"
     }
   }*/
