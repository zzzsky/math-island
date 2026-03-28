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

enum class MapFeedbackKind {
    NewIsland,
    Chest,
    Replay,
    Progress
}

data class MapFeedbackUiState(
    val kind: MapFeedbackKind = MapFeedbackKind.Progress,
    val title: String,
    val body: String,
    val highlightedIslandId: String? = null,
    val starsEarned: Int = 0,
    val chestReady: Boolean = false,
    val summaryTitle: String = title,
    val summaryBody: String = body,
    val summaryLabel: String = "回到地图",
    val detailLabel: String = "回地图后",
    val detailTitle: String = summaryTitle,
    val detailBody: String = summaryBody
)

@Composable
fun MapProgressFeedback(
    feedback: MapFeedbackUiState,
    motionProgress: Float = 0f
) {
    val accent = when (feedback.kind) {
        MapFeedbackKind.NewIsland -> Color(0xFFF2D48B)
        MapFeedbackKind.Chest -> Color(0xFFE8B86D)
        MapFeedbackKind.Replay -> Color(0xFF7FC2D8)
        MapFeedbackKind.Progress -> MaterialTheme.colorScheme.primary
    }
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
                tint = accent.copy(alpha = 0.92f + (motionProgress * 0.08f))
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
                        color = accent,
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

@Composable
fun MapReturnSummaryCard(
    feedback: MapFeedbackUiState,
    modifier: Modifier = Modifier
) {
    val accent = when (feedback.kind) {
        MapFeedbackKind.NewIsland -> Color(0xFFF2D48B)
        MapFeedbackKind.Chest -> Color(0xFFE8B86D)
        MapFeedbackKind.Replay -> Color(0xFF7FC2D8)
        MapFeedbackKind.Progress -> MaterialTheme.colorScheme.primary
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("map-return-summary"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        com.mathisland.app.ui.components.SummarySpotlightCard(
            label = feedback.summaryLabel,
            title = feedback.summaryTitle,
            body = feedback.summaryBody,
            accent = accent
        )
        com.mathisland.app.ui.components.TabletInfoCard(
            title = feedback.detailLabel,
            subtitle = feedback.detailTitle,
            body = feedback.detailBody,
            accentColor = accent.copy(alpha = 0.8f),
            badgeText = feedback.summaryLabel,
            badgeVariant = when (feedback.kind) {
                MapFeedbackKind.NewIsland -> StatusVariant.Recommended
                MapFeedbackKind.Chest -> StatusVariant.Highlight
                MapFeedbackKind.Replay -> StatusVariant.Highlight
                MapFeedbackKind.Progress -> StatusVariant.Success
            },
            modifier = Modifier.testTag("map-return-detail-card")
        )
    }
}
