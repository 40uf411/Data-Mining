package com.dataMiningGUI;
import java.util.concurrent.TimeUnit;

import com.PartieA.Algo_partieA;
import com.dataMining.*;
import javafx.application.Application;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.embed.swing.SwingNode;
import org.jfree.ui.RefineryUtilities;

import javax.swing.SwingUtilities;

public class Main extends Application implements
        EventHandler<ActionEvent>{
    @FXML
    public Pane pnl_basic_info, pnl_dataset, pnl_graphs, pnl_boxplot, pnl_histogram, pnl_scatterplot, pnl_clustering, pnl_freqitm;
    @FXML
    public Button btn_basic_info, btn_dataset, btn_graphs, btn_boxplot, btn_attr_boxplot, btn_scatterplot, btn_histogram, btn_update_histo, button_scatter, btn_clustering, btn_runCluster, btn_freqItm, btn_runsd, btn_runap;
    @FXML
    public ChoiceBox select_attr;
    @FXML
    public TextArea intervaleSize;
    @FXML
    public Text txt_num_attr, txt_num_rows, txt_max, txt_min, txt_mean, txt_median, txt_mode, txt_q1, txt_q3, txt_unq_val, txt_sym, txt_fscore, txt_wcss, txt_execTime, txt_output;
    @FXML
    public TableView<float[]> db_table, cluster_table;
    @FXML
    private ScatterChart<?,?> scatter_plot;
    @FXML
    private BarChart<?,?> barchart_f;
    @FXML
    private ComboBox<Integer> combo_histo, att1, att2, combo_nbrcluster, combo_nbrit, combo_attrIndx, combo_nbrp, combo_nmlcl;
    @FXML
    private ComboBox<String> combo_method;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private Slider slider_sup, slider_conf;

    private String filePath = "";
    private boolean dataset_tab_col_init = false;
    private boolean cluster_tab_col_init = false;
    private static boolean attrInited = false;
    private Desktop desktop = Desktop.getDesktop();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Api.init();
        boolean inIntroWindow = true;
        Parent root = FXMLLoader.load(getClass().getResource("intro.fxml"));
        Scene s = new Scene(root);
        ScreenController screenController = new ScreenController(s);
        screenController.addScreen("main", FXMLLoader.load(getClass().getResource( "main.fxml" )));
        if (inIntroWindow) {
            final FileChooser fileChooser = new FileChooser();
            final Button openButton = (Button)  root.lookup("#pick-file-button");
            openButton.setOnAction(
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(final ActionEvent e) {
                            configureFileChooser(fileChooser);
                            File file = fileChooser.showOpenDialog(primaryStage);
                            filePath = file.getAbsolutePath();
                            System.out.println(filePath);
                            screenController.activate("main");
                            apiCalls(filePath);
                        /*if (file != null) {
                            openFile(file);
                        }*/
                        }
                    });
        }
        primaryStage.setTitle("Welcome to Data Mining TP!");
        primaryStage.setScene(s);
        primaryStage.show();
    }
    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Select dataset");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT", "*.txt")
        );
    }
    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                    Main.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(ActionEvent event) {
        if (!attrInited) {
            int ii = Api.dimensions().dim()[0];
            for (int i = 0; i < ii; i++) {
                select_attr.getItems().add(i);
                att1.getItems().add(i);
                att2.getItems().add(i);
                combo_histo.getItems().add(i);
                combo_attrIndx.getItems().add(i);
            }
            combo_method.getItems().add("K-Means");
            combo_method.getItems().add("K-Med");
            combo_method.getItems().add("Clarens");
            for (int i = 1; i < 10; i++) {
                combo_nbrcluster.getItems().add(i);
                combo_nbrit.getItems().add(i);
                combo_nbrp.getItems().add(i);
                combo_nmlcl.getItems().add(i);
            }
            attrInited = true;
        }
        if (event.getSource() == btn_basic_info) {
            basicInfo();
            pnl_basic_info.toFront();
        } else if (event.getSource() == btn_dataset) {
            view_dataset();
            pnl_dataset.toFront();
        } else if (event.getSource() == btn_graphs) {
            pnl_graphs.toFront();
        } else if (event.getSource() == btn_boxplot) {
            final SwingNode swingNode = new SwingNode();
            createSwingContent(swingNode, false, 0);
            pnl_boxplot.getChildren().add(swingNode);
        } else if (event.getSource() == btn_attr_boxplot) {
            final SwingNode swingNode = new SwingNode();
            createSwingContent(swingNode, true, (int) select_attr.getValue());
            pnl_boxplot.getChildren().add(swingNode);
        } else if (event.getSource() == btn_histogram) {
            update_histogram();
            pnl_histogram.toFront();
        }  else if (event.getSource() == btn_update_histo) {
        } else if (event.getSource() == btn_scatterplot) {
            pnl_scatterplot.toFront();
        } else if (event.getSource() == button_scatter) {
            scatterplot();
        } else if (event.getSource() == btn_clustering) {
            pnl_clustering.toFront();
            clustering();
        } else if (event.getSource() == btn_freqItm) {
            pnl_freqitm.toFront();
            freqItm();
        }

    }
    private void createSwingContent(final SwingNode swingNode, boolean modeAttr, int attr) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (modeAttr){
                    float[][] data = Api.data().data();
                    final test demo = new test("", data[attr]);
                    demo.pack();
                    RefineryUtilities.centerFrameOnScreen(demo);
                    demo.setVisible(true);
                } else {
                    float[][] data = Api.data().data();
                    final test demo = new test("", data);
                    demo.pack();
                    RefineryUtilities.centerFrameOnScreen(demo);
                    demo.setVisible(true);
                }
            }
        });
    }
    private void apiCalls(String filename) {
        Result r = Api.readFile(filename);
    }
    private void basicInfo() {
        Result r = Api.dimensions();
        txt_num_attr.setText(String.valueOf(r.dim()[0]));
        txt_num_rows.setText(String.valueOf(r.dim()[1]));
        int att = r.dim()[0];
        String s_max = "";
        for (int i = 0; i < att; i++) {
            r = Api.max(i);
            s_max = s_max + "A" + i + ": " + r.content();
            if (i != att-1)
                s_max = s_max + "\t║\t";
        }
        txt_max.setText(s_max);
        /******************************/
        String s_min = "";
        for (int i = 0; i < att; i++) {
            r = Api.min(i);
            s_min = s_min + "A" + i + ": " + r.content();
            if (i != att-1)
                s_min = s_min + "\t║\t";
        }
        txt_min.setText(s_min);
        /******************************/
        String s_mean = "";
        for (int i = 0; i < att; i++) {
            r = Api.mean(i);
            s_mean = s_mean + "A" + i + ": " + r.content();
            if (i != att-1)
                s_mean = s_mean + "\t║\t";
        }
        txt_mean.setText(s_mean);
        /******************************/
        String s_median = "";
        for (int i = 0; i < att; i++) {
            r = Api.median(i);
            s_median = s_median + "A" + i + ": " + r.content();
            if (i != att-1)
                s_median = s_median + "\t║\t";
        }
        txt_median.setText(s_median);
        /******************************/
        String s_mode = "";
        for (int i = 0; i < att; i++) {
            r = Api.mode(i);
            s_mode = s_mode + "A" + i + ": " + r.content();
            if (i != att-1)
                s_mode = s_mode + "\t║\t";
        }
        txt_mode.setText(s_mode);
        /******************************/
        String s_q1 = "";
        for (int i = 0; i < att; i++) {
            r = Api.q1(i);
            s_q1 = s_q1 + "A" + i + ": " + r.content();
            if (i != att-1)
                s_q1 = s_q1 + "\t║\t";
        }
        txt_q1.setText(s_q1);
        /******************************/
        String s_q3 = "";
        for (int i = 0; i < att; i++) {
            r = Api.q3(i);
            s_q3 = s_q3 + "A" + i + ": " + r.content();
            if (i != att-1)
                s_q3 = s_q3 + "\t║\t";
        }
        txt_q3.setText(s_q3);
        /******************************/
        String s_uv = "";

        r = Api.uniqueValues();
        for (int i = 0; i < att; i++) {
            s_uv = s_uv + "A" + i + ": " + r.dynamicData()[i].size();
            if (i != att-1)
                s_uv = s_uv + "\t║\t";
        }
        txt_unq_val.setText(s_uv);
        /******************************/
        String s_sym = "";

        r = Api.uniqueValues();
        for (int i = 0; i < att; i++) {
            String v = (
                    Float.parseFloat(Api.mean(i).content()) == Float.parseFloat(Api.mean(i).content()) &&
                    Float.parseFloat(Api.mean(i).content()) == Float.parseFloat(Api.mode(i).content())
            )? "Yes" : "No";
                s_sym = s_sym + "A" + i + ": " + v;
                if (i != att-1)
                    s_sym = s_sym + "\t║\t";
            }
            txt_sym.setText(s_sym);
            /******************************/
    }
    private void view_dataset() {
        if (!dataset_tab_col_init) {
            Result r = Api.dimensions();
            TableColumn<float[], Number>[] att  = new TableColumn[r.dim()[0]];
            for (int i = 0; i < r.dim()[0]; i++) {
                att[i] = new TableColumn<>("Attr " + i);
                int columnIndex = i ;
                att[i].setCellValueFactory(cellData -> {
                    float[] row = cellData.getValue();
                    return new SimpleFloatProperty(row[columnIndex]);
                });
                db_table.getColumns().addAll(att[i]);
            }
            dataset_tab_col_init = !dataset_tab_col_init;
            Result data = Api.data();
            float[] j;
            for (int i = 0 ; i < r.dim()[1] ; i++) {
                j = new float[r.dim()[0]];
                for (int k = 0; k < j.length; k++) {
                    j[k] = data.data()[k][i];
                }
                //db_table.getItems().add(data.data()[i]);
                db_table.getItems().add(j);
            }
        }
    }
    private void update_histogram() {
        btn_update_histo.setOnAction(e -> {
            barchart_f.setCategoryGap(0);
            barchart_f.setBarGap(0);
            barchart_f.getData().clear();
            int attI = combo_histo.getValue();
            ArrayList<FrequencyData> attr = Api.frequencyData().dynamicData()[attI];

            XYChart.Series series1 = new XYChart.Series();
            //xAxis.setAnimated(false);
            series1.setName("Attribute: " + attI);
            float intervale_size = Float.valueOf(intervaleSize.getText());
            float start_val;
            float end_val;
            int intvlFreq;
            int currentIntvSize = 0;
            for (int counter = 0; counter < attr.size(); counter++) {
                start_val = attr.get(counter).num;
                end_val = attr.get(counter).num;
                intvlFreq = attr.get(counter).frequency;
                currentIntvSize = 1;
                for (int i = counter+1; i < attr.size(); i++) {
                    if (attr.get(i).num < start_val + intervale_size) {
                        end_val = attr.get(i).num;
                        intvlFreq += attr.get(i).frequency;
                        currentIntvSize ++;
                        counter = i;
                    }
                    else {
                        break;
                    }
                }
                if (currentIntvSize == 1) {
                    series1.getData().add(new XYChart.Data<String, Number>(String.valueOf(start_val), intvlFreq));
                } else {
                    series1.getData().add(new XYChart.Data<String, Number>(start_val + "-" + end_val, intvlFreq));
                }
            }
            barchart_f.getData().addAll(series1);
        });
    }
    private void scatterplot() {
        scatter_plot.getData().clear();
        int at1 = att1.getValue();
        int at2 = att2.getValue();
        XYChart.Series series = new XYChart.Series();
        series.setName("Attribute 1: '" + at1 + "' & Attribute 2: '" + at2 + "'");
        for(int i=0;i<215;i++)
        {
            series.getData().add(new XYChart.Data (Api.data().data()[at1][i],Api.data().data()[at2][i] ));
        }
        scatter_plot.getData().add(series);
    }
    public void remove_plot() {
        scatter_plot.getData().clear();
        barchart_f.getData().clear();
    }
    private void clustering() {
        Result r = Api.dimensions();
        if (!cluster_tab_col_init) {
            TableColumn<float[], Number>[] att  = new TableColumn[r.dim()[0]+1];
            for (int i = 0; i < r.dim()[0]; i++) {
                int f = 5;
                att[i] = new TableColumn<>("Attr " + i);
                int columnIndex = i ;
                att[i].setCellValueFactory(cellData -> {
                    float[] row = cellData.getValue();
                    return new SimpleFloatProperty(row[columnIndex]);
                });
                cluster_table.getColumns().addAll(att[i]);
            }
            int ind = r.dim()[0];
            att[ind] = new TableColumn<>("Cluster");
            int columnIndex = ind;
            att[ind].setCellValueFactory(cellData -> {
                float[] row = cellData.getValue();
                return new SimpleFloatProperty(row[columnIndex]);
            });
            cluster_table.getColumns().addAll(att[ind]);

            Result data = Api.data();
            float[] j;
            for (int i = 0 ; i < r.dim()[1] ; i++) {
                int f = 5;
                j = new float[r.dim()[0] + 1];
                for (int k = 0; k < j.length - 1; k++) {
                    j[k] = data.data()[k][i];
                }
                j[j.length-1] = -1;
                cluster_table.getItems().add(j);
            }
            cluster_tab_col_init = !cluster_tab_col_init;
        }
        btn_runCluster.setOnAction(e -> {
            String method = combo_method.getValue();
            int nbrClusters = Integer.valueOf(combo_nbrcluster.getValue());
            int nbrItr = Integer.valueOf(combo_nbrit.getValue());
            int numlocal = Integer.valueOf(combo_nmlcl.getValue());

            List res = new ArrayList();

            long startTime = System.nanoTime();
            if (method.equals("K-Means")) {
                k_means km = new k_means();
                res = k_means.k_mean_algo(nbrClusters, nbrItr);
            } else if (method.equals("K-Med")) {
                k_med kmed=new k_med();
                res=kmed.k_med_algo(nbrClusters, nbrItr);
            } else {
                Clarens kmed=new Clarens();
                res=kmed.clarans_algo(nbrClusters, nbrItr, numlocal);
            }
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
            Map<float[], Integer> clusters=(Map<float[], Integer>) res.get(0);
            double wcss=(double) res.get(1);
            float f_score=(float) res.get(2);

            txt_fscore.setText(String.valueOf(f_score));
            txt_wcss.setText(String.valueOf(wcss));
            txt_execTime.setText("" + (timeElapsed / 1000000) + " milliseconds | " + timeElapsed + " nanoseconds");

            cluster_table.getItems().clear();
            float[] j;
            for (float[] key : clusters.keySet()) {
                j = new float[r.dim()[0] + 1];
                for (int i = 0; i < key.length; i++) {
                    j[i] = key[i];
                }
                j[j.length-1] = clusters.get(key);
                cluster_table.getItems().add(j);
            }
        });
    }
   private  void freqItm() {
       btn_runsd.setOnAction(e -> {
           final Algo_partieA pa = new Algo_partieA();
           int attrIndex = combo_attrIndx.getValue();
           int nbrP = combo_nbrp.getValue();
           txt_output.setText(pa.Simplediscretisation(attrIndex, nbrP));

       });
       btn_runap.setOnAction(e -> {
           int support = (int) slider_sup.getValue();
           int confidence = (int) slider_conf.getValue();

           StringBuilder freqItemSets = new StringBuilder("");
           StringBuilder associationRules=new StringBuilder("");

           if( support <= 100 && support >=0 && confidence <= 100 && confidence >=0 ){
               final Algo_partieA pa = new Algo_partieA();
               pa.Algo_apriori(String.valueOf(support),String.valueOf(confidence),freqItemSets,associationRules);
               String out="";
               out  = out +"\n"+freqItemSets+"\n" +associationRules;
               txt_output.setText(out);
           }
       });
   }

    private ObservableList<dataRep> getData() {
        Result r = Api.data();

        ObservableList<dataRep> data = FXCollections.observableArrayList();

        float[] j;
        for (int i = 0; i < r.data()[0].length; i++) {
            j = new float[r.data().length];
            for (int k = 0; k < j.length; k++) {
                j[k] = r.data()[k][i];
            }
            data.add(new dataRep(j));
        }
        return data;
    }
    private class dataRep{
        private float[] data;
        public dataRep(float[] data){
            this.data = data;
        }
        public float[] getData() {
            return data;
        }
    }
}
