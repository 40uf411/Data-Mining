package com.dataMining;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Api {
    private static RushB core;

    public static void init(){
        if (core == null)
            core = new RushB();
    }

    public static Result readFile(String fileName){
        return core.readFile(fileName);
    }

    public static Result data() {
        return core.data();
    }
    public static Result dimensions(){
        return core.dimensions();
    }
    public static Result max(int attributeIndex) {
        return core.max(attributeIndex);
    }
    public static Result min(int attributeIndex) {
        return core.min(attributeIndex);
    }
    public static Result mean(int attributeIndex) {
        return core.mean(attributeIndex);
    }
    public static Result median(int attributeIndex) {
        return core.median(attributeIndex);
    }
    public static Result mode(int attributeIndex) {
        return core.mode(attributeIndex);
    }
    public static Result q1(int attributeIndex) {
        return core.q1(attributeIndex);
    }
    public static Result q3(int attributeIndex) {
        return core.q3(attributeIndex);
    }
    public static Result frequencies() {
        return core.frequencies();
    }
    public static Result uniqueValues() {
        return core.uniqueValues();
    }
    public static Result frequencyData() {
        return core.frequencyData();
    }


    public static void main(String[] args) {
        /*init();
        Result r = readFile("E:\\study\\2021\\data mining\\Projet datamining 2020-2021\\Thyroid_Dataset.txt");
        Result f = frequencies();
        Result e = max(1);
        System.out.println(e.content());*/

        Scanner sc = new Scanner(System.in);
        System.out.println("chemin  de dataset");

        System.out.println("1..pour k_means\n 2..pour k_med\n");
        int j = sc.nextInt();
        if(j==1)
        {
            k_means km=new k_means();
            List res=k_means.k_mean_algo(3, 4);
            //res.get(0)===>cluster
            //res.get(1)===>wcss
            //res.get(2)===>f_score

            Map<float[], Integer> clusters=(Map<float[], Integer>) res.get(0);
            double wcss=(double) res.get(1);
            float f_score=(float) res.get(2);
            System.out.println("\nFinal Clustering of Data");
            System.out.println("ATT 1  \t ATT 2\t ATT 3\t ATT 4\t ATT 5\t ATT 6\t  Cluster");
            for (float[] key : clusters.keySet()) {
                for (int i = 0; i < key.length; i++) {
                    System.out.print(key[i] + "\t \t");
                }
                System.out.print(clusters.get(key) + "\n");
            }
            System.out.println("\n************Results************\n");
            System.out.println("WCSS: "+wcss);
            System.out.println("F_Score: "+f_score);
        } else if(j==2) {
            k_med kmed=new k_med();
            List res=kmed.k_med_algo(3, 4);
            //res.get(0)===>cluster
            //res.get(1)===>wcss
            //res.get(2)===>f_score
            Map<float[], Integer> clusters=(Map<float[], Integer>) res.get(0);
            double wcss=(double) res.get(1);
            float f_score=(float) res.get(2);
            System.out.println("\nFinal Clustering of Data");
            System.out.println("ATT 1  \t ATT 2\t ATT 3\t ATT 4\t ATT 5\t ATT 6\t  Cluster");
            for (float[] key : clusters.keySet()) {
                for (int i = 0; i < key.length; i++) {
                    System.out.print(key[i] + "\t \t");
                }
                System.out.print(clusters.get(key) + "\n");
            }
            System.out.println("\n************Results************\n");
            System.out.println("WCSS: "+wcss);
            System.out.println("F_Score: "+f_score);
        } else if(j==3) {

        }
    }
}