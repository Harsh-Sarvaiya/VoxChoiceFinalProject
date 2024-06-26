package voxchoicefinalproject.test;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.voxchoicefinalproject.R;
import com.example.voxchoicefinalproject.StudentActivity;
import com.example.voxchoicefinalproject.StudentLogin;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;



@RunWith(AndroidJUnit4.class)
public class StudentVotePollTest {

    @Before
    public void setUp() {
        ActivityScenario.launch(StudentActivity.class);
    }

    @Test
    public void testVoteInPoll() throws InterruptedException {
        // Ensure StudentActivity is navigated to
        Espresso.onView(ViewMatchers.withId(R.id.toolbar))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Thread.sleep(2000);


        // Ensure poll exists
        Espresso.onView(ViewMatchers.withText("What is your favorite color?"))
                .perform(ViewActions.click());

        Thread.sleep(2000);

        // Vote on poll
        Espresso.onView(ViewMatchers.withText("Color 1"))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.buttonVote))
                .perform(ViewActions.click());

        Thread.sleep(2000);

        // Ensure vote has been cast and we are back at StudentActivity
        Espresso.onView(withId(R.id.toolbar))
                .check(matches(isDisplayed()));
    }
}
