package com.mathisland.app.feature.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.ui.components.StatusChip
import com.mathisland.app.ui.theme.StatusVariant
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens

data class MapFeedbackUiState(
    val title: String,
    val body: String,
    val highlightedIslandId: String? = null,
    val starsEarned: Int = 0,
    val chestReady: Boolean = false
)

@Composable
fun MapProgressFeedback(
    feedback: MapFeedbackUiState,
    motionProgress: Float = 0f
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                val pulseScale = 1f + (motionProgress * 0.012f)
                scaleX = pulseScale
                scaleY = pulseScale
                alpha = 0.94f + (motionProgress * 0.06f)
            }
            .testTag("map-progress-feedback"),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xCC215A6D).copy(alpha = 0.88f + (motionProgress * 0.12f))
        ),
        shape = RoundedCornerShape(22.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.92f + (motionProgress * 0.08f))
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = feedback.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("map-feedback-title")
                )
                Text(
                    text = feedback.body,
                    style = TypographyTokens.BodyPrimary,
                    color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.testTag("map-feedback-body")
                )
                if (feedback.starsEarned > 0 || feedback.chestReady) {
                    Text(
                        text = "本次推进",
                        style = TypographyTokens.SupportingLabel,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.testTag("map-feedback-summary")
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (feedback.starsEarned > 0) {
                        StatusChip(
                            text = "+${feedback.starsEarned} 星星",
                            modifier = Modifier.testTag("map-feedback-stars-pill"),
                            variant = StatusVariant.Recommended,
                            leadingIcon = Icons.Default.Star,
                        )
                    }
                    if (feedback.chestReady) {
                        StatusChip(
                            text = "宝箱有新收藏",
                            modifier = Modifier.testTag("map-feedback-chest-pill"),
                            variant = StatusVariant.Highlight,
                        )
                    }
                }
            }
        }
    }
}
