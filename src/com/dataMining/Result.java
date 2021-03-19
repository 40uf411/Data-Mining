package com.dataMining;

import java.util.ArrayList;

public class Result {
    private int code;
    private String title;
    private String content;
    private int[] dim;
    private float[][] data;
    private ArrayList[] dynamicData;

    public Result(int code, String title, String content){
        this.code = code;
        this.title = title;
        this.content = content;
    }

    public Result(int code, String title, String content, int[] dim){
        this.code = code;
        this.title = title;
        this.content = content;
        this.dim = dim;
    }

    public Result(int code, String title, String content, float[][] data){
        this.code = code;
        this.title = title;
        this.content = content;
        this.data = data;
    }

    public Result(int code, String title, String content, ArrayList[] dynamicData){
        this.code = code;
        this.title = title;
        this.content = content;
        this.dynamicData = dynamicData;
    }

    public int code() {
        return code;
    }

    public String title() {
        return title;
    }

    public String content() {
        return content;
    }

    public int[] dim() {
        return dim;
    }

    public float[][] data() {
        return data;
    }

    public ArrayList[] dynamicData() {
        return dynamicData;
    }
}
