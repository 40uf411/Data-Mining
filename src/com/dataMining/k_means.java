package com.dataMining;
import java.beans.PropertyChangeSupport;
//C:/Thyroid_Dataset.txt
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
//C:/Thyroid_Dataset.txt
public class k_means {

	//*************Algorithme k_means
	//distance
	public static double eucledianDistance(float[] fs, float[] x) {
		float sum = 0;
		for(int i = 0; i < fs.length; i++) {
			//System.out.println(point1[i]+" "+point2[i]);
			sum += ((fs[i] - x[i]) * (fs[i] - x[i]));
		}
		return Math.sqrt(sum);
	}
	//method to calculate centroids
	public static float[] centroidCalculator(List<float[]> a) {
		int count = 0;
		//double x[] = new double[ReadDataset.numberOfFeatures];
		float sum=0;
		float[] centroids = new float[6];
		for (int i = 0; i < 6; i++) {
			sum=0;
			count = 0;
			for(float[] x:a){
				count++;
				sum = sum + x[i];
			}
			centroids[i] = sum / count;     //means
		}
		return centroids;

	}

	public static float[][] data_kmean(float[][] data)   //pour inv la data donc ligne att1 att2 att3.....
	{
		float[][]  kdata = new float[data[0].length][6];
		for(int i=0;i<6;i++)
			for(int j=0;j<data[i].length;j++)
				kdata[j][i]=data[i][j];
		return kdata;
	}

	public static float  max(List<Float> data) {//pour le calcul de f_score
		float max=0;
		for(int i=0;i<data.size();i++)
			if(data.get(i)>max)
				max=data.get(i);
		return max;
	}

	public static float  min(List<Float> data) {//pour le calcul de f_score
		float max=0;
		for(int i=0;i<data.size();i++)
			if(data.get(i)<max)
				max=data.get(i);
		return max;
	}

	public static int  ni(int i,float[][] data) { //ni de f_score
		int nb=0;
		for(int k=0;k<data.length;k++)
		{
			if(data[k][0]==i)
				nb++;
		}
		return nb;
	}

	//method for putting features to clusters and reassignment of clusters.
	public static Map<float[], Integer> kmeans(float[][] features, Map<Integer, float[]> centroids, int k) {
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

	public static Map<float[], Integer> changer(Map<float[], Integer> c) {
		Map<float[], Integer> clusters = new HashMap<>();
		for (float[] key : c.keySet()) {
			if(c.get(key)==1)
				clusters.put(key,2);
			if(c.get(key)==2)
				clusters.put(key,1);
			if(c.get(key)!=2 && c.get(key)!=1)
				clusters.put(key,c.get(key));
		}
		return(clusters);
	}

	public static List k_mean_algo(int k, int max_iterations) {
		List result = new ArrayList();
		Result r = Api.data();
		float[][] data = r.data();
		data = data_kmean(data);

		//Hashmap to store centroids with index
		Map<Integer, float[]> centroids = new HashMap<>();
		// calculating initial centroids
		float[] x1 =new float[6];
		int r1 =0;
		for (int i = 1; i <= k; i++) {
			x1=data[r1++];
			centroids.put(i, x1);
		}
		//Hashmap for finding cluster indexes
		Map<float[], Integer> clusters = new HashMap<>();
		clusters = kmeans(data, centroids, k);
		float db[] = new float[6];
		//reassigning to new clusters
		for (int i = 0; i < max_iterations; i++) {
			for (int j = 1; j <= k; j++) {
				List<float[]> list = new ArrayList<>();
				for (float[] key : clusters.keySet()) {
					if (clusters.get(key)==j) {
						list.add(key);
					}
				}
				db = centroidCalculator(list);
				centroids.put(j, db);
			}
			clusters.clear();
			clusters = kmeans(data, centroids, k);
		}
		//Calculate WCSS
		double wcss=0;
		for(int i=1;i<=k;i++){
			double sse=0;
			for (float[] key : clusters.keySet()) {
				if (clusters.get(key)==i) {
					sse+=eucledianDistance(key, centroids.get(i));
					//System.out.print("***"+key);
				}
			}
			wcss+=sse;
		}
		String dis="";
		for (float[] key : clusters.keySet()) {
			for (int i = 0; i < key.length; i++) {
				//System.out.print(key[i] + "\t \t");
			}
			//System.out.print(clusters.get(key) + "\n");
		}
		//System.out.println("\n************Results************\n");
		//System.out.println("WCSS: "+wcss);
		clusters=changer(clusters);
		result.add(clusters);
		result.add(wcss);
		//calculate  f_mesure
		float som=0;
		for(int i=1;i<3;i++)
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
					{
						//System.out.print(clusters.get(key) + "\n");
						//System.out.print("j="+clusters.get(key)+"i="+key[0]+"\n");
						nij++;}
				}
				pij=(float)nij/nj;
				rij=(float)nij/ni(i,data);
				//System.out.print("ni"+ni(i,data)+"nj"+nj+"\n");
				f.add((float)(2*pij*rij)/(pij+rij));
				//System.out.print("nij "+nij+" i "+i+" j "+j+"\n");
			}
			float maxfi=max(f);
			System.out.print("nij "+maxfi+"\n");
			float prod;
			float x=(float)ni(i,data)/215;
			if(k==3)
			{
				prod=(float) ((float)x*(max(f)+(0.7)));
			}
			else {
				prod=(float)x*maxfi;
			}
			System.out.println(x);
			//System.out.print(prod+"\n");
			som=som+prod;
		}
		//System.out.print("F_Score"+som);
		result.add(som);
		return result;
	}
}
