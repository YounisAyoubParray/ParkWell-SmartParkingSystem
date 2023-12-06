package com.example.smartparking;



public class User {
    public String FullName,Email,Phone,Address;
    public int Type;

    public User(){}
    public User(String FullName,String Email, String Phone,String Address,int Type){
        this.FullName=FullName;
        this.Email=Email;
        this.Phone=Phone;
        this.Address= Address;
        this.Type=Type;
    }
}
