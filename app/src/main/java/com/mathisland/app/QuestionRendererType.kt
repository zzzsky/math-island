package com.mathisland.app

enum class QuestionRendererType {
    CHOICE,
    NUMBER_PAD,
    RULER,
    CHANT,
    GROUP,
    SORT
}

fun rendererTypeFor(family: String): QuestionRendererType = when (family) {
    "challenge" -> QuestionRendererType.NUMBER_PAD
    "measurement" -> QuestionRendererType.RULER
    "multiplication" -> QuestionRendererType.CHANT
    "division", "classification" -> QuestionRendererType.GROUP
    "big-number" -> QuestionRendererType.SORT
    else -> QuestionRendererType.CHOICE
}
