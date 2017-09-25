package com.example.heojuyeong.foodandroid;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.heojuyeong.foodandroid.activity.MainActivity;

import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * Created by heojuyeong on 2017. 9. 20..
 */
@RunWith(AndroidJUnit4.class)
public class ExpressoTest{
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);




}
