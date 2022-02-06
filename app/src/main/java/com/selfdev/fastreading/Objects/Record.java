package com.selfdev.fastreading.Objects;

public class Record {
    String nickname;
    int speed;
    int understanding;

    public Record (String name, int speed, int understanding){
        nickname = name;
        this.speed = speed;
        this.understanding = understanding;
    }

    public int getSpeed() {
        return speed;
    }

    public int getUnderstanding() {
        return understanding;
    }

    public String getNickname() {
        return nickname;
    }
}
