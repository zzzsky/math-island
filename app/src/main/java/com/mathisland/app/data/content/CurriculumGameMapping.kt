package com.mathisland.app.data.content

import com.mathisland.app.domain.model.CurriculumBundle
import com.mathisland.app.domain.model.CurriculumIslandContent
import com.mathisland.app.domain.model.CurriculumLesson
import com.mathisland.app.domain.model.Island
import com.mathisland.app.domain.model.Lesson
import com.mathisland.app.domain.model.MatchingGroup
import com.mathisland.app.domain.model.MatchingRound
import com.mathisland.app.domain.model.Question
import com.mathisland.app.domain.model.StepFeedbackHint
import com.mathisland.app.domain.model.StepPresentation
import com.mathisland.app.domain.model.StepBranchRule

fun curriculumToGameIslands(curriculum: CurriculumBundle): List<Island> =
    curriculum.catalog.islands.map { catalogEntry ->
        val content = curriculum.islands.first { island -> island.id == catalogEntry.id }
        content.toGameIsland()
    }

private fun CurriculumIslandContent.toGameIsland(): Island = Island(
    id = id,
    title = title,
    subtitle = subtitle,
    description = knowledgePoints.take(2).joinToString("、", prefix = "覆盖"),
    rewardSticker = rewardStickerFor(id),
    lessons = lessons.map { lesson -> lesson.toGameLesson(id, title) }
)

private fun CurriculumLesson.toGameLesson(
    islandId: String,
    islandTitle: String
): Lesson = Lesson(
    id = id,
    islandId = islandId,
    title = title,
    focus = islandTitle,
    summary = summary,
    questions = questionsForLesson(id, questionFamily),
    timeLimitSeconds = timeLimitSecondsForLesson(id)
)

private fun rewardStickerFor(islandId: String): String = when (islandId) {
    "calculation-island" -> "Bridge Builder"
    "measurement-geometry-island" -> "Ruler Ranger"
    "multiplication-island" -> "Forest Singer"
    "division-island" -> "Harbor Captain"
    "big-number-island" -> "Lighthouse Keeper"
    "classification-island" -> "Shell Sorter"
    "challenge-island" -> "Treasure Master"
    else -> "Island Explorer"
}

private fun timeLimitSecondsForLesson(lessonId: String): Int? = when (lessonId) {
    "challenge-sprint-01" -> 8
    else -> null
}

private fun questionsForLesson(lessonId: String, questionFamily: String): List<Question> =
    lessonQuestionBanks[lessonId] ?: questionsForFamily(questionFamily)

private val lessonQuestionBanks: Map<String, List<Question>> = mapOf(
    "challenge-mixed-01" to listOf(
        Question(
            prompt = "35 + 28 = ?",
            choices = listOf("53", "63", "73"),
            correctChoice = "63",
            hint = "先算个位，再进位。",
            family = "challenge"
        ),
        Question(
            prompt = "4 x 7 = ?",
            choices = listOf("21", "24", "28"),
            correctChoice = "28",
            hint = "四七二十八。",
            family = "challenge"
        ),
        Question(
            prompt = "1 米 = ? 厘米",
            choices = listOf("10", "100", "1000"),
            correctChoice = "100",
            hint = "单位换算要记住。",
            family = "challenge"
        )
    ),
    "challenge-sprint-01" to listOf(
        Question(
            prompt = "9 x 9 = ?",
            choices = listOf("72", "81", "99"),
            correctChoice = "81",
            hint = "九九八十一。",
            family = "challenge"
        ),
        Question(
            prompt = "56 - 14 = ?",
            choices = listOf("32", "42", "52"),
            correctChoice = "42",
            hint = "先减十位，再减个位。",
            family = "challenge"
        ),
        Question(
            prompt = "3 米 = ? 厘米",
            choices = listOf("30", "300", "3000"),
            correctChoice = "300",
            hint = "1 米 = 100 厘米。",
            family = "challenge"
        )
    ),
    "classification-match-01" to listOf(
        Question(
            prompt = "把数学工具和它最适合表示的意思连起来。",
            choices = emptyList(),
            correctChoice = "尺子=长度,秤=重量,时钟=时间",
            hint = "先看左边工具，再找到右边最贴切的意思。",
            family = "matching",
            leftItems = listOf("尺子", "秤", "时钟"),
            rightItems = listOf("时间", "重量", "长度")
        )
    ),
    "classification-match-02" to listOf(
        Question(
            prompt = "把分类数量和对应的标签连起来。",
            choices = emptyList(),
            correctChoice = "红贝壳=5 个,蓝贝壳=3 个,绿贝壳=4 个",
            hint = "先看贝壳名称，再找对应数量标签。",
            family = "matching",
            leftItems = listOf("红贝壳", "蓝贝壳", "绿贝壳"),
            rightItems = listOf("4 个", "5 个", "3 个")
        )
    ),
    "classification-match-03" to listOf(
        Question(
            prompt = "把物品和它所属的一类连起来。",
            choices = emptyList(),
            correctChoice = "纽扣=生活用品,树叶=植物,石子=自然材料",
            hint = "先看物品，再判断它最适合放进哪一类。",
            family = "matching",
            leftItems = listOf("纽扣", "树叶", "石子"),
            rightItems = listOf("植物", "自然材料", "生活用品")
        )
    ),
    "classification-match-04" to listOf(
        Question(
            prompt = "把工具和它最贴切的用途连起来。",
            choices = emptyList(),
            correctChoice = "尺子=长度,秤=重量,日历=日期,温度计=温度",
            hint = "先看左边工具，再找右边最贴切的用途。",
            family = "matching",
            leftItems = listOf("尺子", "秤", "日历", "温度计"),
            rightItems = listOf("温度", "日期", "重量", "长度")
        )
    ),
    "classification-match-05" to listOf(
        Question(
            prompt = "按两个小组完成语义配对。",
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
    ),
    "classification-match-06" to listOf(
        Question(
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
    ),
    "measure-fill-01" to listOf(
        Question(
            prompt = "把空格补完整。",
            choices = emptyList(),
            correctChoice = "100,200",
            hint = "先看单位，再把数字放进空格。",
            family = "fill-blank",
            blankParts = listOf("1 米 = ", " 厘米，2 米 = ", " 厘米。"),
            blankOptions = listOf("200", "100", "20")
        )
    ),
    "measure-fill-02" to listOf(
        Question(
            prompt = "把分米换算结果填进空格。",
            choices = emptyList(),
            correctChoice = "30,50",
            hint = "先想 1 分米 = 10 厘米。",
            family = "fill-blank",
            blankParts = listOf("3 分米 = ", " 厘米，5 分米 = ", " 厘米。"),
            blankOptions = listOf("50", "30", "8")
        )
    ),
    "measure-fill-03" to listOf(
        Question(
            prompt = "把长度换算结果填进空格。",
            choices = emptyList(),
            correctChoice = "200,70",
            hint = "先统一成厘米，再把数字放进空格。",
            family = "fill-blank",
            blankParts = listOf("2 米 = ", " 厘米，7 分米 = ", " 厘米。"),
            blankOptions = listOf("70", "200", "20")
        )
    ),
    "measure-fill-04" to listOf(
        Question(
            prompt = "把长度换算结果补完整。",
            choices = emptyList(),
            correctChoice = "400,90,6",
            hint = "先统一单位，再把数字拖进空格。",
            family = "fill-blank",
            blankParts = listOf("4 米 = ", " 厘米，9 分米 = ", " 厘米，60 厘米 = ", " 分米。"),
            blankOptions = listOf("90", "6", "400", "600")
        )
    ),
    "measure-fill-05" to listOf(
        Question(
            prompt = "把数字和单位补完整。",
            choices = emptyList(),
            correctChoice = "300,米,90",
            hint = "先看这个空格需要填数字还是单位。",
            family = "fill-blank",
            blankParts = listOf("3 ", " = ", "00 厘米，9 分米 = ", " 厘米。"),
            blankOptions = listOf("米", "90", "300", "厘米"),
            blankSlotKinds = listOf("number", "unit", "number")
        )
    ),
    "measure-fill-06" to listOf(
        Question(
            prompt = "按分区选项池把长度换算补完整。",
            choices = emptyList(),
            correctChoice = "米,300,分米,70",
            hint = "先看空格要填数字还是单位，再去对应分区找答案。",
            family = "fill-blank",
            blankParts = listOf("3 ", " = ", " 厘米，7 ", " = ", " 厘米。"),
            blankOptions = listOf("分米", "70", "米", "300", "厘米"),
            blankSlotKinds = listOf("unit", "number", "unit", "number")
        )
    ),
    "division-steps-01" to listOf(
        Question(
            prompt = "按步骤完成平均分。",
            choices = emptyList(),
            correctChoice = "平均分给 3 只小猴,每只 4 个",
            hint = "先想平均分，再判断每份有几个。",
            family = "multi-step",
            stepPrompts = listOf(
                "第一步：先判断这题要怎么分？",
                "第二步：每只小猴分到几个？"
            ),
            stepChoices = listOf(
                listOf("平均分给 3 只小猴", "先把 12 和 3 相加", "先比较水果颜色"),
                listOf("每只 3 个", "每只 4 个", "每只 5 个")
            ),
            stepFeedbackHints = listOf(
                StepFeedbackHint(
                    correctLabel = "看对分法",
                    correctBody = "你先判断出了这是平均分，后面的答案就更容易接对。",
                    incorrectLabel = "先看平均分",
                    incorrectBody = "先判断这题是不是平均分，再去想每只分到几个。",
                    timeoutLabel = "回看分法",
                    timeoutBody = "先回看你有没有先看出这是平均分。",
                    expandOnIncorrect = true
                ),
                StepFeedbackHint(
                    correctLabel = "答对结果",
                    correctBody = "这一步把每只分到几个说清楚了。",
                    incorrectLabel = "再看结果",
                    incorrectBody = "最后每只分到几个，要跟着前面的平均分思路来答。",
                    timeoutLabel = "回看结果",
                    timeoutBody = "最后每只分到几个，要跟着前面的平均分语境来答。",
                    expandOnTimeout = true
                )
            )
        )
    ),
    "division-steps-02" to listOf(
        Question(
            prompt = "按步骤完成租船判断。",
            choices = emptyList(),
            correctChoice = "先算 22 ÷ 4,6 条",
            hint = "先求商和余数，再判断要不要多租一条船。",
            family = "multi-step",
            stepPrompts = listOf(
                "第一步：这题先要算什么？",
                "第二步：22 ÷ 4 = 5 余 2，至少租几条船？"
            ),
            stepChoices = listOf(
                listOf("先算 22 ÷ 4", "先算 22 + 4", "先比较船的颜色"),
                listOf("5 条", "6 条", "7 条")
            ),
            stepFeedbackHints = listOf(
                StepFeedbackHint(
                    correctLabel = "先做除法",
                    correctBody = "你先选对了要做的运算，后面的判断才站得住。",
                    incorrectLabel = "先做除法",
                    incorrectBody = "先做除法，不要一开始就跳到别的运算。",
                    timeoutLabel = "回看运算",
                    timeoutBody = "先回看这题是不是应该先做除法。",
                    expandOnIncorrect = true
                ),
                StepFeedbackHint(
                    correctLabel = "补对船数",
                    correctBody = "你把有余数时要多租一条船说出来了。",
                    incorrectLabel = "看余数",
                    incorrectBody = "有余数时，最后的船数还要再多一条。",
                    timeoutLabel = "补一条船",
                    timeoutBody = "22 ÷ 4 还有余数，所以最后要多租一条船。",
                    expandOnTimeout = true
                )
            )
        )
    ),
    "division-steps-03" to listOf(
        Question(
            prompt = "按步骤完成装盒分配。",
            choices = emptyList(),
            correctChoice = "把 18 平均分给 6 个盒子,每个 3 个",
            hint = "先判断怎么平均分，再求每盒数量。",
            family = "multi-step",
            stepPrompts = listOf(
                "第一步：这题先要做什么？",
                "第二步：每个盒子装几个？"
            ),
            stepChoices = listOf(
                listOf("把 18 平均分给 6 个盒子", "先把 18 和 6 相减", "先数盒子颜色"),
                listOf("2 个", "3 个", "4 个")
            ),
            stepFeedbackHints = listOf(
                StepFeedbackHint(
                    correctLabel = "先定分法",
                    correctBody = "你先看出了这是平均分到盒子的题。",
                    incorrectLabel = "先定分法",
                    incorrectBody = "先明确是把 18 平均分到盒子里，再去想每盒几个。",
                    timeoutLabel = "回看分法",
                    timeoutBody = "先回看你有没有先看出这是平均分到盒子。",
                    expandOnIncorrect = true
                ),
                StepFeedbackHint(
                    correctLabel = "答出每盒",
                    correctBody = "这一步把每盒装几个说清楚了。",
                    incorrectLabel = "再看每盒",
                    incorrectBody = "每个盒子装几个，要顺着前面的平均分结果来答。",
                    timeoutLabel = "回看每盒",
                    timeoutBody = "最后每盒数量来自前面的平均分结果。",
                    expandOnTimeout = true
                )
            )
        )
    ),
    "division-steps-04" to listOf(
        Question(
            prompt = "按步骤完成装袋判断。",
            choices = emptyList(),
            correctChoice = "先算 17 ÷ 3,商是 5 余 2,6 个袋子",
            hint = "先算除法，再判断余下的要不要多准备一个袋子。",
            family = "multi-step",
            stepPrompts = listOf(
                "第一步：先要算什么？",
                "第二步：17 ÷ 3 的结果是什么？",
                "第三步：至少需要几个袋子？"
            ),
            stepChoices = listOf(
                listOf("先算 17 ÷ 3", "先算 17 + 3", "先比较袋子颜色"),
                listOf("商是 4 余 1", "商是 5 余 2", "商是 6 余 1"),
                listOf("5 个袋子", "6 个袋子", "7 个袋子")
            ),
            stepFeedbackHints = listOf(
                StepFeedbackHint(
                    correctLabel = "先定算法",
                    correctBody = "你先找对了要算的除法，后面步骤才能接起来。",
                    incorrectLabel = "先看要算什么",
                    incorrectBody = "先看这一步要先算什么，不要直接跳到后面的数量。",
                    timeoutLabel = "回看起点",
                    timeoutBody = "先回看这题是不是该先算 17 ÷ 3。",
                    expandOnIncorrect = true
                ),
                StepFeedbackHint(
                    correctLabel = "算清商余",
                    correctBody = "这一步把商和余数说准确了。",
                    incorrectLabel = "核对商余",
                    incorrectBody = "先把商和余数说准确，再去判断最后要几个袋子。",
                    timeoutLabel = "回看商余",
                    timeoutBody = "先回看商和余数，再看最后的袋子数量。"
                ),
                StepFeedbackHint(
                    correctLabel = "落到袋数",
                    correctBody = "最后袋子数量已经顺着商和余数判断出来了。",
                    incorrectLabel = "接上袋数",
                    incorrectBody = "袋子数量要跟着前面的商和余数一起判断。",
                    timeoutLabel = "回到袋子数",
                    timeoutBody = "最后袋子数量要跟着前面的商和余数来判断。",
                    expandOnTimeout = true
                )
            )
        )
    ),
    "division-steps-05" to listOf(
        Question(
            prompt = "按条件步骤完成装袋判断。",
            choices = emptyList(),
            correctChoice = "有余数,商是4余2,5个袋子",
            hint = "先判断有没有余数，再走对应的后续步骤。",
            family = "multi-step",
            stepPrompts = listOf(
                "第一步：先判断这次平均分会不会有剩余？",
                "第二步",
                "第三步"
            ),
            stepChoices = listOf(
                listOf("有余数", "正好分完", "还要先做加法"),
                listOf("占位"),
                listOf("占位")
            ),
            stepBranchKeys = listOf("branch-start", "step-2", "step-3"),
            stepBranchRules = mapOf(
                "branch-start" to listOf(
                    StepBranchRule("有余数", "remainder-step-2"),
                    StepBranchRule("正好分完", "exact-step-2"),
                    StepBranchRule("还要先做加法", "add-step-2")
                ),
                "remainder-step-2" to listOf(
                    StepBranchRule("*", "remainder-step-3")
                ),
                "exact-step-2" to listOf(
                    StepBranchRule("*", "exact-step-3")
                ),
                "add-step-2" to listOf(
                    StepBranchRule("*", "add-step-3")
                )
            ),
            stepBranchPrompts = mapOf(
                "remainder-step-2" to "第二步：18 ÷ 4 的结果是什么？",
                "remainder-step-3" to "第三步：至少需要几个袋子？",
                "exact-step-2" to "第二步：如果正好分完，每袋装几个？",
                "exact-step-3" to "第三步：这种情况至少需要几个袋子？",
                "add-step-2" to "第二步：这题先做加法对吗？",
                "add-step-3" to "第三步：现在该回到哪种判断？"
            ),
            stepBranchChoices = mapOf(
                "remainder-step-2" to listOf("商是4余2", "商是5余1", "商是6余0"),
                "remainder-step-3" to listOf("4个袋子", "5个袋子", "6个袋子"),
                "exact-step-2" to listOf("每袋4个", "每袋5个", "每袋6个"),
                "exact-step-3" to listOf("4个袋子", "5个袋子", "6个袋子"),
                "add-step-2" to listOf("对，先做加法", "不对，应该先做除法", "先比较袋子颜色"),
                "add-step-3" to listOf("回到有没有余数的判断", "直接结束", "去做乘法")
            ),
            stepFeedbackHints = listOf(
                StepFeedbackHint(
                    correctLabel = "选对路线",
                    correctBody = "先判断有没有余数，后面的步骤路线才会稳定。",
                    incorrectLabel = "先定路线",
                    incorrectBody = "先判断有没有余数，再决定后面走哪条步骤路线。",
                    timeoutLabel = "先看路线",
                    timeoutBody = "先看你有没有先判断剩余，再回看后面的步骤。",
                    expandOnIncorrect = true
                ),
                StepFeedbackHint(
                    correctLabel = "算清结果",
                    correctBody = "这一步把路线里的计算结果说清楚了。",
                    incorrectLabel = "核对结果",
                    incorrectBody = "把这一步的商和余数，或整除结果，再核对一次。",
                    timeoutLabel = "回看计算",
                    timeoutBody = "先看这一步的结果说得对不对，再去看最后数量。"
                ),
                StepFeedbackHint(
                    correctLabel = "落到数量",
                    correctBody = "最后的袋子数量跟着前一步结果一起定下来了。",
                    incorrectLabel = "接上结论",
                    incorrectBody = "最后袋子数量要跟着前一步结果一起判断。",
                    timeoutLabel = "回看结论",
                    timeoutBody = "最后袋子数量要跟着前一步的结果一起判断。",
                    expandOnTimeout = true
                )
            )
        )
    ),
    "division-steps-06" to listOf(
        Question(
            prompt = "按条件步骤完成装盒判断。",
            choices = emptyList(),
            correctChoice = "正好分完,商是4,4个盒子",
            hint = "先判断有没有余数，再走对应步骤，最后收束到同一个装盒判断。",
            family = "multi-step",
            stepPrompts = listOf(
                "第一步：先判断这次平均分会不会有剩余？",
                "第二步",
                "第三步"
            ),
            stepChoices = listOf(
                listOf("有余数", "正好分完"),
                listOf("占位"),
                listOf("占位")
            ),
            stepBranchKeys = listOf("branch-start", "step-2", "step-3"),
            stepBranchRules = mapOf(
                "branch-start" to listOf(
                    StepBranchRule("有余数", "remainder-step-2"),
                    StepBranchRule("正好分完", "exact-step-2")
                ),
                "remainder-step-2" to listOf(
                    StepBranchRule("*", "shared-final-step")
                ),
                "exact-step-2" to listOf(
                    StepBranchRule("*", "shared-final-step")
                )
            ),
            stepBranchPrompts = mapOf(
                "remainder-step-2" to "第二步：14 ÷ 3 的结果是什么？",
                "exact-step-2" to "第二步：12 ÷ 3 的结果是什么？",
                "shared-final-step" to "第三步：现在至少要准备几个盒子？"
            ),
            stepBranchChoices = mapOf(
                "remainder-step-2" to listOf("商是4余2", "商是5余1", "商是3余2"),
                "exact-step-2" to listOf("商是4", "商是3", "商是5"),
                "shared-final-step" to listOf("4个盒子", "5个盒子", "6个盒子")
            ),
            stepFeedbackHints = listOf(
                StepFeedbackHint(
                    correctLabel = "选对路线",
                    correctBody = "先分清有没有余数，后面的步骤才会走对。",
                    incorrectLabel = "先分路线",
                    incorrectBody = "先判断这次是有余数还是正好分完。",
                    timeoutLabel = "先看分路",
                    timeoutBody = "先看你有没有先选对路线，再回看后面的判断。"
                ),
                StepFeedbackHint(
                    correctLabel = "算对结果",
                    correctBody = "这一步把当前路线里的除法结果算准了。",
                    incorrectLabel = "重看计算",
                    incorrectBody = "先重看这一步的除法结果，后面的装盒数量都跟着它走。",
                    timeoutLabel = "回看计算",
                    timeoutBody = "先看分支里的除法结果，再去看最后装盒数量。",
                    expandOnIncorrect = true
                ),
                StepFeedbackHint(
                    correctLabel = "收束判断",
                    correctBody = "两条路线最后都收束到了同一个装盒结论。",
                    incorrectLabel = "接回结论",
                    incorrectBody = "前面的路线不同，但最后都要回到同一个装盒判断。",
                    timeoutLabel = "共享结论",
                    timeoutBody = "虽然前面路线不同，最后都回到同一个装盒判断。",
                    expandOnTimeout = true
                )
            )
        )
    ),
    "division-steps-07" to listOf(
        Question(
            prompt = "按条件步骤完成装盒复核。",
            choices = emptyList(),
            correctChoice = "正好分完,商是4,4个盒子,正好装完，不用多准备",
            hint = "先判断有没有余数，再按路线完成计算，最后回到统一结论。",
            family = "multi-step",
            stepPrompts = listOf(
                "第一步：先判断这次平均分会不会有剩余？",
                "第二步",
                "第三步",
                "第四步"
            ),
            stepChoices = listOf(
                listOf("有余数", "正好分完"),
                listOf("占位"),
                listOf("4个盒子", "5个盒子"),
                listOf("正好装完，不用多准备", "有余数，要多准备1个盒子")
            ),
            stepBranchKeys = listOf("branch-start", "step-2", "shared-final-step", "shared-wrap-up-step"),
            stepBranchRules = mapOf(
                "branch-start" to listOf(
                    StepBranchRule("有余数", "remainder-step-2"),
                    StepBranchRule("正好分完", "exact-step-2")
                ),
                "remainder-step-2" to listOf(
                    StepBranchRule("*", "shared-final-step")
                ),
                "exact-step-2" to listOf(
                    StepBranchRule("*", "shared-final-step")
                ),
                "shared-final-step" to listOf(
                    StepBranchRule("*", "shared-wrap-up-step")
                )
            ),
            stepBranchPrompts = mapOf(
                "remainder-step-2" to "第二步：12 ÷ 5 的结果是什么？",
                "exact-step-2" to "第二步：12 ÷ 3 的结果是什么？",
                "shared-final-step" to "第三步：现在至少要准备几个盒子？",
                "shared-wrap-up-step" to "第四步：最后该怎么复述？"
            ),
            stepBranchChoices = mapOf(
                "remainder-step-2" to listOf("商是2余2", "商是4余1"),
                "exact-step-2" to listOf("商是4", "商是3"),
                "shared-final-step" to listOf("4个盒子", "5个盒子"),
                "shared-wrap-up-step" to listOf("正好装完，不用多准备", "有余数，要多准备1个盒子")
            ),
            stepPresentations = listOf(
                StepPresentation("先定路线", "先判断这次平均分会不会有剩余。", "分支判断"),
                StepPresentation("计算结果", "把除法结果说清楚。", "除法结果"),
                StepPresentation("统一装盒", "不管哪条路，最后都要回到装盒数量。", "装盒数量"),
                StepPresentation("完整结论", "把你的判断完整说出来。", "最终结论")
            ),
            stepBranchPresentations = mapOf(
                "remainder-step-2" to StepPresentation("余数路线", "先说出商和余数。", "余数结果"),
                "exact-step-2" to StepPresentation("整除路线", "直接说出整除后的商。", "整除结果"),
                "shared-final-step" to StepPresentation("统一装盒", "现在两条路线都回到同一个装盒判断。", "装盒数量")
            ),
            stepFeedbackHints = listOf(
                StepFeedbackHint(
                    correctLabel = "路线清楚",
                    correctBody = "你先把路线判断清楚了，后面的步骤更容易接稳。",
                    incorrectLabel = "先看路线",
                    incorrectBody = "先判断这次平均分会不会有剩余，再往下走。",
                    timeoutLabel = "回看起点",
                    timeoutBody = "先回看你有没有先选对路线，再看后面的步骤。"
                ),
                StepFeedbackHint(
                    correctLabel = "算准分支",
                    correctBody = "这一步把分支里的计算结果说清楚了。",
                    incorrectLabel = "回看分支",
                    incorrectBody = "先回看这一步的分支计算，再继续看后面的统一结论。",
                    timeoutLabel = "回看计算",
                    timeoutBody = "先看分支里的计算结果，再去看统一装盒结论。",
                    expandOnIncorrect = true
                ),
                StepFeedbackHint(
                    correctLabel = "接到结论",
                    correctBody = "这一步把不同路线重新收束到了同一个装盒判断。",
                    incorrectLabel = "看统一结论",
                    incorrectBody = "先看这里有没有把前面的结果正确接回统一装盒判断。",
                    timeoutLabel = "统一结论",
                    timeoutBody = "本题已经结束，先看这里怎么把前面的路线收回统一结论。",
                    expandOnTimeout = true
                ),
                StepFeedbackHint(
                    correctLabel = "说完整了",
                    correctBody = "你把最后的结论说完整了，这一步把整道题收稳了。",
                    incorrectLabel = "补完整句",
                    incorrectBody = "最后一步要把前面的判断完整复述出来。",
                    timeoutLabel = "回看收尾",
                    timeoutBody = "最后再看一眼完整结论是怎么说的。"
                )
            )
        )
    )
)

private fun questionsForFamily(questionFamily: String): List<Question> = when (questionFamily) {
    "CALCULATION" -> listOf(
        Question(
            prompt = "26 + 18 = ?",
            choices = listOf("34", "44", "54"),
            correctChoice = "44",
            hint = "先算 20 + 10，再把 6 和 8 合起来。",
            family = "calculation"
        ),
        Question(
            prompt = "72 - 37 = ?",
            choices = listOf("35", "45", "55"),
            correctChoice = "35",
            hint = "个位不够减，先借 1 个十。",
            family = "calculation"
        ),
        Question(
            prompt = "43 + 19 = ?",
            choices = listOf("52", "62", "72"),
            correctChoice = "62",
            hint = "先算 43 + 10，再加 9。",
            family = "calculation"
        )
    )

    "MEASUREMENT" -> listOf(
        Question(
            prompt = "铅笔长 15 ___",
            choices = listOf("米", "厘米", "分米"),
            correctChoice = "厘米",
            hint = "铅笔比较短，要用更小的长度单位。",
            family = "measurement"
        ),
        Question(
            prompt = "1 米 = ? 厘米",
            choices = listOf("10", "100", "1000"),
            correctChoice = "100",
            hint = "米和厘米的换算要记牢。",
            family = "measurement"
        ),
        Question(
            prompt = "拉动长方形框架后更容易变成什么图形？",
            choices = listOf("三角形", "平行四边形", "圆形"),
            correctChoice = "平行四边形",
            hint = "这种图形对边相等，而且容易变形。",
            family = "measurement"
        )
    )

    "MULTIPLICATION" -> listOf(
        Question(
            prompt = "5 + 5 + 5 + 5 = ?",
            choices = listOf("4 x 5", "5 x 5", "3 x 5"),
            correctChoice = "4 x 5",
            hint = "有 4 个 5。",
            family = "multiplication"
        ),
        Question(
            prompt = "四（ ）二十八",
            choices = listOf("五", "六", "七"),
            correctChoice = "七",
            hint = "四七二十八。",
            family = "multiplication"
        ),
        Question(
            prompt = "3 x 8 = ?",
            choices = listOf("21", "24", "28"),
            correctChoice = "24",
            hint = "三八二十四。",
            family = "multiplication"
        )
    )

    "DIVISION" -> listOf(
        Question(
            prompt = "12 个苹果平均分给 3 只小猴，每只几个？",
            choices = listOf("3", "4", "5"),
            correctChoice = "4",
            hint = "12 里面有 3 个 4。",
            family = "division"
        ),
        Question(
            prompt = "24 ÷ 4 = ?",
            choices = listOf("5", "6", "7"),
            correctChoice = "6",
            hint = "四六二十四。",
            family = "division"
        ),
        Question(
            prompt = "22 人租船，每条船限乘 4 人，至少要租几条船？",
            choices = listOf("5", "6", "7"),
            correctChoice = "6",
            hint = "22 ÷ 4 = 5 余 2，还要再加 1 条船。",
            family = "division"
        )
    )

    "BIG_NUMBER" -> listOf(
        Question(
            prompt = "一千零三十六写作多少？",
            choices = listOf("1036", "10036", "1360"),
            correctChoice = "1036",
            hint = "注意 0 的位置。",
            family = "big-number"
        ),
        Question(
            prompt = "8848 和 7999，哪个更大？",
            choices = listOf("8848", "7999", "一样大"),
            correctChoice = "8848",
            hint = "先比较千位。",
            family = "big-number"
        ),
        Question(
            prompt = "把 2050、2500、2005 按从小到大排序，最后一个是？",
            choices = listOf("2005", "2050", "2500"),
            correctChoice = "2500",
            hint = "比较千位后再比较百位。",
            family = "big-number"
        )
    )

    "CLASSIFICATION" -> listOf(
        Question(
            prompt = "先按形状分类，再按扣眼数分类，这说明什么？",
            choices = listOf("只能有一种分类法", "可以按不同标准分类", "分类后不能再统计"),
            correctChoice = "可以按不同标准分类",
            hint = "分类标准可以切换。",
            family = "classification"
        ),
        Question(
            prompt = "把 8 个圆形纽扣分成一类，这一步最像什么？",
            choices = listOf("分类统计", "长度测量", "乘法口诀"),
            correctChoice = "分类统计",
            hint = "先定标准，再记录结果。",
            family = "classification"
        ),
        Question(
            prompt = "记录每一类有几个，主要是在做什么？",
            choices = listOf("分类统计", "竖式验算", "图形拼组"),
            correctChoice = "分类统计",
            hint = "分完后还要数一数。",
            family = "classification"
        )
    )

    "MATCHING" -> lessonQuestionBanks.getValue("classification-match-01")

    "FILL_BLANK" -> lessonQuestionBanks.getValue("measure-fill-01")

    "MULTI_STEP" -> lessonQuestionBanks.getValue("division-steps-01")

    "CHALLENGE" -> lessonQuestionBanks.getValue("challenge-mixed-01")

    else -> error("Unsupported question family: $questionFamily")
}
