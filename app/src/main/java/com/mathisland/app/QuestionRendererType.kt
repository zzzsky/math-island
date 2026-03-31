package com.mathisland.app

enum class QuestionRendererType {
    CHOICE,
    NUMBER_PAD,
    RULER,
    CHANT,
    GROUP,
    SORT,
    MATCHING,
    FILL_BLANK,
    MULTI_STEP
}

fun rendererTypeFor(family: String): QuestionRendererType = when (family) {
    "challenge" -> QuestionRendererType.NUMBER_PAD
    "measurement" -> QuestionRendererType.RULER
    "multiplication" -> QuestionRendererType.CHANT
    "division", "classification" -> QuestionRendererType.GROUP
    "big-number" -> QuestionRendererType.SORT
    "matching" -> QuestionRendererType.MATCHING
    "fill-blank" -> QuestionRendererType.FILL_BLANK
    "multi-step" -> QuestionRendererType.MULTI_STEP
    else -> QuestionRendererType.CHOICE
}
