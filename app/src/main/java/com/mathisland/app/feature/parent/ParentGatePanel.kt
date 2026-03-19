package com.mathisland.app.feature.parent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.mathisland.app.ui.components.ActionButton
import com.mathisland.app.ui.components.StatusChip
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.StatusVariant
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
fun ParentGatePanel(
    state: ParentGateUiState,
    onAnswer: (String) -> Unit,
    onBackHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xEE173C4C)),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatusChip(text = state.chipLabel, variant = StatusVariant.Recommended)
            Text(
                text = state.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            ParentInfoCard(
                title = "验证题",
                subtitle = state.question,
                body = state.subtitle
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                state.answers.forEach { answer ->
                    ActionButton(
                        text = answer,
                        modifier = Modifier.testTag("parent-answer-$answer"),
                        onClick = { onAnswer(answer) },
                        role = ActionRole.Primary,
                    )
                }
            }
            ActionButton(
                text = "返回首页",
                onClick = onBackHome,
                role = ActionRole.OutlinedSecondary,
            )
        }
    }
}

@Composable
private fun ParentInfoCard(
    title: String,
    subtitle: String,
    body: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0x80244C5E)),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatusChip(text = title, variant = StatusVariant.Neutral)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = body,
                style = TypographyTokens.Caption,
                color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface)
            )
        }
    }
}
