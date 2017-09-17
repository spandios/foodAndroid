package com.example.heojuyeong.foodandroid;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by heojuyeong on 2017. 9. 16..
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class testMAin  {
    @Rule
    public ActivityTestRule<CartActivity> mActivityRule = new ActivityTestRule(CartActivity.class);

    @Test
    public void listGoesOverTheFold() {

        onView(withText("장바구니")).check(matches(isDisplayed()));
    }

}
