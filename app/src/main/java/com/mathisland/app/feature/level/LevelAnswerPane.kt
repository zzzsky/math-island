package com.mathisland.app.feature.level

import androidx.compose.runtime.Composable
import com.mathisland.app.QuestionRendererType
import com.mathisland.app.domain.model.Question
import com.mathisland.app.feature.level.renderers.RendererActionState
import com.mathisland.app.feature.level.renderers.AnswerFeedbackUiState
import com.mathisland.app.feature.level.renderers.ChantQuestionPane
import com.mathisland.app.feature.level.renderers.ChoiceQuestionPane
import com.mathisland.app.feature.level.renderers.FillBlankQuestionPane
import com.mathisland.app.feature.level.renderers.GroupQuestionPane
import com.mathisland.app.feature.level.renderers.MatchingQuestionPane
import com.mathisland.app.feature.level.renderers.MultiStepQuestionPane
import com.mathisland.app.feature.level.renderers.NumberPadQuestionPane
import com.mathisland.app.feature.level.renderers.RulerQuestionPane
import com.mathisland.app.feature.level.renderers.SortQuestionPane
import com.mathisland.app.rendererTypeFor

@Composable
fun LevelAnswerPane(
    question: Question,
    feedback: AnswerFeedbackUiState? = null,
    actionState: RendererActionState = com.mathisland.app.feature.level.renderers.rendererActionStateFor(
        feedback = feedback,
        inputEnabled = true
    ),
    onAnswer: (String) -> Unit
) {
    when (rendererTypeFor(question.family)) {
        QuestionRendererType.CHOICE -> ChoiceQuestionPane(
            question = question,
            feedback = feedback,
            actionState = actionState,
            onAnswer = onAnswer
        )

        QuestionRendererType.NUMBER_PAD -> NumberPadQuestionPane(
            question = question,
            feedback = feedback,
            actionState = actionState,
            onAnswer = onAnswer
        )

        QuestionRendererType.RULER -> RulerQuestionPane(
            question = question,
            feedback = feedback,
            actionState = actionState,
            onAnswer = onAnswer
        )

        QuestionRendererType.CHANT -> ChantQuestionPane(
            question = question,
            feedback = feedback,
            actionState = actionState,
            onAnswer = onAnswer
        )

        QuestionRendererType.GROUP -> GroupQuestionPane(
            question = question,
            feedback = feedback,
            actionState = actionState,
            onAnswer = onAnswer
        )

        QuestionRendererType.SORT -> SortQuestionPane(
            question = question,
            feedback = feedback,
            actionState = actionState,
            onAnswer = onAnswer
        )

        QuestionRendererType.MATCHING -> MatchingQuestionPane(
            question = question,
            feedback = feedback,
            actionState = actionState,
            onAnswer = onAnswer
        )

        QuestionRendererType.FILL_BLANK -> FillBlankQuestionPane(
            question = question,
            feedback = feedback,
            actionState = actionState,
            onAnswer = onAnswer
        )

        QuestionRendererType.MULTI_STEP -> MultiStepQuestionPane(
            question = question,
            feedback = feedback,
            actionState = actionState,
            onAnswer = onAnswer
        )
    }
}
