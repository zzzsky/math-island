package com.mathisland.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.ActionTokens
import com.mathisland.app.ui.theme.StatusVariant

@Composable
fun ReturnActionCard(
    label: String,
    title: String,
    body: String,
    accentColor: Color,
    badgeVariant: StatusVariant,
    actionRole: ActionRole,
    modifier: Modifier = Modifier,
    badgeTag: String? = null,
    emphasisTag: String? = null
) {
    val roleColors = ActionTokens.colors(actionRole)
    val emphasisColor = when (actionRole) {
        ActionRole.Primary -> accentColor.copy(alpha = 0.16f)
        ActionRole.Recommended -> roleColors.containerColor.copy(alpha = 0.22f)
        ActionRole.Secondary -> roleColors.containerColor.copy(alpha = 0.18f)
        ActionRole.Completed -> roleColors.containerColor.copy(alpha = 0.22f)
        ActionRole.OutlinedSecondary -> accentColor.copy(alpha = 0.14f)
    }
    Box(
        modifier = (emphasisTag?.let { Modifier.testTag(it) } ?: Modifier)
            .fillMaxWidth()
            .background(
                color = emphasisColor,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(2.dp)
    ) {
        TabletInfoCard(
            modifier = modifier,
            title = label,
            subtitle = title,
            body = body,
            accentColor = accentColor,
            containerColor = accentColor.copy(alpha = 0.16f),
            badgeText = label,
            badgeVariant = badgeVariant,
            badgeTag = badgeTag
        )
    }
}
