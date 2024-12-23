package com.example.mydiary.ui.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mydiary.ui.theme.Corn
import com.example.mydiary.ui.theme.NoRippleTheme

/**
 * Calorica Design Filled Button
 * - See [CaloricaOutlinedButton] for a medium-emphasis button with a border.
 * - See [CaloricaTextButton] for a low-emphasis button with no border.
 * @param onClick called when this button is clicked
 * @param modifier the [Modifier] to be applied to this button
 * @param tag optional tag to identify the component in ui tests
 * @param enabled controls the enabled state of this button. When `false`, this component will not
 * respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param shape defines the shape of this button's container.
 * @param colors [MyDiaryButtonColors] that will be used to resolve the colors for this button in different
 * states. See [CaloricaButtonDefaults.filledButtonColors].
 */
// We are currently Suppressing ReusedModifierInstance since this is a false positive that
// shouldn't be triggered when composables are wrapped by CompositionLocalProviders
@Composable
fun MyDiaryFilledButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    tag: String = text,
    enabled: Boolean = true,
    shape: Shape = CaloricaButtonDefaults.filledButtonShape,
    colors: MyDiaryButtonColors = CaloricaButtonDefaults.filledButtonColors(),
    elevation: ButtonElevation? = CaloricaButtonDefaults.buttonElevation,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = CaloricaButtonDefaults.contentPadding(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    trailingIcon: @Composable (() -> Unit)? = null
) {
    CompositionLocalProvider(
        LocalRippleTheme provides NoRippleTheme
    ) {
        Button(
            onClick = onClick,
            modifier = modifier
                .sizeIn(
                    minHeight = CaloricaButtonDefaults.MinButtonHeight,
                    minWidth = CaloricaButtonDefaults.MinButtonWidth
                )
                .testTag(tag),
            enabled = enabled,
            elevation = elevation,
            shape = shape,
            border = border,
            colors = colors.toFilledButtonColors(isPressed = interactionSource.collectIsPressedAsState().value),
            contentPadding = contentPadding,
            interactionSource = interactionSource
        ) {
                Text(text = text)
                if (trailingIcon != null) {
                    Spacer(Modifier.width(8.dp))
                    trailingIcon()
                }
        }
    }
}

/**
 * Calorica custom button styles, as per Figma. We override some of the material design default values in order to have
 * parity with iOS. Since the Material Design lib is no longer available for iOS apps
 */
object CaloricaButtonDefaults {

    private val ButtonHorizontalPadding = 24.dp
    private val ButtonVerticalPadding = 8.dp
    private val TextButtonHorizontalPadding = 12.dp

    val MinButtonHeight = 56.dp

    val MinButtonWidth = 80.dp

    val buttonElevation: ButtonElevation
        @Composable get() = ButtonDefaults.buttonElevation()

    val filledButtonShape: Shape
        @Composable get() = ButtonDefaults.shape

    @Composable
    fun contentPadding(
        start: Dp = ButtonHorizontalPadding,
        top: Dp = ButtonVerticalPadding,
        end: Dp = ButtonHorizontalPadding,
        bottom: Dp = ButtonVerticalPadding
    ) = PaddingValues(
        start = start,
        top = top,
        end = end,
        bottom = bottom
    )

    @Composable
    fun filledButtonColors(
        containerColor: Color = MaterialTheme.colorScheme.primary,
        contentColor: Color = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor: Color = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 6f),
        disabledContentColor: Color = MaterialTheme.colorScheme.outlineVariant,
        highlightColor: Color = Corn
    ) = MyDiaryButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
        highlightColor = highlightColor
    )
}

/**
 * Class that applies pressed states to buttons
 */
class MyDiaryButtonColors internal constructor(
    private val containerColor: Color,
    private val contentColor: Color,
    private val disabledContainerColor: Color,
    private val disabledContentColor: Color,
    private val highlightColor: Color
) {
    /**
     * Represents the container color for this button, depending on [enabled].
     *
     * @param enabled whether the button is enabled
     */

    @Composable
    private fun toButtonColors(
        containerColor: Color = this.containerColor,
        contentColor: Color = this.contentColor,
        disabledContainerColor: Color = this.disabledContainerColor,
        disabledContentColor: Color = this.disabledContentColor
    ) = ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor
    )

    @Composable
    fun toFilledButtonColors(isPressed: Boolean): ButtonColors = toButtonColors(
        containerColor = if (isPressed) {
            highlightColor
        } else {
            containerColor
        }
    )
}
