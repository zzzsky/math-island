package com.mathisland.app.feature.chest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.ui.components.StatusChip
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.StatusVariant
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
fun StickerCard(
    sticker: String,
    index: Int,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.testTag("chest-sticker-card-$index"),
        colors = CardDefaults.cardColors(containerColor = Color(0xCC1C4D5F)),
        shape = RadiusTokens.CardMd
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .padding(SpacingTokens.Lg),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            StatusChip(text = "Island Sticker", variant = StatusVariant.Recommended)
            Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.Xs)) {
                Text(
                    text = sticker,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "已收入宝箱",
                    style = TypographyTokens.BodySecondary,
                    color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface)
                )
            }
        }
    }
}
