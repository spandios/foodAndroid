package com.example.fooddeuk.util

import org.joda.time.*
import java.util.*

/**
 * Created by heo on 2018. 2. 25..
 */

object TimeUtil{
    fun time(time : String) : DateTime = DateTime(time)

    fun mul(time : Int) : Int = time*-1




     fun currentBetweenTime(createdAt : String) : String{
         val period= Period(DateTime(Date()), time(createdAt))
         if(period.years!=0){
             return "${-1*period.years}년 전"
         }else if(period.months!=0){
             return "${-1*period.months}달 전"
         }else if(period.days!=0){
             return "${-1*period.days}일 전"
         }else if(period.hours!=0){
             return "${-1*period.hours}시간 전"
         }else{
             return "${-1*period.seconds}초 전"
         }

     }


    fun betwwenMonth(startTime : DateTime, endTime : DateTime) : Int{
        val result =Months.monthsBetween((startTime.toLocalDate()), endTime.toLocalDate())
        return result.months
    }

    fun betwwenMonth(startTime : String, endTime : String) : Int{
        val result =Months.monthsBetween((DateTime(startTime).toLocalDate()), DateTime(endTime).toLocalDate())
        return result.months
    }


    fun betweenCurrentDays(endTime : DateTime) : Int{
        val result =Days.daysBetween(DateTime(Date()).toLocalDate(), endTime.toLocalDate())
        return result.days
    }

    fun betwwenDays(startTime : DateTime, endTime : DateTime) : Int{
        val result =Days.daysBetween(startTime.toLocalDate(), endTime.toLocalDate())
        return result.days
    }

    fun betwwenDays(startTime : String, endTime : String) : Int{
        val result =Days.daysBetween(DateTime(startTime).toLocalDate(), DateTime(endTime).toLocalDate())
        return result.days
    }

    fun betweenCurrentHour(endTime : DateTime) : Int{
        val result =Hours.hoursBetween(DateTime(Date()).toLocalDate(), endTime.toLocalDate())
        return result.hours
    }

    fun betwwenHour(startTime : DateTime, endTime : DateTime) : Int{
        val result =Hours.hoursBetween(startTime.toLocalDate(), endTime.toLocalDate())
        return result.hours
    }

    fun betwwenHour(startTime : String, endTime : String) : Int{
        val result =Hours.hoursBetween(DateTime(startTime).toLocalDate(), DateTime(endTime).toLocalDate())
        return result.hours
    }



    fun betweenCurrentMinute(endTime : DateTime) : Int{
        val result =Minutes.minutesBetween(DateTime(Date()).toLocalDate(), endTime.toLocalDate())
        return result.minutes
    }

    fun betweenMinute(startTime : DateTime, endTime : DateTime) : Int{
        val result =Minutes.minutesBetween(startTime.toLocalDate(), endTime.toLocalDate())
        return result.minutes
    }

    fun betweenMinute(startTime : String, endTime : String) : Int{
        val result =Minutes.minutesBetween(DateTime(startTime).toLocalDate(), DateTime(endTime).toLocalDate())
        return result.minutes
    }


    fun betweenCurrentSecond(endTime : DateTime) : Int{
        val result =Seconds.secondsBetween(DateTime(Date()).toLocalDate(), endTime.toLocalDate())
        return result.seconds
    }

    fun betweenSecond(startTime : DateTime, endTime : DateTime) : Int{
        val result =Seconds.secondsBetween(startTime.toLocalDate(), endTime.toLocalDate())
        return result.seconds
    }

    fun betweenSecond(startTime : String, endTime : String) : Int{
        val result =Seconds.secondsBetween(DateTime(startTime).toLocalDate(), DateTime(endTime).toLocalDate())
        return result.seconds
    }



}