package com.mathisland.app.feature.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mathisland.app.ui.theme.TabletFoam
import com.mathisland.app.ui.theme.TabletSand

val TabletDeepWater: Color
    get() = com.mathisland.app.ui.theme.TabletDeepWater

val TabletFoam: Color
    get() = com.mathisland.app.ui.theme.TabletFoam

val TabletSand: Color
    get() = com.mathisland.app.ui.theme.TabletSand

@Deprecated(
    message = "Use ui.components.TabletActionCard instead.",
    replaceWith = ReplaceWith("com.mathisland.app.ui.components.TabletActionCard(title, subtitle, buttonText, buttonTag, accent, onClick)")
)
@Composable
fun TabletActionCard(
    title: String,
    subtitle: String,
    buttonText: String,
    buttonTag: String? = null,
    accent: Color,
    onClick: () -> Unit
) {
    com.mathisland.app.ui.components.TabletActionCard(
        title = title,
        subtitle = subtitle,
        buttonText = buttonText,
        buttonTag = buttonTag,
        accent = accent,
        onClick = onClick
    )
}

@Deprecated(
    message = "Use ui.components.TabletInfoCard instead.",
    replaceWith = ReplaceWith("com.mathisland.app.ui.components.TabletInfoCard(modifier, title, subtitle, body)")
)
@Composable
fun TabletInfoCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    body: String
) {
    com.mathisland.app.ui.components.TabletInfoCard(
        modifier = modifier,
        title = title,
        subtitle = subtitle,
        body = body
    )
}

@Deprecated(
    message = "Use ui.components.TabletStatTile instead.",
    replaceWith = ReplaceWith("com.mathisland.app.ui.components.TabletStatTile(modifier, title, value, accent)")
)
@Composable
fun TabletStatTile(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    accent: Color
) {
    com.mathisland.app.ui.components.TabletStatTile(
        modifier = modifier,
        title = title,
        value = value,
        accent = accent
    )
}

@Deprecated(
    message = "Use ui.components.TabletChipLabel instead.",
    replaceWith = ReplaceWith("com.mathisland.app.ui.components.TabletChipLabel(text, modifier)")
)
@Composable
fun TabletChipLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    com.mathisland.app.ui.components.TabletChipLabel(text = text, modifier = modifier)
}
