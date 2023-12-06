package com.example.smartparking;



public class parkingname {
    public String parkinglotname;
    public String parkcost;

    public parkingname(String parkinglotname, String parkcost){
        this.parkinglotname=parkinglotname;
        this.parkcost=parkcost;
    }

    public String getParkinglotname()
    {
        return parkinglotname;
    }
    public String getParkcost()
    {
        return parkcost;
    }


}
