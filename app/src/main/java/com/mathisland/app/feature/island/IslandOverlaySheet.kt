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
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.SpacingTokens

@Composable
fun IslandOverlaySheet(
    state: IslandUiState,
    onStartLesson: (String) -> Unit,
    onOpenChest: () -> Unit = {}
) {
    val island = state.island
    val primaryLesson = island.lessons.firstOrNull { lesson -> lesson.id == state.primaryLessonId }
        ?: island.lessons.firstOrNull { lesson -> lesson.enabled && !lesson.completed }
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
                .padding(SpacingTokens.Xxl),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.Md)
        ) {
            IslandPanelHeader(island = island)
            if (state.handoffLabel != null && state.handoffTitle != null && state.handoffBody != null) {
                IslandHandoffCard(
                    kind = state.handoffKind,
                    label = state.handoffLabel,
                    title = state.handoffTitle,
                    body = state.handoffBody,
                    detailLabel = state.handoffDetailLabel,
                    detailTitle = state.handoffDetailTitle,
                    detailBody = state.handoffDetailBody
                )
            }
            IslandStoryCard(island = island)
            state.primaryActionLabel?.let { label ->
                WoodButton(
                    text = label,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("island-primary-action"),
                    enabled = state.primaryActionMode == IslandPrimaryActionMode.OpenChest || primaryLesson?.enabled == true,
                    onClick = {
                        when (state.primaryActionMode) {
                            IslandPrimaryActionMode.StartLesson -> primaryLesson?.let { onStartLesson(it.id) }
                            IslandPrimaryActionMode.OpenChest -> onOpenChest()
                        }
                    },
                    role = if (state.primaryActionMode == IslandPrimaryActionMode.OpenChest) {
                        ActionRole.Recommended
                    } else if (primaryLesson?.completed == true) {
                        ActionRole.Completed
                    } else {
                        ActionRole.Recommended
                    },
                    containerColor = if (primaryLesson?.completed == true &&
                        state.primaryActionMode == IslandPrimaryActionMode.StartLesson
                    ) {
                        IslandPanelTokens.CompletedButton
                    } else {
                        IslandPanelTokens.RecommendedButton
                    }
                )
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm),
                modifier = Modifier.testTag("panel-lessons-list")
            ) {
                items(island.lessons) { lesson ->
                    IslandLessonCard(
                        lesson = lesson,
                        isPrimary = lesson.id == primaryLesson?.id,
                        handoffKind = state.handoffKind,
                        onStartLesson = onStartLesson
                    )
                }
            }
        }
    }
}
