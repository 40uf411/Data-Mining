package com.dataMining;

public class FrequencyData implements Comparable<FrequencyData> {
    public float num;
    public int frequency;
    FrequencyData(float num, int frequency) {
        this.frequency = frequency;
        this.num = num;
    }
    @Override
    public int compareTo(FrequencyData o) {
        if (this.num > o.num)
            return 1;
        else if (this.num < o.num)
            return -1;
        return 0;
    }
}
