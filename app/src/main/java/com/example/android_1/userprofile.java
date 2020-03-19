package com.example.android_1;

public class userprofile {
    public String age,mail,name;
    public userprofile(){

    }
    public userprofile(String a,String m,String n){
        this.age=a;
        this.mail=m;
        this.name=n;
    }

    public String getAge() {
        return age;
    }

    public String getMail() {
        return mail;
    }

    public String getName() {
        return name;
    }
}
