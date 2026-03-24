package com.mathisland.app.feature.parent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.mathisland.app.ui.components.TabletInfoCard
import com.mathisland.app.ui.theme.SpacingTokens

@Composable
fun ParentSummarySections(
    state: ParentSummaryUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)) {
            TabletInfoCard(
                modifier = Modifier
                    .weight(1f)
                    .testTag("parent-summary-today-card"),
                title = "今日学习",
                subtitle = state.todayLearnedText,
                body = "把今天完成的内容单独列出来，方便快速确认主线进度。"
            )
            TabletInfoCard(
                modifier = Modifier
                    .weight(1f)
                    .testTag("parent-summary-weak-card"),
                title = "薄弱知识点",
                subtitle = state.weakTopicsText,
                body = "这些知识点建议优先回看，下一轮复习会优先照顾这里。"
            )
        }
        TabletInfoCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("parent-summary-recommended-card"),
            title = "建议优先复习",
            subtitle = state.recommendedIslandText,
            body = "下一次进入家长总结或返回地图时，可以优先关注这座岛上的推荐内容。"
        )
    }
}
