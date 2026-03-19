package com.mathisland.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.SurfaceLevel

@Composable
fun TabletActionCard(
    title: String,
    subtitle: String,
    buttonText: String,
    buttonTag: String? = null,
    role: ActionRole = ActionRole.Primary,
    onClick: () -> Unit
) {
    StoryPanelCard(
        modifier = Modifier.fillMaxWidth(),
        level = SurfaceLevel.Primary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f),
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
            )
            ActionButton(
                text = buttonText,
                modifier = buttonTag?.let(Modifier::testTag) ?: Modifier,
                onClick = onClick,
                role = role,
            )
        }
    }
}
