package com.mathisland.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mathisland.app.ui.theme.StatusVariant

@Composable
fun ReturnActionCard(
    label: String,
    title: String,
    body: String,
    accentColor: Color,
    badgeVariant: StatusVariant,
    modifier: Modifier = Modifier,
    badgeTag: String? = null
) {
    TabletInfoCard(
        modifier = modifier,
        title = label,
        subtitle = title,
        body = body,
        accentColor = accentColor,
        containerColor = accentColor.copy(alpha = 0.12f),
        badgeText = label,
        badgeVariant = badgeVariant,
        badgeTag = badgeTag
    )
}
