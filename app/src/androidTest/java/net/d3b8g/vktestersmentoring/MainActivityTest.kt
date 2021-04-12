package net.d3b8g.vktestersmentoring

import android.preference.PreferenceManager
import android.view.Gravity
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import net.d3b8g.vktestersmentoring.ui.login.LoginActivity
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest{

    companion object{
        var count_field_profile_start = ""
        var count_field_profile_restart = ""
    }

    @Test
    fun isNormalDataInView(){

        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open())

        onView(withId(R.id.main_user_visits)).check(matches(isDisplayed()))

        onView(withId(R.id.main_user_visits)).check(matches(withText(`not`(containsString("null")))))

        onView(withId(R.id.main_user_visits)).check(matches(withText(`not`(containsString("-")))))

    }

    @Test
    fun checkCounterStep(){

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        activityScenario.onActivity {
            count_field_profile_start = it.findViewById<TextView>(R.id.main_user_visits).text.toString()
        }

        isNormalDataInView()

        activityScenario.moveToState(Lifecycle.State.RESUMED)
        activityScenario.recreate()

        activityScenario.onActivity {
            count_field_profile_restart = it.findViewById<TextView>(R.id.main_user_visits).text.toString()
        }

        assertFalse(count_field_profile_start == count_field_profile_restart)

    }

    @Test
    fun testCardFragment(){

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.rcv_longrid)).perform(click())

        onView(withId(R.id.lg_components_block)).check(matches(isDisplayed()))

        onView(withId(R.id.lg_title)).check(matches(withText(`not`(containsString("plug")))))

        Thread.sleep(2000)

        onView(withId(R.id.lg_had_read)).check(matches(isNotChecked())).perform(click())
        // isNotChecked = assertFalse
        activityScenario.onActivity {
            PreferenceManager.getDefaultSharedPreferences(it).apply {
                assertFalse(getBoolean("check_box_0",false))

            }
        }
        onView(withId(R.id.lg_had_read)).perform(click())

    }

    @Test
    fun splashScreenOpeningTest(){

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        var isSplash = false

        activityScenario.onActivity {
            PreferenceManager.getDefaultSharedPreferences(it).apply{
                isSplash = getBoolean("make_splash",false)
            }
        }

        if(!isSplash) workWithSplash()

    }

    @Test
    fun workWithSplash(){

        val splashScenario = ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.register_start)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavigation(){

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open()) //если закрыт - то открыаем его

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_upload))

        onView(withId(R.id.upload_url)).check(matches(isDisplayed()))

    }

}