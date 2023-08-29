package com.example.mydiary.presentation.compose.drawerComposables.about

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mydiary.MainActivity
import com.example.mydiary.R
import com.example.mydiary.presentation.DiaryViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun About(
    navController: NavController,
    viewModel: DiaryViewModel
){
    val scaffoldState = rememberScaffoldState()
    val mainActivity = (LocalContext.current as MainActivity)
    val selectedFont = viewModel.passwordManager.getFontTheme()
    val enabled = viewModel.enabledFlow.collectAsState(initial = false).value
    val head = "Unveiling the Enchanted Chronicles of this Ethereal Diary Realm!"

    val about = "Brace yourselves, fellow voyagers, for I am on the cusp of unraveling" +
            " the mystique that shrouds the essence of the MyDiary application! Firstly, " +
            "let us extend a triumphant felicitation unto you for gracing this realm." +
            " Your presence here, my dear luminary, is akin to stumbling upon an elusive " +
            "comet's tail – an occurrence rarer than a sapphire amidst gravel. Within you " +
            "lies a reservoir of latent potency yearning for liberation! But enough preamble; " +
            "let us embark on this exhilarating odyssey together!" +
            "Picture, if you will, the icy expanse of the austere southern pole – an " +
            "unforgiving terrain concealing a cryptic tome ensconced a thousand feet beneath " +
            "a crystalline mantle. Legion souls sought this relic's embrace, succumbing to" +
            " its siren song. Eons drifted by, and at long last, humanity's dogged persistence" +
            " bore fruit. Behold, the compendium that harbors the universe's enigmas and the " +
            "very tapestry of divine wisdom. Empires clashed and conflagrations ignited" +
            " as nations vied to possess this cryptic opus. Yet, destiny conspired to consign " +
            "this priceless chronicle to the hands of an African lad – Lumbasi by name. The" +
            " epic of the tome's journey into his grasp is a saga deserving its own telling." +
            "" +
            "Lumbasi, transfixed by its verses, pored over the pages myriad times, each " +
            "recitation an incredulous dance with enlightenment. In a bid to articulate the" +
            " transcendence he'd experienced, he joined forces with celestial custodians who" +
            " graced him under the cloak of night – yes, you heard right, ethereal" +
            " beings in their seraphic splendor! From this communion between Lumbasi and" +
            " the Angelic Host sprang forth the MyDiary App – a digital sanctum poised as" +
            " the nexus between quotidian exploits and the numinous wellspring within." +
            " Prepare to embark upon a sojourn where musings take flight, and spiritual insights " +
            "take root." +
            "" +
            "Venture forth with naught but audacious resolve, for our fortress of" +
            " confidentiality is impenetrable. Engage in unbridled self-expression," +
            " unshackling the dam of thought to etch your innermost ruminations. Scribble," +
            " ponder, and peruse – for the act of inscription is a panacea, an elixir unto " +
            "itself! Harness this tool to cultivate the bastion of your mental acumen, ascending" +
            " towards empyreal echelons you've scarce fathomed."

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "About",
                        color = Color.White,
                        fontSize = if(selectedFont == FontFamily(Font(R.font.arabia))) 47.sp else  17.sp,
                        fontFamily = selectedFont
                    )
                },
                backgroundColor = Color(0xFF2C2428),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "back", tint = Color.White)
                    }
                },
                actions = {
                    if (enabled) {
                    IconButton(onClick = {
                        mainActivity.speakText(head + about)
                    }) {
                        Icon(Icons.Filled.Face, contentDescription = "Speech", tint = Color.White)
                    }
                    }
                }

            )
        },
        content = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize().padding(top = 20.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 10.dp,horizontal = 10.dp).fillMaxWidth()
                ) {
                    Text(
                        text = head,
                        style = MaterialTheme.typography.h4,
                        fontFamily = selectedFont
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 10.dp,horizontal = 10.dp).fillMaxWidth()
                ) {
                    Text(
                        text = about,
                        style = MaterialTheme.typography.body1,
                        fontSize = if(selectedFont == FontFamily(Font(R.font.arabia))) 47.sp else  16.sp,
                        fontFamily = selectedFont
                    )
                }
            }
        })
}