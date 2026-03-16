package com.mathisland.app.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.sp
import com.mathisland.app.domain.usecase.HomeState
import com.mathisland.app.feature.common.TabletFoam
import com.mathisland.app.feature.common.TabletSand
import com.mathisland.app.ui.components.TabletActionCard
import com.mathisland.app.ui.components.TabletChipLabel
import com.mathisland.app.ui.components.TabletInfoCard
import com.mathisland.app.ui.components.TabletStatTile

private val HomeCoral = Color(0xFFEE964B)

@Composable
fun HomeTabletScreen(
    state: HomeState,
    onContinue: () -> Unit,
    onOpenMap: () -> Unit,
    onOpenChest: () -> Unit,
    onOpenParent: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Card(
            modifier = Modifier.weight(1.3f),
            colors = CardDefaults.cardColors(containerColor = Color(0xCC113B4A)),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    TabletChipLabel(text = "TABLET MVP")
                    Text(
                        text = "数学岛",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "短回合数学冒险，把今天的 3 到 5 分钟变成一段可见的地图推进。",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                    )
                    state.nextLessonTitle?.let { lessonTitle ->
                        TabletInfoCard(
                            title = if (state.isReview) "小海鸥求助" else "继续冒险",
                            subtitle = listOfNotNull(lessonTitle, state.nextLessonFocus)
                                .joinToString(" · "),
                            body = state.nextLessonSummary.orEmpty()
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TabletStatTile(
                        modifier = Modifier.weight(1f),
                        title = "星星",
                        value = state.totalStars.toString(),
                        accent = MaterialTheme.colorScheme.primary
                    )
                    TabletStatTile(
                        modifier = Modifier.weight(1f),
                        title = "贴纸",
                        value = state.stickerCount.toString(),
                        accent = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        Column(
            modifier = Modifier.weight(0.9f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                accent = TabletSand,
                onClick = onContinue
            )
            TabletActionCard(
                title = "地图",
                subtitle = "查看主岛、节点进度和下一步可玩课程。",
                buttonText = "打开地图",
                buttonTag = "home-open-map",
                accent = HomeCoral,
                onClick = onOpenMap
            )
            TabletActionCard(
                title = "宝箱",
                subtitle = "查看已经收集到的贴纸奖励和总星星。",
                buttonText = "查看宝箱",
                buttonTag = "home-open-chest",
                accent = Color(0xFF98D9C2),
                onClick = onOpenChest
            )
            Button(
                modifier = Modifier.testTag("home-open-parent"),
                onClick = onOpenParent,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0x335A7B88),
                    contentColor = TabletFoam
                )
            ) {
                Text("家长入口")
            }
        }
    }
}
