package com.example.smartparking;

public class BanObj {
    public String BanTime,ReportedBy,Dated,ParkingName,SlotNumber;

    public BanObj(){}
    public BanObj(String BanTime, String ReportedBy,String Dated, String ParkingName ,String SlotNumber){
        this.BanTime=BanTime;
        this.ReportedBy=ReportedBy;
        this.Dated= Dated;
        this.ParkingName=ParkingName;
        this.SlotNumber=SlotNumber;
    }
}
