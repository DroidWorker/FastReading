package com.selfdev.fastreading.Objects;

public class Info {
    String header;
    String text;

    public Info(String header, String text)
    {
        this.header = header;
        this.text = text;
    }

    public String getHeader() {
        return header;
    }

    public String getText() {
        return text;
    }
}
