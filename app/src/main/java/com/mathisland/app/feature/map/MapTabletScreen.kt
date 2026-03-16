package com.mathisland.app.feature.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.feature.common.TabletChipLabel

@Composable
fun MapTabletScreen(
    state: MapTabletUiState,
    onBackHome: () -> Unit,
    onOpenChest: () -> Unit,
    onStartLesson: (String) -> Unit
) {
    var selectedIslandId by remember(state.recommendedIslandId, state.islands) {
        mutableStateOf(state.recommendedIslandId)
    }
    val selectedIsland = state.islands.firstOrNull { island -> island.id == selectedIslandId }
        ?: state.islands.firstOrNull()

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = onBackHome) {
                    Text("返回首页")
                }
                Button(
                    modifier = Modifier.testTag("map-open-chest"),
                    onClick = onOpenChest,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("打开宝箱")
                }
                Text(
                    text = "总星星 ${state.totalStars}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            MapSceneCanvas(
                islands = state.islands,
                selectedIslandId = selectedIsland?.id,
                onSelectIsland = { islandId -> selectedIslandId = islandId }
            )

            selectedIsland?.let { island ->
                Text(
                    text = "当前查看 ${island.title}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("map-selected-title")
                )
            }

            LazyColumn(
                modifier = Modifier.testTag("map-islands-list"),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(state.islands) { island ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedIslandId = island.id }
                            .testTag("select-island-${island.id}"),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                island.id == selectedIsland?.id -> Color(0xFF215A6D)
                                island.unlocked -> Color(0xCC18475A)
                                else -> Color(0x6618475A)
                            }
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = island.title,
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = island.subtitle,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f)
                                    )
                                }
                                TabletChipLabel(
                                    text = when {
                                        island.id == selectedIsland?.id -> "当前焦点"
                                        island.completed -> "已完成"
                                        island.unlocked -> "已解锁"
                                        else -> "等待前岛完成"
                                    }
                                )
                            }

                            LinearProgressIndicator(
                                progress = { island.progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(999.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = Color.White.copy(alpha = 0.14f)
                            )
                        }
                    }
                }
            }
        }

        selectedIsland?.let { island ->
            Column(modifier = Modifier.weight(0.95f)) {
                IslandDetailPanel(
                    island = island,
                    onStartLesson = onStartLesson
                )
            }
        }
    }
}
