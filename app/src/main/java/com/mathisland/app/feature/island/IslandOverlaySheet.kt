package com.mathisland.app.feature.island

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import com.mathisland.app.ui.components.SurfaceCard
import com.mathisland.app.ui.components.WoodButton
import com.mathisland.app.ui.theme.SurfaceLevel

@Composable
fun IslandOverlaySheet(
    state: IslandUiState,
    onStartLesson: (String) -> Unit
) {
    val island = state.island
    val primaryLesson = island.lessons.firstOrNull { lesson -> lesson.enabled && !lesson.completed }
        ?: island.lessons.firstOrNull()

    SurfaceCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("island-overlay-sheet"),
        level = SurfaceLevel.Primary,
        containerColor = IslandPanelTokens.OverlaySurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IslandPanelHeader(island = island)
            IslandStoryCard(island = island)
            primaryLesson?.let { lesson ->
                WoodButton(
                    text = if (lesson.completed) "再次练习 ${lesson.title}" else "继续 ${lesson.title}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("island-primary-action"),
                    enabled = lesson.enabled,
                    onClick = { onStartLesson(lesson.id) },
                    containerColor = if (lesson.completed) {
                        IslandPanelTokens.CompletedButton
                    } else {
                        IslandPanelTokens.RecommendedButton
                    }
                )
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.testTag("panel-lessons-list")
            ) {
                items(island.lessons) { lesson ->
                    IslandLessonCard(
                        lesson = lesson,
                        isPrimary = lesson.id == primaryLesson?.id,
                        onStartLesson = onStartLesson
                    )
                }
            }
        }
    }
}
