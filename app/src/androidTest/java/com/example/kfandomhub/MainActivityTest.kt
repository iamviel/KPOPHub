package com.example.kfandomhub

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.kfandomhub.ui.activity.MainActivity
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun appContext_usesExpectedPackageName() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        assertEquals("com.example.kfandomhub", appContext.packageName)
    }

    @Test
    fun homeRecyclerView_isDisplayedOnLaunch() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val context = instrumentation.targetContext
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val activity = instrumentation.startActivitySync(intent)

        try {
            onView(withId(R.id.rvGroup)).check(matches(isDisplayed()))
        } finally {
            instrumentation.runOnMainSync {
                activity.finish()
            }
        }
    }
}
