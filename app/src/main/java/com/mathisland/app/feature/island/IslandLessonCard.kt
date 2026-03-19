package com.mathisland.app.feature.island

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.feature.map.MapFeedbackKind
import com.mathisland.app.feature.map.MapTabletLessonUiState
import com.mathisland.app.ui.components.StatusChip
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.components.WoodButton
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.StatusVariant
import com.mathisland.app.ui.theme.SurfaceLevel

@Composable
fun IslandLessonCard(
    lesson: MapTabletLessonUiState,
    isPrimary: Boolean,
    handoffKind: MapFeedbackKind?,
    modifier: Modifier = Modifier,
    onStartLesson: (String) -> Unit
) {
    val containerColor = when {
        lesson.completed -> IslandPanelTokens.LessonCompletedSurface
        isPrimary -> IslandPanelTokens.LessonRecommendedSurface
        else -> IslandPanelTokens.LessonSurface
    }
    val borderColor = when {
        lesson.completed -> IslandPanelTokens.CompletedBorder
        isPrimary -> IslandPanelTokens.RecommendedBorder
        else -> IslandPanelTokens.LessonBorder
    }

    StoryPanelCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("panel-lesson-card-${lesson.id}"),
        level = SurfaceLevel.Secondary,
        containerColor = containerColor,
        borderColor = borderColor,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                StatusChip(
                    text = lessonStatusLabel(lesson, isPrimary, handoffKind),
                    variant = lessonStatusVariant(lesson, isPrimary, handoffKind)
                )
            }
            Text(
                text = lesson.summary,
                color = IslandPanelTokens.SummaryText,
                style = MaterialTheme.typography.bodyMedium
            )
            WoodButton(
                text = lessonActionLabel(lesson, isPrimary, handoffKind),
                onClick = { onStartLesson(lesson.id) },
                modifier = Modifier.testTag("panel-start-${lesson.id}"),
                enabled = lesson.enabled,
                role = when {
                    lesson.completed -> ActionRole.Completed
                    isPrimary -> ActionRole.Recommended
                    else -> ActionRole.Secondary
                },
                containerColor = when {
                    lesson.completed -> IslandPanelTokens.CompletedButton
                    isPrimary -> IslandPanelTokens.RecommendedButton
                    else -> IslandPanelTokens.DefaultButton
                },
                contentColor = if (lesson.completed) MaterialTheme.colorScheme.onPrimary else IslandPanelTokens.ButtonContent
            )
        }
    }
}

private fun lessonStatusLabel(
    lesson: MapTabletLessonUiState,
    isPrimary: Boolean,
    handoffKind: MapFeedbackKind?
): String = when {
    lesson.completed -> "已完成"
    isPrimary && handoffKind == MapFeedbackKind.NewIsland -> "主线推荐"
    isPrimary && handoffKind == MapFeedbackKind.Replay -> "回放优先"
    isPrimary && handoffKind == MapFeedbackKind.Chest -> "收藏后继续"
    isPrimary -> "新解锁"
    lesson.enabled -> "可进入"
    else -> "未开始"
}

private fun lessonActionLabel(
    lesson: MapTabletLessonUiState,
    isPrimary: Boolean,
    handoffKind: MapFeedbackKind?
): String = when {
    lesson.completed -> "再次练习"
    isPrimary && handoffKind == MapFeedbackKind.NewIsland -> "开始主线"
    isPrimary && handoffKind == MapFeedbackKind.Replay -> "开始回放"
    isPrimary && handoffKind == MapFeedbackKind.Chest -> "稍后继续"
    isPrimary -> "推荐开始"
    else -> "进入课程"
}

private fun lessonStatusVariant(
    lesson: MapTabletLessonUiState,
    isPrimary: Boolean,
    handoffKind: MapFeedbackKind?
): StatusVariant = when {
    lesson.completed -> StatusVariant.Success
    isPrimary && handoffKind == MapFeedbackKind.Replay -> StatusVariant.Highlight
    isPrimary && handoffKind == MapFeedbackKind.Chest -> StatusVariant.Caution
    isPrimary -> StatusVariant.Recommended
    lesson.enabled -> StatusVariant.Highlight
    else -> StatusVariant.Caution
}
