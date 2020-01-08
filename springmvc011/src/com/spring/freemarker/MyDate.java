package com.spring.freemarker;

/*define a class*/
class MyDate{
    String  Year;
    String  Month;
    String  Day;
    String  Date;
    public MyDate(){
        Year = "1900";
        Month ="01";
        Day = "01";
        Date = Year + "." + Month + "."+ Day;
    }
     public String toString(){
        return Date;
    }   
}