package com.mathisland.app

import com.mathisland.app.data.progress.legacy.LegacySharedPreferencesProgressStore

@Deprecated(
    message = "Compatibility alias for legacy SharedPreferences progress only. Use data.progress.ProgressStore implementations directly.",
    replaceWith = ReplaceWith("com.mathisland.app.data.progress.legacy.LegacySharedPreferencesProgressStore")
)
internal typealias MathIslandProgressStore = LegacySharedPreferencesProgressStore
