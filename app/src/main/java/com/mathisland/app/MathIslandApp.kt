package com.mathisland.app
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ocean = Color(0xFF0F4C5C)
private val DeepWater = Color(0xFF114B5F)
private val Foam = Color(0xFFF3FAF8)
private val Sand = Color(0xFFF4D58D)
private val Coral = Color(0xFFEE964B)
private val Seaweed = Color(0xFF4B6F44)
private val Sky = Color(0xFF8ECae6)
private val Mint = Color(0xFF9ADBC7)

@Composable
fun MathIslandTheme(content: @Composable () -> Unit) {
    val colors = darkColorScheme(
        primary = Sand,
        secondary = Coral,
        tertiary = Seaweed,
        background = Ocean,
        surface = DeepWater,
        onPrimary = DeepWater,
        onSecondary = Color.White,
        onBackground = Foam,
        onSurface = Foam
    )

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}

@Composable
fun MathIslandApp(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surface,
                            Color(0xFF173B4F)
                        )
                    )
                )
                .padding(horizontal = 28.dp, vertical = 20.dp)
        ) {
            content()
        }
    }
}

@Preview(widthDp = 1280, heightDp = 800)
@Composable
private fun MathIslandAppPreview() {
    MathIslandTheme {
        MathIslandApp {
            PreviewPlaceholderScene()
        }
    }
}

@Composable
private fun PreviewPlaceholderScene() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = "Math Island Scene Host",
            color = Foam,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
