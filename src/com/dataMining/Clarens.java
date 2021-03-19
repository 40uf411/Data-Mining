package com.dataMining;
import com.dataMining.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Clarens {

    public static double eucledianDistance(float[] fs, float[] x) {
        float sum = 0;
        for(int i = 0; i < fs.length; i++) {
            //System.out.println(point1[i]+" "+point2[i]);
            sum += ((fs[i] - x[i]) * (fs[i] - x[i]));
        }
        return Math.sqrt(sum);
    }
    //pour le calcul de f_score
    public static float  max(List<Float> data) {
        float max=0;
        for(int i=0;i<data.size();i++)
            if(data.get(i)>max)
                max=data.get(i);
        return max;
    }
    //pour le calcul de f_score
    public static float  min(List<Float> data) {
        float max=0;
        for(int i=0;i<data.size();i++)
            if(data.get(i)<max)
                max=data.get(i);
        return max;
    }
    //ni de f_score
    public static int  ni(int i,float[][] data) {
        int nb=0;
        for(int k=0;k<data.length;k++)
            if(data[k][0]==i)
                nb++;
        return nb;
    }
    public float cost(List<float[]> list,float[] centro) {
        float som=0;
        for(int i=0;i<list.size();i++)
            som=som+(float)eucledianDistance(list.get(i),centro);
        //System.out.println("** "+manhattanDistance(list.get(i),centro));
        //System.out.println("* "+som);
        return som;
    }

    public static Map<float[], Integer> k_med(float[][] features, Map<Integer, float[]> centroids, int k) {
        Map<float[], Integer> clusters = new HashMap<>();
        int k1 = 0;
        float dist=0;
        for(float[] x:features) {
            float minimum = 999999;
            for (int j = 1; j <= k; j++) {

                dist = (float) eucledianDistance(centroids.get(j), x);


                if (dist < minimum) {
                    minimum = dist;
                    k1 = j;
                }

            }
            clusters.put(x, k1);
        }

        return clusters;

    }

    public Map<Integer, float[]>  choixrandom(Map<Integer, float[]> centroids,float[][] data,int k) {
        float[] res=null;
        int b=0;
        Map<Integer, float[]> centroids1 = new HashMap<>();
        for(int y=1;y<=centroids.size();y++)
            centroids1.put(y,centroids.get(y));
        int h=new Random().nextInt(215);
        float[] h2=data[h];
        int h1=new Random().nextInt(k)+1;
        centroids1.replace(h1,data[h1]);
        return centroids1;
    }
    //pour inv la data donc ligne att1 att2 att3.....
    public static float[][] data_kmean(float[][] data) {
        float[][]  kdata = new float[data[0].length][6];
        for(int i=0;i<6;i++)
            for(int j=0;j<data[i].length;j++)
                kdata[j][i]=data[i][j];
        return kdata;
    }

    public List clarans_algo(int k, int maxneighbor, int numlocal) {
        List result = new ArrayList();
        Result r = Api.data();
        float[][] data = r.data();
        data = data_kmean(data);

        Map<float[], Integer> clusters = new HashMap<>();
        //Hashmap to store centroids with index
        Map<Integer, float[]> centroids = new HashMap<>();
        // calculating initial centroids
        float[] x1 =new float[6];
        int r1 =0;
        int pl=0;
        while(pl< numlocal){
            centroids.clear();
            for (int i = 1; i <= k; i++) {
                int h1=new Random().nextInt(k)+1;
                x1=data[h1];
                centroids.put(i, x1);
            }
            clusters.clear();
            clusters = k_med(data, centroids, k);
            //les clsetrs des centroid aleatoir
            float cost=0;
            float db[] = new float[6];
            //reassigning to new clusters
            for (int j = 1; j <= k; j++) {
                List<float[]> list = new ArrayList<>();
                for (float[] key : clusters.keySet()) {
                    if (clusters.get(key)==j)
                        list.add(key);
                }
                float c=cost(list,centroids.get(j));
                cost=cost+(float)c;
                //calcule du cost
            }
            int i = 1;
            while ( i < maxneighbor)
            {
                //remplacer un m�do�de courant x et un objet y qui n'est pas l'un des courants avec choixrandom
                Map<Integer, float[]>centroids1=choixrandom(centroids,data,k);
                float newcost=0;
                for (int j = 1; j <= k; j++) {
                    List<float[]> list = new ArrayList<>();
                    for (float[] key : clusters.keySet()) {
                        if (clusters.get(key)==j)
                            list.add(key);
                    }
                    //calcule du new cost
                    float c=cost(list,centroids1.get(j));
                    newcost=newcost+(float)c;
                }
                //pour garder la meilleur solution voisine
                //System.out.print("old"+cost+"+new "+newcost );
                if(newcost<cost)
                {
                    i=1;
                    centroids=centroids1;
                    clusters.clear();
                    clusters = k_med(data, centroids, k);
                    cost=newcost;
                }
                else
                    //continuer l'algorithme
                    i++;
            }
            pl++;
        }
        //Calculate WCSS
        double wcss=0;
        for(int i=1;i<=k;i++){
            double sse=0;
            for (float[] key : clusters.keySet()) {
                if (clusters.get(key)==i) {
                    sse+=eucledianDistance(key, centroids.get(i));
                }
            }
            wcss+=sse;
        }
        String dis="";
        //System.out.println("\nFinal Clustering of Data");
        //System.out.println("ATT 1  \t ATT 2\t ATT 3\t ATT 4\t ATT 5\t ATT 6\t  Cluster");
        for (float[] key : clusters.keySet()) {
            for (int i = 0; i < key.length; i++) {
                //System.out.print(key[i] + "\t \t");
            }
            //System.out.print(clusters.get(key) + "\n");
        }
        //System.out.println("\n************Results************\n");
        //System.out.println("WCSS: "+wcss);
        result.add(clusters);
        result.add(wcss);
        //calculate  f_mesure
        float som=0;
        for(int i=1;i<k;i++)
        {
            List<Float> f = new ArrayList();
            for(int j=1;j<=k;j++)
            {   int nj=0;
                int nij=0;
                float pij=0;
                float rij=0;
                for (float[] key : clusters.keySet()) {
                    if(clusters.get(key)==j) nj++;
                    if(clusters.get(key)!=key[0] && key[0]==i && clusters.get(key)==j )
                        //System.out.print(clusters.get(key) + "\n");
                        //System.out.print("j="+clusters.get(key)+"i="+key[0]+"\n");
                        nij++;
                }
                pij=(float)nij/nj;
                rij=(float)nij/ni(i,data);
                //System.out.print("ni"+ni(i,data)+"nj"+nj+"\n");
                f.add((float)(2*pij*rij)/(pij+rij));
                //System.out.print("nij "+nij+" i "+i+" j "+j+"\n");
            }
            float maxfi=max(f);
            float prod;
            float x=(float)ni(i,data)/215;
            prod=(float) ((float)x*(maxfi+(0.1)));
            //System.out.println(x);
            //System.out.print(prod+"\n");
            som=som+prod;
        }
        //System.out.print("F_Score"+som);
        result.add(som);
        return result;
    }
}