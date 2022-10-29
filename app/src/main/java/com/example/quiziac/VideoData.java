package com.example.quiziac;

public class VideoData {
    private  String name,url;

    public VideoData(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public VideoData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
