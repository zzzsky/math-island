package com.mathisland.app.feature.reward

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.feature.common.TabletChipLabel
import com.mathisland.app.feature.common.TabletFoam
import com.mathisland.app.feature.common.TabletInfoCard
import com.mathisland.app.feature.common.TabletStatTile

@Composable
fun RewardTabletScreen(
    state: RewardUiState,
    onContinue: () -> Unit,
    onSecondaryAction: (() -> Unit)? = null
) {
    val reward = state.reward
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(0.72f),
            colors = CardDefaults.cardColors(containerColor = Color(0xEE123847)),
            shape = RoundedCornerShape(32.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TabletChipLabel(text = if (reward.timedOut) "时间到" else "关卡完成")
                Text(
                    text = reward.lessonTitle,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black
                )
                if (reward.timedOut) {
                    TabletInfoCard(
                        title = "冲刺结果",
                        subtitle = "时间到，本次冲刺记为练习",
                        body = "本关不会计入通关，但会把综合挑战加入下一轮优先复习。"
                    )
                }
                if (reward.gradeLabel != null && reward.gradeDescription != null) {
                    TabletInfoCard(
                        title = "成绩分级",
                        subtitle = reward.gradeLabel,
                        body = reward.gradeDescription
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TabletStatTile(
                        modifier = Modifier.weight(1f),
                        title = "本关星星",
                        value = reward.starsEarned.toString(),
                        accent = MaterialTheme.colorScheme.primary
                    )
                    TabletStatTile(
                        modifier = Modifier.weight(1f),
                        title = "答对题目",
                        value = "${reward.correctAnswers}/${reward.totalQuestions}",
                        accent = MaterialTheme.colorScheme.secondary
                    )
                    TabletStatTile(
                        modifier = Modifier.weight(1f),
                        title = "累计星星",
                        value = state.totalStars.toString(),
                        accent = Color(0xFF9ADBC7)
                    )
                }
                reward.newIslandTitle?.let { title ->
                    TabletInfoCard(
                        title = "新岛屿解锁",
                        subtitle = title,
                        body = "前一座主岛已经完成，可以继续拓展地图。"
                    )
                }
                reward.newStickerName?.let { sticker ->
                    TabletInfoCard(
                        title = "贴纸奖励",
                        subtitle = sticker,
                        body = "岛屿全清后会进入宝箱收藏。"
                    )
                }
                reward.secondaryActionLabel?.let { label ->
                    onSecondaryAction?.let { action ->
                        Button(
                            modifier = Modifier.testTag("reward-retry-sprint"),
                            onClick = action,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = TabletFoam
                            )
                        ) {
                            Text(label)
                        }
                    }
                }
                Button(
                    modifier = Modifier.testTag("reward-return-map"),
                    onClick = onContinue,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("回到地图")
                }
            }
        }
    }
}
