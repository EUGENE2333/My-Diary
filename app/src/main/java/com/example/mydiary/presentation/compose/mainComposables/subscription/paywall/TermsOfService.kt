package com.example.mydiary.presentation.compose.mainComposables.subscription.paywall

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TermsOfserviceAndPolicy(
    navController: NavController,

){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                Text(
                    text = "Terms of service & Privacy Policy",
                    color = Color.White,
                )
            },
                backgroundColor = Color(0xFF2C2428),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "back", tint = Color.White)
                    }
                },
    ) },

    ){ paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,

            ){

            item{
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp).fillMaxWidth()
                ) {
                    Text(
                        text =buildAnnotatedString {
                            // Title
                            pushStyle(SpanStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
                            append("Terms of Service\n\n")
                            pop()

                            // Section 1
                            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            append("1. Usage:\n")
                            pop()
                            append("You may use MyDiary to privately record your thoughts, experiences, and memories. You are responsible for the content you create and store.\n\n")

                            // Section 2
                            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            append("2. Subscription:\n")
                            pop()
                            append("Some features may require a paid subscription. Subscription details, including pricing and renewal terms, will be clearly displayed before purchase.\n\n")

                            // Section 3
                            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            append("3. Account Security:\n")
                            pop()
                            append("If your account requires login, you are responsible for keeping your credentials secure.\n\n")

                            // Section 4
                            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            append("4. Content Ownership:\n")
                            pop()
                            append("You own your diary entries. We do not claim any ownership over the content you create or upload.\n\n")

                            // Section 5
                            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            append("5. Service Changes:\n")
                            pop()
                            append("We may update, add, or remove features over time to improve your experience. We will try to notify you of major changes.\n\n")

                            // Section 6
                            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            append("6. Termination:\n")
                            pop()
                            append("You can stop using MyDiary at any time. We reserve the right to suspend or terminate access if you violate these terms.\n\n")

                            // Section 7
                            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            append("7. Disclaimer:\n")
                            pop()
                            append("MyDiary is provided “as is.” We are not responsible for any data loss or service interruptions.\n\n")

                            // Space
                            append("\n")

                            // Title 2
                            pushStyle(SpanStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
                            append("Privacy Policy\n\n")
                            pop()

                            // Privacy Sections
                            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            append("1. Data Collection:\n")
                            pop()
                            append("We collect only the minimum information necessary to provide and improve the service, such as anonymized usage data and billing info.\n\n")

                            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            append("2. Diary Entries:\n")
                            pop()
                            append("Your entries are private and stored securely. We do not access, read, or share your personal entries unless required by law.\n\n")

                            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            append("3. Third-Party Services:\n")
                            pop()
                            append("We may use trusted services (e.g., for payments or analytics) who are also required to protect your data.\n\n")

                            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            append("4. Your Rights:\n")
                            pop()
                            append("You can delete your account and all associated data at any time in your profile section.\n\n")

                            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            append("5. Data Security:\n")
                            pop()
                            append("We take reasonable technical measures to protect your data from unauthorized access or loss. No one apart from you, not even us,can read your entries.\n\n")

                            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            append("6. Changes to This Policy:\n")
                            pop()
                            append("We may update this policy from time to time. We will notify you if significant changes are made.\n\n")

                            // Contact Us
                            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            append("Contact Us:\n")
                            pop()
                            append("If you have any questions about these Terms or our Privacy Policy, please contact us at myDiaryAngels@gmail.com.")
                        },
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 10.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.body1,
                        fontStyle = FontStyle.Normal
                    )
                }
            }
        }
    }
}
