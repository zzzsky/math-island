package com.mathisland.app.data.content

import com.mathisland.app.domain.model.CurriculumBundle

data class CurriculumCoverageReport(
    val missingKnowledgePoints: Set<String>
)

object ContentCoverage {
    fun coveredSourceUnits(curriculum: CurriculumBundle): Set<String> =
        curriculum.islands
            .flatMap { island -> island.sourceUnits }
            .toSet()

    fun coveredKnowledgePoints(curriculum: CurriculumBundle): Set<String> =
        curriculum.islands
            .flatMap { island -> island.knowledgePoints }
            .toSet()
}

fun validateCurriculumCoverage(curriculum: CurriculumBundle): CurriculumCoverageReport {
    val approvedKnowledgePoints = setOf(
        "进位加法",
        "退位减法",
        "单位换算",
        "测量方法",
        "乘法意义",
        "口诀编制",
        "平均分",
        "用口诀求商",
        "连加运算",
        "解决实际问题",
        "平面与立体转化",
        "平行四边形",
        "余数规律",
        "实际取舍",
        "读数写数",
        "大小比较",
        "三位数计算",
        "验算方法",
        "分类统计"
    )
    val coveredKnowledgePoints = ContentCoverage.coveredKnowledgePoints(curriculum)

    return CurriculumCoverageReport(
        missingKnowledgePoints = approvedKnowledgePoints - coveredKnowledgePoints
    )
}
