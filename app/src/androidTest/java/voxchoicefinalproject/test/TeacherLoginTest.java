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
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.voxchoicefinalproject.R;
import com.example.voxchoicefinalproject.TeacherLogin;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TeacherLoginTest {

    @Before
    public void setUp() {
        ActivityScenario.launch(TeacherLogin.class);
    }

    @Test
    public void testTeacherLoginAndPoll() throws InterruptedException {
        // Enter valid teacher credentials and log in
        Espresso.onView(withId(R.id.emailInput))
                .perform(ViewActions.typeText("ekam.taneja03@gmail.com"), closeSoftKeyboard());
        Espresso.onView(withId(R.id.passwordInput))
                .perform(ViewActions.typeText("wertyuiop"), closeSoftKeyboard());
        Espresso.onView(withId(R.id.loginButton))
                .perform(click());

        Thread.sleep(1000);

        Espresso.onView(withId(R.id.toolbar))
                .check(matches(isDisplayed()));

        Thread.sleep(1000);

        Espresso.onView(withId(R.id.btnCreatePoll))
                .perform(click());

        int numOptions = 3;
        Espresso.onView(withId(R.id.editTextNumOptions))
                .perform(replaceText(String.valueOf(numOptions)), closeSoftKeyboard());

        Thread.sleep(1000);

        Espresso.onView(withId(R.id.editTextNumOptions))
                .perform(click());

        Thread.sleep(1000);

        Espresso.onView(withId(R.id.editTextQuestion))
                .perform(ViewActions.typeText("What is your favorite color?"), closeSoftKeyboard());

        for (int i = 1; i <= numOptions; i++) {
            String optionHint = "Enter option " + i;
            Espresso.onView(withHint(optionHint))
                    .perform(ViewActions.typeText("Color " + i), closeSoftKeyboard());
        }

        Espresso.onView(withId(R.id.btnCreatePollFinal))
                .perform(click());

        Espresso.onView(ViewMatchers.withText("What is your favorite color?"))
                .check(matches(isDisplayed()));
    }
}
