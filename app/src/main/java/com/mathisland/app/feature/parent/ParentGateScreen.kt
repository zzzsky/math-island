package com.mathisland.app.feature.parent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ParentGateScreen(
    onAnswer: (String) -> Unit,
    onBackHome: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(0.56f),
            colors = CardDefaults.cardColors(containerColor = Color(0xEE173C4C)),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ParentChipLabel(text = "家长入口")
                Text(
                    text = "请先完成一道口算",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                ParentInfoCard(
                    title = "验证题",
                    subtitle = "8 + 7 = ?",
                    body = "用于确认由家长进入摘要页。"
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    listOf("14", "15", "16").forEach { answer ->
                        Button(
                            modifier = Modifier.testTag("parent-answer-$answer"),
                            onClick = { onAnswer(answer) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color(0xFF114B5F)
                            )
                        ) {
                            Text(answer)
                        }
                    }
                }
                Button(
                    onClick = onBackHome,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF40697A))
                ) {
                    Text("返回首页")
                }
            }
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
            ParentChipLabel(text = title)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = body,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f)
            )
        }
    }
}

@Composable
internal fun ParentChipLabel(text: String) {
    Box(
        modifier = Modifier
            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.88f),
            style = MaterialTheme.typography.labelLarge
        )
    }
}
