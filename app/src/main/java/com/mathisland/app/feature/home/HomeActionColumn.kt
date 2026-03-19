package com.mathisland.app.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.mathisland.app.domain.usecase.HomeState
import com.mathisland.app.ui.components.ActionButton
import com.mathisland.app.ui.components.TabletActionCard
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.SpacingTokens

@Composable
fun HomeActionColumn(
    state: HomeState,
    onContinue: () -> Unit,
    onOpenMap: () -> Unit,
    onOpenChest: () -> Unit,
    onOpenParent: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Md)
    ) {
        TabletActionCard(
            title = "继续冒险",
            subtitle = if (state.isReview) {
                "先完成 2 道同类型复习题，再回主线继续推进。"
            } else {
                "从当前解锁的第一节继续出发。"
            },
            buttonText = "开始闯关",
            buttonTag = "home-continue-adventure",
            role = ActionRole.Recommended,
            onClick = onContinue
        )
        TabletActionCard(
            title = "地图",
            subtitle = "查看主岛、节点进度和下一步可玩课程。",
            buttonText = "打开地图",
            buttonTag = "home-open-map",
            role = ActionRole.Primary,
            onClick = onOpenMap
        )
        TabletActionCard(
            title = "宝箱",
            subtitle = "查看已经收集到的贴纸奖励和总星星。",
            buttonText = "查看宝箱",
            buttonTag = "home-open-chest",
            role = ActionRole.Secondary,
            onClick = onOpenChest
        )
        ActionButton(
            text = "家长入口",
            modifier = Modifier.testTag("home-open-parent"),
            onClick = onOpenParent,
            role = ActionRole.OutlinedSecondary,
        )
    }
}
