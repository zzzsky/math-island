package com.mathisland.app.feature.parent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.mathisland.app.ui.components.ActionButton
import com.mathisland.app.ui.components.StatusChip
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.StatusVariant
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
fun ParentSummaryTabletScreen(
    state: ParentSummaryUiState,
    onBackHome: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Md)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "家长学习摘要",
                style = TypographyTokens.ScreenTitle,
                fontWeight = FontWeight.Black
            )
            ActionButton(
                text = "完成",
                onClick = onBackHome,
                role = ActionRole.Completed,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Xs)) {
            ParentSummaryCard(
                modifier = Modifier.weight(1f),
                title = "今日学习",
                value = state.todayLearnedText
            )
            ParentSummaryCard(
                modifier = Modifier.weight(1f),
                title = "薄弱知识点",
                value = state.weakTopicsText
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Xs)) {
            ParentSummaryCard(
                modifier = Modifier.weight(1f),
                title = "连续学习",
                value = state.streakText
            )
            ParentSummaryCard(
                modifier = Modifier.weight(1f),
                title = "建议优先复习",
                value = state.recommendedIslandText
            )
        }
    }
}

@Composable
private fun ParentSummaryCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xCC173C4C)),
        shape = RadiusTokens.CardMd
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.Xl),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.Xs)
        ) {
            StatusChip(text = title, variant = StatusVariant.Neutral)
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
