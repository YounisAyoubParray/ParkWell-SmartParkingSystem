package com.example.smartparking;

public class UnbanObj {
    public String UnBanTime,TotalFine,TransactionId, UserId;

    public UnbanObj(){}
    public UnbanObj(String UnBanTime, String TotalFine, String TransactionId, String UserId ){
        this.UnBanTime=UnBanTime;
        this.TotalFine=TotalFine;
        this.TransactionId= TransactionId;
        this.UserId=UserId;
    }
}
