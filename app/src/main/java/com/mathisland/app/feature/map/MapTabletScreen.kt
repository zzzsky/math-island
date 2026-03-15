package com.mathisland.app.feature.map

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.feature.common.TabletChipLabel
import com.mathisland.app.feature.common.TabletDeepWater

private val MapSeaweed = Color(0xFF4B6F44)

@Composable
fun MapTabletScreen(
    state: MapTabletUiState,
    onBackHome: () -> Unit,
    onOpenChest: () -> Unit,
    onStartLesson: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
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

        LazyColumn(
            modifier = Modifier.testTag("map-islands-list"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(state.islands) { island ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (island.unlocked) Color(0xCC18475A) else Color(0x6618475A)
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
                                    text = "${island.subtitle} · ${island.description}",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f)
                                )
                            }
                            TabletChipLabel(text = if (island.unlocked) "已解锁" else "等待前岛完成")
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

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            island.lessons.forEach { lesson ->
                                Card(
                                    modifier = Modifier.weight(1f),
                                    colors = CardDefaults.cardColors(containerColor = Color(0x80203E4C)),
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text(
                                            text = lesson.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = lesson.summary,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
                                        )
                                        Button(
                                            modifier = Modifier.testTag("start-${lesson.id}"),
                                            onClick = { onStartLesson(lesson.id) },
                                            enabled = lesson.enabled,
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (lesson.completed) MapSeaweed else MaterialTheme.colorScheme.primary,
                                                contentColor = if (lesson.completed) Color.White else TabletDeepWater
                                            )
                                        ) {
                                            Text(if (lesson.completed) "再次练习" else "开始")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
