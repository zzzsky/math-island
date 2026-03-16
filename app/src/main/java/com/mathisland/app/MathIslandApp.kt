package com.mathisland.app
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mathisland.app.ui.theme.TabletFoam
import com.mathisland.app.ui.theme.TabletOcean
import com.mathisland.app.ui.theme.TabletPanelSurface

@Composable
fun MathIslandTheme(content: @Composable () -> Unit) {
    com.mathisland.app.ui.theme.MathIslandTheme(content = content)
}

@Composable
fun MathIslandApp(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = androidx.compose.material3.MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            androidx.compose.material3.MaterialTheme.colorScheme.background,
                            androidx.compose.material3.MaterialTheme.colorScheme.surface,
                            TabletPanelSurface
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
            color = TabletFoam,
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )
    }
}
