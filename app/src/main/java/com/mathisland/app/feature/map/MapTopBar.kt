package com.mathisland.app.feature.map

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.mathisland.app.feature.island.IslandPanelTokens
import com.mathisland.app.ui.components.WoodButton

@Composable
fun MapTopBar(
    totalStars: Int,
    starsScale: Float,
    chestScale: Float,
    chestPulseAlpha: Float,
    showChestPulse: Boolean,
    onBackHome: () -> Unit,
    onOpenChest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        WoodButton(
            text = "返回首页",
            onClick = onBackHome,
            containerColor = IslandPanelTokens.DefaultButton
        )
        Box(
            modifier = Modifier
                .scale(chestScale)
                .testTag("map-open-chest-container")
        ) {
            WoodButton(
                text = "打开宝箱",
                onClick = onOpenChest,
                modifier = Modifier.testTag("map-open-chest"),
                containerColor = IslandPanelTokens.RecommendedButton
            )
            if (showChestPulse) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = 2.dp,
                            color = MapScreenTokens.ChestPulseBorder.copy(alpha = chestPulseAlpha),
                            shape = RoundedCornerShape(999.dp)
                        )
                        .testTag("map-open-chest-pulse")
                )
            }
        }
        Card(
            modifier = Modifier
                .scale(starsScale)
                .testTag("map-total-stars-pill"),
            colors = CardDefaults.cardColors(containerColor = MapScreenTokens.StarsPillSurface),
            shape = RoundedCornerShape(999.dp)
        ) {
            Text(
                text = "总星星 $totalStars",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .testTag("map-total-stars")
            )
        }
    }
}
