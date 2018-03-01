package com.example.fooddeuk.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by heojuyeong on 2017. 9. 19..
 */

public class IntentUtil {

    public static void startActivity(Context context,Class t){
        Intent intent=new Intent(context,t);
        context.startActivity(intent);
    }
    public static void startActivity(Context context,Class t,Bundle extra){
        Intent intent=new Intent(context,t);
        intent.putExtras(extra);
        context.startActivity(intent);
    }



}
