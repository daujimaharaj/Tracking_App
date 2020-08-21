package com.example.tracking_app;

import android.net.Uri;

public class create_user {

    public String email,password,confrm_password,firstname,lstname,gender,imageurl,code,userid,isharing,lat,lng;

    public create_user(){

    }

    public String getUserid() {
        return userid;
    }

    public create_user(String email, String password, String confrm_password, String firstname, String lstname, String gender,
                       String imageurl, String code, String userid, String isharing,String lat,String lng) {
        this.email = email;
        this.password = password;
        this.confrm_password = confrm_password;
        this.firstname = firstname;
        this.lstname = lstname;
        this.gender=gender;
        this.imageurl=imageurl;
         this.code=code;
         this.userid=userid;
         this.isharing=isharing;
         this.lat=lat;
         this.lng=lng;

    }

}
