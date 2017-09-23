package com.example.heojuyeong.foodandroid.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by heojuyeong on 2017. 9. 19..
 */

public class IntentUtil {

    public static void startActivity(Context context,Class t){
        Intent intent=new Intent(context,t);
        context.startActivity(intent);
    }



}
