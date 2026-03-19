package com.mathisland.app.feature.level.renderers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.domain.model.Question
import com.mathisland.app.ui.components.TabletChipLabel
import com.mathisland.app.ui.theme.TabletDeepWater
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens

internal val TabletCoral = Color(0xFFEE964B)
internal val TabletSky = Color(0xFF8ECae6)
internal val TabletMint = Color(0xFF9ADBC7)

@Composable
internal fun RendererOptionsColumn(
    question: Question,
    rendererTag: String,
    accent: Color,
    header: String? = null,
    helper: String? = null,
    affordance: @Composable (() -> Unit)? = null,
    buttonLabel: String,
    onAnswer: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(rendererTag),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (header != null && helper != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0x662C647A)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TabletChipLabel(text = header)
                    Text(
                        text = helper,
                        style = TypographyTokens.Caption,
                        color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface)
                    )
                }
            }
        }
        affordance?.invoke()
        question.choices.forEach { choice ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xCC225267)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = choice,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Button(
                        modifier = Modifier.testTag("answer-$choice"),
                        onClick = { onAnswer(choice) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accent,
                            contentColor = TabletDeepWater
                        )
                    ) {
                        Text(buttonLabel)
                    }
                }
            }
        }
    }
}
