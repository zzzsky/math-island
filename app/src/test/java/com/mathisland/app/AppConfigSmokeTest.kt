package com.mathisland.app

import org.junit.Assert.assertEquals
import org.junit.Test

class AppConfigSmokeTest {
    @Test
    fun appTarget_isTabletLandscapeMvp() {
        assertEquals("tablet-landscape", BuildConfig.TARGET_DEVICE_PROFILE)
    }
}
