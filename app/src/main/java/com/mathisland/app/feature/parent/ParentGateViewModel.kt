package com.mathisland.app.feature.parent

data class ParentGateUiState(
    val chipLabel: String,
    val title: String,
    val question: String,
    val subtitle: String,
    val answers: List<String>
)

object ParentGateViewModel {
    fun uiState(): ParentGateUiState = ParentGateUiState(
        chipLabel = "家长入口",
        title = "请先完成一道口算",
        question = "8 + 7 = ?",
        subtitle = "用于确认由家长进入摘要页。",
        answers = listOf("14", "15", "16")
    )
}
