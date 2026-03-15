package com.mathisland.app.feature.parent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
@Composable
fun ParentSummaryTabletScreen(
    state: ParentSummaryUiState,
    onBackHome: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "家长学习摘要",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black
            )
            Button(
                onClick = onBackHome,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("完成")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
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
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
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
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ParentChipLabel(text = title)
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
