package com.mathisland.app.feature.chest

import androidx.compose.runtime.Composable
import com.mathisland.app.ui.components.StatusChip
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.StatusVariant
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
fun ChestEmptyStateCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0x80244C5E)),
        shape = RadiusTokens.CardMd
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.Lg),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.Xs)
        ) {
            StatusChip(text = "还没有贴纸", variant = StatusVariant.Neutral)
            Text(
                text = "先去完成一整座主岛",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "每清空一座岛，就会在这里点亮一张新的纪念贴纸。",
                style = TypographyTokens.BodyPrimary,
                color = TextToneTokens.supporting(MaterialTheme.colorScheme.onSurface)
            )
        }
    }
}
