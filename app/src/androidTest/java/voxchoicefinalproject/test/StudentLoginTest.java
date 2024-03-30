package voxchoicefinalproject.test;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import com.example.voxchoicefinalproject.R;
import com.example.voxchoicefinalproject.StudentLogin;

public class StudentLoginTest {

    @Rule
    public ActivityScenarioRule<StudentLogin> activityScenarioRule =
            new ActivityScenarioRule<>(StudentLogin.class);

    @Test
    public void testStudentLoginAndViewPolls() {
        ActivityScenario<StudentLogin> scenario = activityScenarioRule.getScenario();

        Espresso.onView(ViewMatchers.withId(R.id.studentEmailInput))
                .perform(ViewActions.typeText("bob@gmail.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.studentPasswordInput))
                .perform(ViewActions.typeText("qwertyuiop"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.studentLoginButton)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.toolbar))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
