package com.example.heojuyeong.foodandroid;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<CartActivity> activityActivityTestRule=new ActivityTestRule<CartActivity>(CartActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        TextView textView=(TextView)activityActivityTestRule.getActivity().findViewById(R.id.cartClear);
        String test=textView.getText().toString();
        assertEquals(test,"클리어 test");
    }
}
