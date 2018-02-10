package com.example.fooddeuk;

import com.example.fooddeuk.network.UserService;
import com.example.fooddeuk.model.user.UserItem;

import org.junit.Test;

import java.util.Scanner;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Test
    public void test() {
       final int dollr=1100;
        Scanner scanner=new Scanner(System.in);
        int won=scanner.nextInt();

        double result=(double)won/dollr;
        System.out.print(result);




    }


    public void service() {
        try {
            UserItem userItem = UserService.getUser("33").execute().body();

        } catch (Exception e) {

        }


    }

}



