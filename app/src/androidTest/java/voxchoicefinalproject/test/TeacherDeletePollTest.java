package voxchoicefinalproject.test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.voxchoicefinalproject.R;
import com.example.voxchoicefinalproject.TeacherActivity;
import com.example.voxchoicefinalproject.TeacherLogin;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TeacherDeletePollTest {

    @Before
    public void setUp() {
        ActivityScenario.launch(TeacherActivity.class);
    }

    @Test
    public void testTeacherLoginAndPoll() throws InterruptedException {

        Thread.sleep(2000);
        // Verify that the poll exists
        Espresso.onView(ViewMatchers.withText("What is your favorite color?"))
                .check(matches(isDisplayed()));

        // Delete the created poll
        Espresso.onView(withId(R.id.btnDelete))
                .perform(click());

        Thread.sleep(2000);

        // Verify that the poll is deleted successfully
        Espresso.onView(ViewMatchers.withText("What is your favorite color?"))
                .check(ViewAssertions.doesNotExist());
    }
}
