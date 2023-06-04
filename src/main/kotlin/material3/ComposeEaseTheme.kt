package material3

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val appLightColorScheme = lightColorScheme(
    primary = Color(245, 245, 245),
    onPrimary = Color.Gray,
    secondary = Color(230, 230, 230)
)

private val appDarkColorScheme = darkColorScheme()

@Composable
fun ApplicationTheme(
    scheme: ColorScheme = if (isSystemInDarkTheme()) appDarkColorScheme else appLightColorScheme,
    content: @Composable () -> Unit,
) {

    MaterialTheme(
        colorScheme = scheme,
        // typography = JetchatTypography
    ) {
        val rippleIndication = rememberRipple()
        CompositionLocalProvider(
            LocalIndication provides rippleIndication,
            content = content
        )
    }
}
