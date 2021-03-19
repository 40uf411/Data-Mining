package com.dataMining;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RushB {
    private int[] dim = new int[2];
    private float[][] data;
    private ArrayList<Float>[] uniq_val;
    private ArrayList<Integer>[] frequency;
    private ArrayList<FrequencyData>[] entities;
    private float[][] sortedData;
    private boolean sorted = false;

    private void sortData() {
        float[] a;
        for (int i = 0; i < dim[0]; i++) {
            a = new float[dim[1]];
            for (int j = 0; j < a.length; j++) {
                a[j] = data[i][j];
            }
            Arrays.sort(a);
            sortedData[i] = a;
        }
        return;
    }

    private static List<String> readFileInList (String fileName) {
        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  lines;
    }

    private boolean parse(String fileName){
        List<String> lines = readFileInList(fileName);
        int elems = lines.size();
        String s = lines.get(0);
        String[] ss = s.split(",");
        int features = ss.length;
        dim[1] = elems;
        dim[0] = features;
        //frequency = new int[features];

        data = new float[features][elems];
        sortedData = new float[features][elems];
        uniq_val = new ArrayList[features];
        for (int i = 0; i < uniq_val.length; i++) {
            uniq_val[i] = new ArrayList<Float>();
        }
        frequency = new ArrayList[features];
        for (int i = 0; i < frequency.length; i++) {
            frequency[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < elems; i++) {
            s = lines.get(i);
            ss = s.split(",");
            for (int j = 0; j < ss.length; j++) {
                // save the value in data
                float e = Float.parseFloat(ss[j]);
                data[j][i] = e;

                // calculate unique values and frequency
                if( ! uniq_val[j].contains(e)){
                    uniq_val[j].add(e);
                    frequency[j].add(1);
                }
                else {
                    // get the id
                    int id = -1;
                    for (int k = 0; k < uniq_val[j].size(); k++) {
                        if(uniq_val[j].get(k) == e){
                            id = k;
                            break;
                        }
                    }
                    frequency[j].set(id, frequency[j].get(id)+1);
                }
            }
        }
        sortData();

        entities = new ArrayList[dim[0]];

        for (int i = 0; i < entities.length; i++) {
            entities[i] = new ArrayList<FrequencyData>();
            for (int j = 0; j < uniq_val[i].size(); j++) {
                entities[i].add(new FrequencyData(uniq_val[i].get(j), frequency[i].get(j)));
            }
            Collections.sort(entities[i]);
        }
        return true;
    }

    public Result readFile(String fileName) {
        if (parse(fileName))
            return new Result(200, "ok", "[!] The file '" + fileName + "' has been read successfully.", data);
        else
            return new Result(400, "error", "[*] Could not read the file '" + fileName + "'.");
    }

    public Result data() {
        return new Result(200, "ok", "", data);
    }
    public Result dimensions() {
        return new Result(200, "ok", "", dim);
    }

    private float getMax(int id) {
        return sortedData[id][sortedData[0].length-1];
    }
    public Result max(int attributeIndex){
        return new Result(200, "ok", String.valueOf(getMax(attributeIndex)));
    }

    private float getMin(int id) {
        return sortedData[id][0];
    }
    public Result min(int attributeIndex){
        return new Result(200, "ok", String.valueOf(getMin(attributeIndex)));
    }

    private float getMean(int id) {
        float c = 0;
        for (float i : data[id]) {
            c += i;
        }
        return c/dim[1];
    }
    public Result mean(int attributeIndex){
        return new Result(200, "ok", String.valueOf(getMean(attributeIndex)));
    }

    private float getMedian(int id) {
        return sortedData[id][(dim[1]/2)];
    }
    public Result median(int attributeIndex){
        return new Result(200, "ok", String.valueOf(getMedian(attributeIndex)));
    }

    private float getQ1(int id) {
        return sortedData[id][(dim[1]/4)];
    }
    public Result q1(int attributeIndex){
        return new Result(200, "ok", String.valueOf(getQ1(attributeIndex)));
    }

    private float getQ3(int id) {
        return sortedData[id][(dim[1]*3/4)];
    }
    public Result q3(int attributeIndex){
        return new Result(200, "ok", String.valueOf(getQ3(attributeIndex)));
    }

    private float getMode(int id) {
        float c = -1;
        float ide = -1;

        for (int i = 0; i < frequency[id].size(); i++) {
            if (frequency[id].get(i) > c) {
                c = frequency[id].get(i);
                ide = i;
            }
        }
        return ide;
    }
    public Result mode(int attributeIndex) {
        return new Result(200, "ok", String.valueOf(uniq_val[attributeIndex].get((int)getMode(attributeIndex))));
    }

    public Result frequencies() {
        return new Result(200, "ok", "", frequency);
    }

    public Result uniqueValues() {
        return new Result(200, "ok", "", uniq_val);
    }

    public Result frequencyData() {
        return new Result(200, "ok", "", entities);
    }
}