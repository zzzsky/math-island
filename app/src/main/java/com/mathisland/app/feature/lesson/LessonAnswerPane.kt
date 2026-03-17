package com.mathisland.app.feature.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.mathisland.app.QuestionRendererType
import com.mathisland.app.domain.model.Question
import com.mathisland.app.rendererTypeFor
import com.mathisland.app.ui.components.TabletChipLabel
import com.mathisland.app.ui.theme.TabletDeepWater
import com.mathisland.app.ui.theme.TabletFoam
import com.mathisland.app.ui.theme.TabletSand

private val TabletCoral = Color(0xFFEE964B)
private val TabletSky = Color(0xFF8ECae6)
private val TabletMint = Color(0xFF9ADBC7)

@Composable
fun LessonAnswerPane(
    question: Question,
    onAnswer: (String) -> Unit
) {
    when (rendererTypeFor(question.family)) {
        QuestionRendererType.CHOICE -> ChoiceRenderer(question = question, onAnswer = onAnswer)
        QuestionRendererType.NUMBER_PAD -> NumberPadRenderer(question = question, onAnswer = onAnswer)
        QuestionRendererType.RULER -> RulerRenderer(question = question, onAnswer = onAnswer)
        QuestionRendererType.CHANT -> ChantRenderer(question = question, onAnswer = onAnswer)
        QuestionRendererType.GROUP -> GroupRenderer(question = question, onAnswer = onAnswer)
        QuestionRendererType.SORT -> SortRenderer(question = question, onAnswer = onAnswer)
    }
}

@Composable
private fun ChoiceRenderer(
    question: Question,
    onAnswer: (String) -> Unit
) {
    RendererOptionsColumn(
        question = question,
        rendererTag = "renderer-choice",
        accent = MaterialTheme.colorScheme.primary,
        buttonLabel = "选择这个答案",
        onAnswer = onAnswer
    )
}

@Composable
private fun NumberPadRenderer(
    question: Question,
    onAnswer: (String) -> Unit
) {
    var enteredAnswer by remember(question.prompt) { mutableStateOf("") }
    val keypadRows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("清除", "0", "提交")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("renderer-number-pad"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0x663F536B)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TabletChipLabel(text = "数字键盘")
                Text(
                    text = "可输入答案：${question.choices.joinToString(" / ")}",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.84f)
                )
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xCC173C4C)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 20.dp)
                            .testTag("number-pad-display"),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = enteredAnswer.ifEmpty { "请输入答案" },
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        keypadRows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { key ->
                    val tag = when (key) {
                        "清除" -> "number-pad-clear"
                        "提交" -> "number-pad-submit"
                        else -> "number-pad-key-$key"
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(68.dp)
                            .testTag(tag),
                        onClick = {
                            when (key) {
                                "清除" -> enteredAnswer = ""
                                "提交" -> onAnswer(enteredAnswer)
                                else -> enteredAnswer += key
                            }
                        },
                        enabled = key != "提交" || enteredAnswer.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (key == "提交") TabletSand else Color(0xCC225267),
                            contentColor = if (key == "提交") TabletDeepWater else TabletFoam
                        ),
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Text(
                            text = key,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RulerRenderer(
    question: Question,
    onAnswer: (String) -> Unit
) {
    RendererOptionsColumn(
        question = question,
        rendererTag = "renderer-ruler",
        accent = TabletSky,
        header = "尺子工坊",
        helper = "拖动尺子观察刻度，再选择最合适的答案。",
        affordance = {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0x553A7088)),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("虚拟尺子", fontWeight = FontWeight.Bold)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(start = 72.dp)
                                .size(width = 36.dp, height = 12.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(TabletSky)
                                .testTag("tablet-ruler-handle")
                        )
                    }
                }
            }
        },
        buttonLabel = "对准刻度",
        onAnswer = onAnswer
    )
}

@Composable
private fun ChantRenderer(
    question: Question,
    onAnswer: (String) -> Unit
) {
    RendererOptionsColumn(
        question = question,
        rendererTag = "renderer-chant",
        accent = TabletCoral,
        header = "口诀回声",
        helper = "先大声念口诀，再点中正确答案。",
        affordance = {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0x55A65B4B)),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "节拍条",
                        modifier = Modifier.testTag("chant-beat-strip"),
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        repeat(4) { index ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height((20 + index * 6).dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (index % 2 == 0) TabletCoral else TabletSand)
                            )
                        }
                    }
                }
            }
        },
        buttonLabel = "念完选择",
        onAnswer = onAnswer
    )
}

@Composable
private fun GroupRenderer(
    question: Question,
    onAnswer: (String) -> Unit
) {
    RendererOptionsColumn(
        question = question,
        rendererTag = "renderer-group",
        accent = TabletMint,
        header = "分组操作台",
        helper = "先想想该怎么分组或分类，再确认答案。",
        affordance = {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0x553E7E70)),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "分类篮子",
                        modifier = Modifier.testTag("group-basket-zone"),
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(88.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        listOf("篮子 A", "篮子 B").forEach { label ->
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = Color(0x77498F81)),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(88.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(label, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        },
        buttonLabel = "确认分组",
        onAnswer = onAnswer
    )
}

@Composable
private fun SortRenderer(
    question: Question,
    onAnswer: (String) -> Unit
) {
    RendererOptionsColumn(
        question = question,
        rendererTag = "renderer-sort",
        accent = TabletSand,
        header = "灯塔排序板",
        helper = "比较大小或顺序后，点亮正确信号灯。",
        affordance = {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0x55B79C66)),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "排序信号灯",
                        modifier = Modifier.testTag("sort-signal-lights"),
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(34.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(Color(0xFFEF476F), Color(0xFFFFD166), Color(0xFF06D6A0)).forEach { light ->
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(99.dp))
                                    .background(light)
                            )
                        }
                    }
                }
            }
        },
        buttonLabel = "点亮信号",
        onAnswer = onAnswer
    )
}

@Composable
private fun RendererOptionsColumn(
    question: Question,
    rendererTag: String,
    accent: Color,
    header: String? = null,
    helper: String? = null,
    affordance: @Composable (() -> Unit)? = null,
    buttonLabel: String,
    onAnswer: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(rendererTag),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (header != null && helper != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0x662C647A)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TabletChipLabel(text = header)
                    Text(
                        text = helper,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f)
                    )
                }
            }
        }
        affordance?.invoke()
        question.choices.forEach { choice ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xCC225267)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = choice,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Button(
                        modifier = Modifier.testTag("answer-$choice"),
                        onClick = { onAnswer(choice) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accent,
                            contentColor = TabletDeepWater
                        )
                    ) {
                        Text(buttonLabel)
                    }
                }
            }
        }
    }
}
