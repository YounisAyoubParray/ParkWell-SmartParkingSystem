package com.example.smartparking;

public class parkingname1 {
        public String parkinglotname1;
        public String parkcost1;
        public String dt;
        public String tas;

        public parkingname1(String parkinglotname1, String parkcost1,String dt,String tas){
            this.parkinglotname1=parkinglotname1;
            this.parkcost1=parkcost1;
            this.dt=dt;
            this.tas=tas;
        }

        public String getParkinglotname1()
        {
            return parkinglotname1;
        }
        public String getParkcost1()
        {
            return parkcost1;
        }
        public String getdt()
    {
        return dt;
    }
         public String gettas()
    {
        return tas;
    }



}
