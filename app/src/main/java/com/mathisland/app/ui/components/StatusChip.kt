package com.mathisland.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.StatusTokens
import com.mathisland.app.ui.theme.StatusVariant

@Composable
fun StatusChip(
    text: String,
    modifier: Modifier = Modifier,
    variant: StatusVariant = StatusVariant.Neutral,
    leadingIcon: ImageVector? = null,
) {
    val colors = StatusTokens.colors(variant)
    Row(
        modifier = modifier
            .border(
                width = if (colors.borderColor.alpha > 0f) 1.dp else 0.dp,
                color = colors.borderColor,
                shape = RadiusTokens.Pill
            )
            .background(
                color = colors.containerColor,
                shape = RadiusTokens.Pill
            )
            .padding(
                horizontal = StatusTokens.HorizontalPadding,
                vertical = StatusTokens.VerticalPadding
            ),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon?.let { icon ->
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.contentColor
            )
        }
        Text(
            text = text,
            color = colors.contentColor,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
