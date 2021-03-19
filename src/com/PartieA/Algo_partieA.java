package com.PartieA;

import com.dataMining.Api;
import com.dataMining.Result;

import java.util.*;

public class Algo_partieA {
    // fonction de simple descretisation
    public String Simplediscretisation(int attributeIndex,int NombreMorceau){
        Result r = Api.data();
        float[][] data = r.data();
        float[] Atr = data[attributeIndex];
        Arrays.sort(Atr);
        float max = Atr[Atr.length-1];
        float min = Atr[0];
        float lenthOfbin = (max - min) / NombreMorceau;
        ArrayList<bin> Bins = new ArrayList();

        for(int i=0;i<NombreMorceau;i++){
            min = max-lenthOfbin;
            bin b = new bin(max,min);
            max = min;
            Bins.add(b);
        }

        for (int i=0;i<Atr.length;i++){
            for(int j=0;j<Bins.size();j++){
                if(Atr[i]>=Bins.get(j).minbin && Atr[i]<=Bins.get(j).maxbin){
                    Bins.get(j).elem.add(Atr[i]);
                }
            }
        }
        String resulta="";
         for(int i=0;i<Bins.size();i++){
            resulta = resulta + Bins.get(i).toString()+"\n";
        }
    return resulta;
    }

    public HashSet<ArrayList<String>> aprioriGenerator(HashSet<ArrayList<String>> listIn){

        HashSet<ArrayList<String>> listOutput = new HashSet<ArrayList<String>>();
        ArrayList<String> newItem;
        Object tab[] =(Object[])listIn.toArray();

        for(int i=0;i<tab.length-1;i++) {
            for(int j=i+1; j<tab.length;j++) {
                //recuperer les deux listes L1 et L2
                ArrayList<String> L1 = (ArrayList<String>) tab[i];
                ArrayList<String> L2 = (ArrayList<String>) tab[j];

                boolean b = true;
                newItem = new ArrayList<String>();

                for(int k=0;k<L1.size()-1;k++) {
                    if(!L1.get(k).equals(L2.get(k))) {b=false; break;}
                    newItem.add(L1.get(k));
                }
                if(b==true) { // les element de L1 et L2 sont egaux jusqu'a taille-1
                    if(L1.size()>=1) {
                        newItem.add(L1.get(L1.size()-1));
                        newItem.add(L2.get(L2.size()-1));
                        //ajouter le nouveau item a notre hashset
                        Collections.sort(newItem); // il faut le trie tous d'abord
                        listOutput.add(newItem);
                    }
                }
            }
        }
        return listOutput;
    }
    public Object[] regle_association (String min_support,String min_confidence){
        int minSupp = Integer.parseInt(min_support);
        double minConf = Double.parseDouble(min_confidence);
        minConf = minConf/100;
        Result r = Api.data();
        float[][] data = r.data();
        // deviser la data en 6 list de meme lengeur , chaque list une cologne de la dataset
        float[] laclass = data[0];
        float[] T1 = data[1];
        float[] T2 = data[2];
        float[] T3 = data[3];
        float[] T4 = data[4];
        float[] T5 =  data[5];
        //  partie creation transaction
        ArrayList<Transaction> ListTransation = new ArrayList<Transaction>();
       for (int i=0 ;i<laclass.length;i++){
           ArrayList<String> itemList = new ArrayList<String>();
           // la premier cologne :  les Class
           if(laclass[i]==1){  itemList.add("normal"); }
           if(laclass[i]==2){  itemList.add("hyper"); }
           if(laclass[i]==3){  itemList.add("hypo"); }

           // la 2eme Cologne : T3-resin uptake test. (A percentage) [ 65 , 144]
           if (T1[i] <=90  && T1[i]>60 ){   itemList.add("RT1"); }
           if (T1[i] <=110  && T1[i]>90 ){   itemList.add("RT2"); }
           if (T1[i] <=120  && T1[i]>110 ){   itemList.add("RT3"); }
           if (T1[i] <=150  && T1[i]>120 ){   itemList.add("RT4"); }
           // la 3eme cologne :  Total Serum thyroxin as measured by the isotopic  displacement method.  [0.5,25.3]
           if (T2[i] <=5  && T2[i]>0 ){   itemList.add("ST1"); }
           if (T2[i] <=9  && T2[i]>5 ){   itemList.add("ST2"); }
           if (T2[i] <=11  && T2[i]>9 ){   itemList.add("ST3"); }
           if (T2[i] <=15  && T2[i]>11 ){   itemList.add("ST4"); }
           if (T2[i] <=19  && T2[i]>15 ){   itemList.add("ST5"); }
           if (T2[i] <=26  && T2[i]>19 ){   itemList.add("ST6"); }
           // la 4eme cologne :	Total serum triiodothyronine as measured by radioimmun assay. [0.3 , 10]
           if(T3[i] <=3 && T3[i]>0 ) {itemList.add("STR1"); }
           if(T3[i] <=6 && T3[i]>3 ) { itemList.add("STR2"); }
           if(T3[i] <=12 && T3[i]>6 ) { itemList.add("STR3"); }
           // la 5eme cologne : basal thyroid-stimulating hormone (TSH) as measured by radioimmuno assay. [0.1 , 56.4]
           if(T4[i] <=10 && T4[i]>0 ) {itemList.add("TSH1"); }
           if(T4[i] <=20 && T4[i]>10 ) {itemList.add("TSH2"); }
           if(T4[i] <=30 && T4[i]>20 ) {itemList.add("TSH3"); }
           if(T4[i] <=60 && T4[i]>30 ) {itemList.add("TSH4"); }
           // la 5eme cologne Maximal absolute difference of TSH value after injection of 200 micro grams of
           // thyrotropin-releasing hormone as compared to the basal value. [ -0.7 , 56.3]
           if(T5[i] <=10 && T5[i]>-1 ) {itemList.add("DTA1"); }
           if(T5[i] <=40 && T5[i]>10 ) {itemList.add("DTA2"); }
           if(T5[i] <=60 && T5[i]>40 ) {itemList.add("DTA3"); }
           ListTransation.add(new Transaction(itemList));
       }
       // listTransation Contien tous les instant apres la Transformation
        HashSet<String> items = new HashSet<String>();
        HashSet<ArrayList<String>> itemsPris = new HashSet<ArrayList<String>>();
        HashSet<ArrayList<String>> frequentItemsets = new HashSet<ArrayList<String>>(); //pour garder l'historique
        HashMap<ArrayList<String>, Integer> listItemFrequence = new HashMap<ArrayList<String>, Integer>();
        HashMap<ArrayList<String>, Float> regleAssociation = new HashMap<ArrayList<String>, Float>(); //pour garder les regles d'association

    //debut d'execution
    long startTime = System.currentTimeMillis();

    //recuperer les itemset de longueur 1 (premier niveau)
    for(int i =0 ;i<ListTransation.size();i++) {
        for(String s : ListTransation.get(i).getItemList()) {
            items.add(s);
        }
    }

    //calculer les supports des items
    for(String item : items) {
        ArrayList<String> temp = new ArrayList<String>();
        int cpt=0;
        for(int i =0 ;i<ListTransation.size();i++) {
            if(ListTransation.get(i).getItemList().contains(item)) {
                cpt++;
            }
        }
        if(cpt>=minSupp) { //on prend l'item
            temp.add(item);
            itemsPris.add(temp);
            listItemFrequence.put(temp, cpt);
        }
    }

    //sauvgrader le 1er niveau
    frequentItemsets.addAll(itemsPris);

        //construction d'autres niveaux
    while(!itemsPris.isEmpty()) {
        itemsPris = aprioriGenerator(itemsPris);
        //supprimer les itemSets qui ne verifie pas la condition de min support
        HashSet<ArrayList<String>> itemsPrisNew = new HashSet<ArrayList<String>>();
        for(ArrayList<String> listItems : itemsPris) {
            int cpt=0;
            for(int i =0 ;i<ListTransation.size();i++) {
                if(ListTransation.get(i).getItemList().containsAll(listItems)) { //si la transaction contient tous l'itemset
                    cpt++;
                }
            }
            if(cpt>=minSupp) { //on prend l'item
                itemsPrisNew.add(listItems);
                listItemFrequence.put(listItems, cpt);
            }
        }
        //mettre a jour de liste
        itemsPris = itemsPrisNew;
        //ajouter a la hashset global
        if(!itemsPris.isEmpty()) {
            frequentItemsets.addAll(itemsPris);
        }
    }
        //Regle d'association
        for(ArrayList<String> a:frequentItemsets) {
            for(ArrayList<String> b : frequentItemsets) {
                if(a.containsAll(b)) {
                    float confidence;
                    float var1 = listItemFrequence.get(a);
                    float var2 = listItemFrequence.get(b);
                    confidence= var1/var2;
                    if(confidence>=minConf) { //ajouter la regle d'association
                        ArrayList<String> temp = new  ArrayList<String>();
                        if(!a.equals(b) ) {
                            temp.addAll(b);
                            temp.add("=>");
                            for(String s : a) {
                                if(!b.contains(s)) {
                                    temp.add(s);
                                }
                            }
                            regleAssociation.put(temp,confidence);
                        }
                    }
                }
            }
        }
        //le temps d'execution estime
        long endTime = System.currentTimeMillis();
        double duration = endTime - startTime;

        //fin d'algo
        Object[] obj = new Object[3];

        obj[0] = frequentItemsets;
        obj[1] = regleAssociation;
        obj[2] = duration;

        return obj;
}// fin regle association
    public void Algo_apriori(String min_support,String min_confidence,StringBuilder freqItemSet,
                             StringBuilder associationRule){
        Object[] obj = regle_association(min_support,min_confidence);
        HashSet<ArrayList<String>> frequentItemsets;
        frequentItemsets = (HashSet<ArrayList<String>>) obj[0];
        HashMap<ArrayList<String>, Float> regleAssociation;
        regleAssociation = (HashMap<ArrayList<String>, Float>) obj[1];



        freqItemSet.append("*******************Apriori Algorithme******************************:\n");
        freqItemSet.append("frequent itemset :\n");
        for(ArrayList<String> temp : frequentItemsets) {
            //affichage des items frequents
            if(temp.size()>1) {
                freqItemSet.append("\n");
                for(String s:temp) {
                    //Affichage

                    freqItemSet.append(s+" ");

                }

            }
        }
        associationRule.append("association Rules :\n");
        for(ArrayList<String> temp : regleAssociation.keySet()) {
            //affichage des regles d'associations
            associationRule.append("\n");
            for(String s:temp) {
                associationRule.append(s+" ");
            }
            associationRule.append(" ,  confidence :" + regleAssociation.get(temp));

        }

    }// fin algo apriorie

}
