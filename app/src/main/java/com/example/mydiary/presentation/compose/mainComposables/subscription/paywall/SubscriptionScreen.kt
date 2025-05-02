package com.example.mydiary.presentation.compose.mainComposables.subscription.paywall

//import androidx.lifecycle.compose.collectAsStateWithLifecycle
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.mydiary.R
import com.example.mydiary.ui.components.MyDiaryCircularProgressIndicator
import com.example.mydiary.ui.theme.LocalSpacing
import com.example.mydiary.ui.theme.bodyLarge
import com.example.mydiary.ui.theme.bodyMedium
import com.example.mydiary.ui.theme.bodySmall
import com.example.mydiary.ui.theme.crownColor
import com.example.mydiary.ui.theme.headlineExtraLarge
import com.example.mydiary.ui.theme.titleLarge


@SuppressLint("RememberReturnType")
@Composable
fun SubscriptionScreen(
    onNavigateToSuccess: () -> Unit = {},
    viewModel: SubscriptionViewModel = hiltViewModel(),
    onNavigateToPolicy: () -> Unit = {},
    onNavigate: () -> Unit,
    navController: NavController
) {
    // Collect state as state flow to ensure recomposition when it changes
    val uiState by viewModel.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    is SubscriptionUiEvent.SubscriptionPurchased -> onNavigateToSuccess()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        SubscriptionContent(
            state = uiState,
            onPlanSelect = viewModel::onSubscriptionPlanSelected,
            onRestorePurchase = viewModel::onRestorePurchase,
            onSubscriptionPurchase = onNavigateToSuccess,
            navController = navController,
            onNavigateToPolicy = onNavigateToPolicy,
            modifier = Modifier.fillMaxSize()
        )
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable(enabled = false) { },
                contentAlignment = Alignment.Center
            ) {
                MyDiaryCircularProgressIndicator()
            }
        }
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun SubscriptionContent(
    state: PricingPlanUiState,
    onRestorePurchase: () -> Unit,
    onPlanSelect: (PurchaseSubscriptionInput) -> Unit,
    onSubscriptionPurchase: () -> Unit,
    onNavigateToPolicy: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black/*Color(0xFFA53E97)*/)
            .padding(horizontal = LocalSpacing.current.medium)
    ) {
        Spacer(modifier = Modifier.height(LocalSpacing.current.large))
         Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    modifier = Modifier
                        .clickable { onRestorePurchase() },
                    color = MaterialTheme.colorScheme.onPrimary,
                    text = stringResource(id = R.string.restore_purchase),
                    style = bodyLarge,
                )

            }
            Spacer(modifier = Modifier.height(LocalSpacing.current.large))
            Spacer(modifier = Modifier.height(LocalSpacing.current.large))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.small),
                text = stringResource(id = R.string.pricing_plan_screen_title),
                maxLines = 2,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                style = headlineExtraLarge
            )
            Text(
                text = stringResource(id = R.string.pricing_plan_screen_subtitle),
                textAlign = TextAlign.Center,
                style = bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Spacer(modifier = Modifier.height(LocalSpacing.current.extraLarge))

                Offers()
            Spacer(modifier = Modifier.height(LocalSpacing.current.extraLarge))

        // Check for null values before rendering PricingPlans
           if (state.monthlyPlanUi != null && state.yearlyPlanUi != null) {
          //  state.monthlyPlanUi ?: return
         //   state.yearlyPlanUi ?: return

                    PricingPlans(
                        monthlyPlan = state.monthlyPlanUi,
                        yearlyPlan = state.yearlyPlanUi,
                        onPlanSelect = onPlanSelect
                    )
        }


            Spacer(modifier = Modifier.height(LocalSpacing.current.extraLarge))
            Button(
                onClick = {
                    val selectedPlan = if (state.yearlyPlanUi?.selected == true) {
                        state.yearlyPlanUi
                    } else {
                        state.monthlyPlanUi
                    }
                    selectedPlan?.let {
                         //  onSubscriptionPurchase(PurchaseSubscriptionInput(context, selectedPlan))
                        onSubscriptionPurchase()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = LocalSpacing.current.small),
                colors = ButtonDefaults.buttonColors(containerColor = crownColor),
                enabled = state.monthlyPlanUi != null && state.yearlyPlanUi != null
            ){
                Text(
                text = stringResource(id = R.string.subscribe_button),
                 color = MaterialTheme.colorScheme.onPrimary,
                 style = titleLarge
                )
            }
            Spacer(modifier = Modifier.height(LocalSpacing.current.large))
              Text(
                     modifier = Modifier
                         .fillMaxWidth(),
                  color = MaterialTheme.colorScheme.onPrimary,
                     text = "By subscribing, you agree to our",
                     style = bodyMedium,
                     textAlign = TextAlign.Center
                 )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToPolicy.invoke() },
                    color = crownColor,
                    text = stringResource(id = R.string.terms_of_service),
                    style = bodyMedium,
                    textAlign = TextAlign.Center
                )

    }
}

@Composable
fun PricingPlans(
    monthlyPlan: SubscriptionPlanUi,
    yearlyPlan: SubscriptionPlanUi,
    onPlanSelect: (PurchaseSubscriptionInput) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        SubscriptionItemView(
            title = monthlyPlan.title.toString(context),
            price = monthlyPlan.price.toString(context),
            cadence = monthlyPlan.cadence.toString(context),
            selected = monthlyPlan.selected,
            highlight = true,
            onClick = { onPlanSelect(PurchaseSubscriptionInput(context, monthlyPlan)) }
        )
        Spacer(modifier = Modifier.width(LocalSpacing.current.large))

            SubscriptionItemView(
                title = yearlyPlan.title.toString(context),
                price = yearlyPlan.price.toString(context),
                modifier = Modifier.padding(horizontal = 12.dp),
                cadence = yearlyPlan.cadence.toString(context),
                selected = yearlyPlan.selected,
                highlight = true,
                onClick = { onPlanSelect(PurchaseSubscriptionInput(context, yearlyPlan)) }
            )
      //  }

        Spacer(modifier = Modifier.width(LocalSpacing.current.large))
    }
}

@Composable
private fun Offers(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LocalSpacing.current.extraExtraLarge),
        verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.medium),
    ) {
        PricingPlanOfferItem(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = R.string.pricing_plan_offer_no_ads))
                }
            }
        )
        PricingPlanOfferItem(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = R.string.pricing_plan_offer_access_fonts))
                }
            }
        )
        PricingPlanOfferItem(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = R.string.pricing_plan_offer_priority_on_support))
                }
            }
        )
        PricingPlanOfferItem(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = R.string.pricing_plan_offer_add_images))
                }
            }
        )
    }
}

@Composable
private fun PricingPlanOfferItem(text: AnnotatedString) {
    Row {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_crown),
            contentDescription = null,
            tint = crownColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(LocalSpacing.current.medium))
        Text(
            text = text,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onPrimary,
            style = bodyLarge
        )
    }
}

@Composable
private fun BestValueLabel() {
    Box(
        modifier = Modifier
            .wrapContentWidth(Alignment.CenterHorizontally)
            .heightIn(min = LocalSpacing.current.extraLarge)
            .background(
                color = crownColor,
                shape = MaterialTheme.shapes.medium
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(horizontal = LocalSpacing.current.medium),
            text = stringResource(id = R.string.pricing_plan_best_value),
            style = bodySmall,
            color = Color.White
        )
    }
}