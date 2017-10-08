package com.example.fooddeuk;

import com.example.fooddeuk.http.UserService;
import com.example.fooddeuk.model.user.UserItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Test
    public void test() {
//        service();
    String a="[\n" +
            "    \"{\\\"arrivedTime\\\":\\\" 13:23\\\",\\\"cartItems\\\":[{\\\"menu\\\":{\\\"menu_avgtime\\\":\\\"30분\\\",\\\"menu_name\\\":\\\"치킨\\\",\\\"menu_id\\\":49,\\\"menu_price\\\":0},\\\"menu_count\\\":\\\"1\\\",\\\"option\\\":[{\\\"menu_option_category_name\\\":\\\"사이즈\\\",\\\"menu_option_name\\\":\\\"L\\\",\\\"menu_option_price\\\":\\\"+3,000원\\\",\\\"menu_option_id\\\":2},{\\\"menu_option_category_name\\\":\\\"양념종류\\\",\\\"menu_option_name\\\":\\\"양념2\\\",\\\"menu_option_price\\\":\\\"+2,000원\\\",\\\"menu_option_id\\\":6}],\\\"totalPrice\\\":\\\"5,000원\\\",\\\"id\\\":0}],\\\"completePrice\\\":\\\"5,000원\\\",\\\"create_at\\\":\\\"2017-23-07 01:23:42\\\",\\\"request\\\":\\\"\\\",\\\"rest_admin_id\\\":\\\"3\\\",\\\"status\\\":\\\"주문수락대기\\\",\\\"rest_id\\\":273,\\\"user_id\\\":24,\\\"order_id\\\":\\\"17\\\"}\",\n" +
            "    \"{\\\"arrivedTime\\\":\\\" 13:23\\\",\\\"cartItems\\\":[{\\\"menu\\\":{\\\"menu_avgtime\\\":\\\"30분\\\",\\\"menu_name\\\":\\\"치킨\\\",\\\"menu_id\\\":49,\\\"menu_price\\\":0},\\\"menu_count\\\":\\\"1\\\",\\\"option\\\":[{\\\"menu_option_category_name\\\":\\\"사이즈\\\",\\\"menu_option_name\\\":\\\"L\\\",\\\"menu_option_price\\\":\\\"+3,000원\\\",\\\"menu_option_id\\\":2},{\\\"menu_option_category_name\\\":\\\"양념종류\\\",\\\"menu_option_name\\\":\\\"양념2\\\",\\\"menu_option_price\\\":\\\"+2,000원\\\",\\\"menu_option_id\\\":6}],\\\"totalPrice\\\":\\\"5,000원\\\",\\\"id\\\":0}],\\\"completePrice\\\":\\\"5,000원\\\",\\\"create_at\\\":\\\"2017-28-07 01:28:01\\\",\\\"request\\\":\\\"\\\",\\\"rest_admin_id\\\":\\\"3\\\",\\\"status\\\":\\\"주문수락대기\\\",\\\"rest_id\\\":273,\\\"user_id\\\":24,\\\"order_id\\\":\\\"18\\\"}\"\n" +
            "]";
        try{
            JSONArray jsonObject= new JSONArray(a);
            jsonObject.get(0);
            System.out.print(jsonObject.get(0));
        }catch (JSONException e){
            e.printStackTrace();
        }


    }


    public void service(){
        try {
            UserItem userItem=UserService.getUser("33").execute().body();

        }catch (Exception e){

        }


    }

}



