package com.example.fooddeuk;

import com.example.fooddeuk.http.UserService;
import com.example.fooddeuk.model.user.UserItem;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Test
    public void test() {
        float rating=(float)3.5;
        for(int j=0; j<5; j++){
            if(rating==0){
                System.out.println("노별");
            }else if(rating>=1.0){
                rating-=1.0;
                System.out.println("별");
            }else if(rating==0.5){
                System.out.println("반별");
                rating-=0.5;

            }
//            System.out.println(rating);
        }



    }


    public void service(){
        try {
            UserItem userItem=UserService.getUser("33").execute().body();

        }catch (Exception e){

        }


    }

}



