package com.example.fooddeuk;

import org.junit.Test;

import java.util.Scanner;

/**
 * Example local unit network, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    public void service() {
        try {

        } catch (Exception e) {

        }


    }


    @Test
    public void test() {
       final int dollr=1100;
        Scanner scanner=new Scanner(System.in);
        int won=scanner.nextInt();

        double result=(double)won/dollr;
        System.out.print(result);




    }

}



