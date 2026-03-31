package com.mathisland.app.feature.parent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.verticalScroll
import com.mathisland.app.ui.components.ActionButton
import com.mathisland.app.ui.components.SurfaceCard
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
fun ParentSummaryTabletScreen(
    state: ParentSummaryUiState,
    onBackHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("parent-summary-screen"),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Lg)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "家长学习摘要",
                style = TypographyTokens.ScreenTitle,
                fontWeight = FontWeight.Black
            )
            ActionButton(
                text = "完成",
                onClick = onBackHome,
                role = ActionRole.Completed,
            )
        }
        SurfaceCard(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            level = SurfaceLevel.Page
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(SpacingTokens.Xxl),
                verticalArrangement = Arrangement.spacedBy(SpacingTokens.Lg)
            ) {
                ParentSummaryHeroPanel(state = state)
                ParentSummarySections(state = state)
            }
        }
    }
}
