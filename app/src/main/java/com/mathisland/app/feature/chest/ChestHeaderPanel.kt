package com.mathisland.app.feature.chest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import com.mathisland.app.ui.components.ActionButton
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
fun ChestHeaderPanel(
    summaryText: String,
    onBackHome: () -> Unit,
    onOpenMap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.testTag("chest-header-panel"),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Md)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)) {
            ActionButton(
                text = "返回首页",
                onClick = onBackHome,
                role = ActionRole.OutlinedSecondary,
            )
            ActionButton(
                text = "回到地图",
                modifier = Modifier.testTag("chest-open-map"),
                onClick = onOpenMap,
                role = ActionRole.Recommended,
            )
        }
        Text(
            text = "宝箱收藏",
            style = TypographyTokens.ScreenTitle,
            fontWeight = FontWeight.Black
        )
        Text(
            text = summaryText,
            style = TypographyTokens.BodyLead,
            color = TextToneTokens.high(MaterialTheme.colorScheme.onSurface)
        )
    }
}
