package com.mathisland.app.data.content

import com.mathisland.app.domain.model.CurriculumBundle
import com.mathisland.app.domain.model.CurriculumIslandContent
import com.mathisland.app.domain.model.CurriculumLesson
import com.mathisland.app.domain.model.Island
import com.mathisland.app.domain.model.Lesson
import com.mathisland.app.domain.model.Question

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
