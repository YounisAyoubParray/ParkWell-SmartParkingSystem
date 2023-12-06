package com.example.smartparking;

public class Bookingdetail {
    public String SlotNumber,UserId,Entry,Exit,Date, Payment, VehcileNumber, VehcileType,PaymentId,Bookingtime,Er;

    public Bookingdetail(){}
    public Bookingdetail(String SlotNumber,String UserId, String Entry, String Exit,String Date, String Payment,String VehcileNumber,String VehcileType,String Bookingtime,String Er,String PaymentId){
        this.SlotNumber=SlotNumber;
        this.UserId=UserId;
        this.Entry=Entry;
        this.Exit=Exit;
        this.Date= Date;
        this.Payment=Payment;
        this.VehcileNumber=VehcileNumber;
        this.VehcileType=VehcileType;
        this.Bookingtime=Bookingtime;
        this.Er=Er;
        this.PaymentId=PaymentId;
    }
}
