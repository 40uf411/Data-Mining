package com.PartieA;

import java.util.ArrayList;

public class bin {
    float maxbin,minbin;
    ArrayList<Float> elem;

    public  bin(float max , float min ){
        maxbin = max;
        minbin=min;
        elem = new ArrayList<Float>();
    }
    public String  toString(){
        String s ="";
        for(int i = 0;i<elem.size();i++){
            s = s+ " "+elem.get(i);
        }

    return (">>> "+ s+" >>>");
    }
}
