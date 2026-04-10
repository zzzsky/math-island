package com.mathisland.app.feature.level

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.mathisland.app.MathIslandTheme
import com.mathisland.app.domain.model.MatchingGroup
import com.mathisland.app.domain.model.MatchingRound
import com.mathisland.app.domain.model.Question
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MatchingQuestionPaneTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun matchingQuestion_requiresAllPairsBeforeSubmit() {
        var submittedAnswer: String? = null
        val question = Question(
            prompt = "把数学工具和它最适合表示的意思连起来。",
            choices = emptyList(),
            correctChoice = "尺子=长度,秤=重量,时钟=时间",
            hint = "先看左边工具，再找到右边最贴切的意思。",
            family = "matching",
            leftItems = listOf("尺子", "秤", "时钟"),
            rightItems = listOf("时间", "重量", "长度")
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = { submittedAnswer = it }
                )
            }
        }

        composeRule.onNodeWithTag("renderer-matching").assertIsDisplayed()
        composeRule.onNodeWithTag("matching-submit").assertIsNotEnabled()

        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-left-select-0"))
        composeRule.onNodeWithTag("matching-left-select-0").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-right-assign-2"))
        composeRule.onNodeWithTag("matching-right-assign-2").assertIsEnabled()
        composeRule.onNodeWithTag("matching-right-assign-2").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-left-select-1"))
        composeRule.onNodeWithTag("matching-left-select-1").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-right-assign-1"))
        composeRule.onNodeWithTag("matching-right-assign-1").assertIsEnabled()
        composeRule.onNodeWithTag("matching-right-assign-1").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-left-select-2"))
        composeRule.onNodeWithTag("matching-left-select-2").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-right-assign-0"))
        composeRule.onNodeWithTag("matching-right-assign-0").assertIsEnabled()
        composeRule.onNodeWithTag("matching-right-assign-0").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-submit"))

        composeRule.onNodeWithTag("matching-submit").assertIsEnabled().performClick()

        assertEquals("尺子=长度,秤=重量,时钟=时间", submittedAnswer)
    }

    @Test
    fun matchingQuestion_supportsFourPairAssignments() {
        var submittedAnswer: String? = null
        val question = Question(
            prompt = "把工具和它最贴切的用途连起来。",
            choices = emptyList(),
            correctChoice = "尺子=长度,秤=重量,日历=日期,温度计=温度",
            hint = "先看左边工具，再找右边最贴切的用途。",
            family = "matching",
            leftItems = listOf("尺子", "秤", "日历", "温度计"),
            rightItems = listOf("温度", "日期", "重量", "长度")
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = { submittedAnswer = it }
                )
            }
        }

        composeRule.onNodeWithTag("renderer-matching").assertIsDisplayed()
        composeRule.onNodeWithTag("matching-submit").assertIsNotEnabled()

        listOf(3, 2, 1, 0).forEachIndexed { leftIndex, rightIndex ->
            composeRule.onNodeWithTag("renderer-matching")
                .performScrollToNode(hasTestTag("matching-left-select-$leftIndex"))
            composeRule.onNodeWithTag("matching-left-select-$leftIndex").performClick()
            composeRule.waitForIdle()
            composeRule.onNodeWithTag("renderer-matching")
                .performScrollToNode(hasTestTag("matching-right-assign-$rightIndex"))
            composeRule.onNodeWithTag("matching-right-assign-$rightIndex").performClick()
            composeRule.waitForIdle()
        }

        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-submit"))
        composeRule.onNodeWithTag("matching-submit").assertIsEnabled().performClick()

        assertEquals("尺子=长度,秤=重量,日历=日期,温度计=温度", submittedAnswer)
    }

    @Test
    fun matchingQuestion_supportsGroupedSections() {
        var submittedAnswer: String? = null
        val question = Question(
            prompt = "按两个小组完成配对。",
            choices = emptyList(),
            correctChoice = "平均分苹果=用除法,合并两堆贝壳=用加法||尺子=测长度,秤=测重量",
            hint = "先完成一组，再完成下一组。",
            family = "matching",
            matchingGroups = listOf(
                MatchingGroup(
                    title = "看场景选算法",
                    leftItems = listOf("平均分苹果", "合并两堆贝壳"),
                    rightItems = listOf("用加法", "用除法")
                ),
                MatchingGroup(
                    title = "看工具选用途",
                    leftItems = listOf("尺子", "秤"),
                    rightItems = listOf("测重量", "测长度")
                )
            )
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = { submittedAnswer = it }
                )
            }
        }

        composeRule.onNodeWithTag("renderer-matching").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-group-0"))
        composeRule.onNodeWithTag("matching-group-0").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-group-1"))
        composeRule.onNodeWithTag("matching-group-1").assertIsDisplayed()

        listOf(
            Triple(0, 0, 1),
            Triple(0, 1, 0),
            Triple(1, 0, 1),
            Triple(1, 1, 0)
        ).forEach { (groupIndex, leftIndex, rightIndex) ->
            composeRule.onNodeWithTag("renderer-matching")
                .performScrollToNode(hasTestTag("matching-left-select-$groupIndex-$leftIndex"))
            composeRule.onNodeWithTag("matching-left-select-$groupIndex-$leftIndex").performClick()
            composeRule.waitForIdle()
            composeRule.onNodeWithTag("renderer-matching")
                .performScrollToNode(hasTestTag("matching-right-assign-$groupIndex-$rightIndex"))
            composeRule.onNodeWithTag("matching-right-assign-$groupIndex-$rightIndex").performClick()
            composeRule.waitForIdle()
        }

        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-submit"))
        composeRule.onNodeWithTag("matching-submit").assertIsEnabled().performClick()

        assertEquals(
            "平均分苹果=用除法,合并两堆贝壳=用加法||尺子=测长度,秤=测重量",
            submittedAnswer
        )
    }

    @Test
    fun matchingQuestion_supportsMultiRoundProgression() {
        var submittedAnswer: String? = null
        val question = Question(
            prompt = "按两轮完成语义配对。",
            choices = emptyList(),
            correctChoice = "平均分苹果=用除法,合并两堆贝壳=用加法>>>用除法=求每份有多少,用加法=求合起来一共多少",
            hint = "先完成当前轮，再进入下一轮。",
            family = "matching",
            matchingRounds = listOf(
                MatchingRound(
                    title = "第一轮：看场景选算法",
                    prompt = "第一轮：把场景和最合适的算法连起来。",
                    groups = listOf(
                        MatchingGroup(
                            title = "",
                            leftItems = listOf("平均分苹果", "合并两堆贝壳"),
                            rightItems = listOf("用加法", "用除法")
                        )
                    )
                ),
                MatchingRound(
                    title = "第二轮：看算法选作用",
                    prompt = "第二轮：把算法和它最适合解决的问题连起来。",
                    groups = listOf(
                        MatchingGroup(
                            title = "",
                            leftItems = listOf("用除法", "用加法"),
                            rightItems = listOf("求合起来一共多少", "求每份有多少")
                        )
                    )
                )
            )
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = { submittedAnswer = it }
                )
            }
        }

        composeRule.onNodeWithTag("matching-round-chip-0").assertIsDisplayed()
        composeRule.onNodeWithTag("matching-round-chip-1").assertIsDisplayed()
        composeRule.onNodeWithText("第一轮：把场景和最合适的算法连起来。").assertIsDisplayed()

        listOf(1, 0).forEachIndexed { leftIndex, rightIndex ->
            composeRule.onNodeWithTag("renderer-matching")
                .performScrollToNode(hasTestTag("matching-left-select-$leftIndex"))
            composeRule.onNodeWithTag("matching-left-select-$leftIndex").performClick()
            composeRule.waitForIdle()
            composeRule.onNodeWithTag("renderer-matching")
                .performScrollToNode(hasTestTag("matching-right-assign-$rightIndex"))
            composeRule.onNodeWithTag("matching-right-assign-$rightIndex").performClick()
            composeRule.waitForIdle()
        }

        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-next-round"))
        composeRule.onNodeWithTag("matching-next-round").assertIsEnabled().performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasText("第二轮：把算法和它最适合解决的问题连起来。"))
        composeRule.onNodeWithText("第二轮：把算法和它最适合解决的问题连起来。").assertIsDisplayed()

        listOf(1, 0).forEachIndexed { leftIndex, rightIndex ->
            composeRule.onNodeWithTag("renderer-matching")
                .performScrollToNode(hasTestTag("matching-left-select-$leftIndex"))
            composeRule.onNodeWithTag("matching-left-select-$leftIndex").performClick()
            composeRule.waitForIdle()
            composeRule.onNodeWithTag("renderer-matching")
                .performScrollToNode(hasTestTag("matching-right-assign-$rightIndex"))
            composeRule.onNodeWithTag("matching-right-assign-$rightIndex").performClick()
            composeRule.waitForIdle()
        }

        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-submit"))
        composeRule.onNodeWithTag("matching-submit").assertIsEnabled().performClick()

        assertEquals(
            "平均分苹果=用除法,合并两堆贝壳=用加法>>>用除法=求每份有多少,用加法=求合起来一共多少",
            submittedAnswer
        )
    }
}
