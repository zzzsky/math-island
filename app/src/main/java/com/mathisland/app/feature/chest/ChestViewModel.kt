package com.mathisland.app.feature.chest

data class ChestUiState(
    val stickers: List<String>,
    val totalStars: Int
) {
    val stickerCount: Int = stickers.size
    val summaryText: String = "累计星星 $totalStars · 收集到 $stickerCount 张岛屿贴纸"
}

object ChestViewModel {
    fun uiState(
        stickers: List<String>,
        totalStars: Int
    ): ChestUiState = ChestUiState(
        stickers = stickers,
        totalStars = totalStars
    )
}
