package com.mathisland.app.feature.island

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.feature.common.TabletChipLabel
import com.mathisland.app.feature.common.TabletDeepWater

private val CompletedLessonColor = Color(0xFF4B6F44)

@Composable
fun IslandOverlaySheet(
    state: IslandUiState,
    onStartLesson: (String) -> Unit
) {
    val island = state.island
    val primaryLesson = island.lessons.firstOrNull { lesson -> lesson.enabled && !lesson.completed }
        ?: island.lessons.firstOrNull()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("island-overlay-sheet"),
        colors = CardDefaults.cardColors(containerColor = Color(0xCC173C4C)),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TabletChipLabel(text = if (island.completed) "本岛完成" else "岛屿面板")
            Text(
                text = island.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                modifier = Modifier.testTag("panel-island-title")
            )
            Text(
                text = "${island.subtitle} · ${island.description}",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.84f)
            )
            LinearProgressIndicator(
                progress = { island.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(999.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color.White.copy(alpha = 0.14f)
            )
            primaryLesson?.let { lesson ->
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("island-primary-action"),
                    onClick = { onStartLesson(lesson.id) },
                    enabled = lesson.enabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = TabletDeepWater
                    )
                ) {
                    Text(if (lesson.completed) "再次练习 ${lesson.title}" else "继续 ${lesson.title}")
                }
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.testTag("panel-lessons-list")
            ) {
                items(island.lessons) { lesson ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0x80203E4C)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
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
                                modifier = Modifier.testTag("panel-start-${lesson.id}"),
                                onClick = { onStartLesson(lesson.id) },
                                enabled = lesson.enabled,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (lesson.completed) CompletedLessonColor else MaterialTheme.colorScheme.secondary,
                                    contentColor = if (lesson.completed) Color.White else TabletDeepWater
                                )
                            ) {
                                Text(
                                    when {
                                        lesson.completed -> "再次练习"
                                        lesson.id == primaryLesson?.id -> "推荐开始"
                                        else -> "进入课程"
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
