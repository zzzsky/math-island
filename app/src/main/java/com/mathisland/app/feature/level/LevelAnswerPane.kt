package com.mathisland.app.feature.level

import androidx.compose.runtime.Composable
import com.mathisland.app.QuestionRendererType
import com.mathisland.app.domain.model.Question
import com.mathisland.app.feature.level.renderers.ChantQuestionPane
import com.mathisland.app.feature.level.renderers.ChoiceQuestionPane
import com.mathisland.app.feature.level.renderers.GroupQuestionPane
import com.mathisland.app.feature.level.renderers.NumberPadQuestionPane
import com.mathisland.app.feature.level.renderers.RulerQuestionPane
import com.mathisland.app.feature.level.renderers.SortQuestionPane
import com.mathisland.app.rendererTypeFor

@Composable
fun LevelAnswerPane(
    question: Question,
    onAnswer: (String) -> Unit
) {
    when (rendererTypeFor(question.family)) {
        QuestionRendererType.CHOICE -> ChoiceQuestionPane(question = question, onAnswer = onAnswer)
        QuestionRendererType.NUMBER_PAD -> NumberPadQuestionPane(question = question, onAnswer = onAnswer)
        QuestionRendererType.RULER -> RulerQuestionPane(question = question, onAnswer = onAnswer)
        QuestionRendererType.CHANT -> ChantQuestionPane(question = question, onAnswer = onAnswer)
        QuestionRendererType.GROUP -> GroupQuestionPane(question = question, onAnswer = onAnswer)
        QuestionRendererType.SORT -> SortQuestionPane(question = question, onAnswer = onAnswer)
    }
}
