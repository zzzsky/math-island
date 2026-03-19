package com.mathisland.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mathisland.app.ui.theme.ActionColorSet
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.ActionTokens

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    role: ActionRole = ActionRole.Primary,
    containerColor: Color? = null,
    contentColor: Color? = null,
    borderColor: Color? = null,
) {
    val colors = resolveColors(
        role = role,
        containerColor = containerColor,
        contentColor = contentColor,
        borderColor = borderColor,
    )
    val shape = RoundedCornerShape(ActionTokens.CornerRadius)
    val contentPadding = PaddingValues(
        horizontal = ActionTokens.HorizontalPadding,
        vertical = ActionTokens.VerticalPadding,
    )

    if (role == ActionRole.OutlinedSecondary) {
        OutlinedButton(
            modifier = modifier,
            onClick = onClick,
            enabled = enabled,
            shape = shape,
            border = BorderStroke(1.dp, colors.borderColor),
            contentPadding = contentPadding,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = colors.containerColor,
                contentColor = colors.contentColor,
            ),
        ) {
            Text(text)
        }
    } else {
        Button(
            modifier = modifier,
            onClick = onClick,
            enabled = enabled,
            shape = shape,
            contentPadding = contentPadding,
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.containerColor,
                contentColor = colors.contentColor,
            ),
        ) {
            Text(text)
        }
    }
}

private fun resolveColors(
    role: ActionRole,
    containerColor: Color?,
    contentColor: Color?,
    borderColor: Color?,
): ActionColorSet {
    val defaults = ActionTokens.colors(role)
    return defaults.copy(
        containerColor = containerColor ?: defaults.containerColor,
        contentColor = contentColor ?: defaults.contentColor,
        borderColor = borderColor ?: defaults.borderColor,
    )
}
