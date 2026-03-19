package com.mathisland.app.feature.chest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.mathisland.app.ui.theme.ActionRole

@Composable
fun ChestTabletScreen(
    state: ChestUiState,
    onBackHome: () -> Unit,
    onOpenMap: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ActionButton(
                text = "返回首页",
                onClick = onBackHome,
                role = ActionRole.OutlinedSecondary,
            )
            ActionButton(
                text = "回到地图",
                modifier = Modifier.testTag("chest-open-map"),
                onClick = onOpenMap,
                role = ActionRole.Recommended,
            )
        }
        Text(
            text = "宝箱收藏",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black
        )
        Text(
            text = state.summaryText,
            style = MaterialTheme.typography.titleMedium
        )
        if (state.stickers.isEmpty()) {
            ChestInfoCard(
                title = "还没有贴纸",
                subtitle = "先去完成一整座主岛",
                body = "每清空一座岛，就会在这里点亮一张新的纪念贴纸。"
            )
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                state.stickers.forEach { sticker ->
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xCC1C4D5F)),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                                .padding(18.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            ChestChipLabel(text = "Island Sticker")
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = sticker,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "已收入宝箱",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChestInfoCard(
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
            ChestChipLabel(text = title)
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
private fun ChestChipLabel(text: String) {
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
