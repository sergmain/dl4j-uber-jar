package com.xxx;

import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.StringTokenizer;

@SpringBootApplication
public class ModelLoader implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ModelLoader.class, args);
    }

    public static final String csvFull = "1,0,0,2778.0,0.0,0.0,994.0,0.0,2899.0,1429.0,1863.0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,";
    public static final String csv = "2778.0,0.0,0.0,994.0,0.0,2899.0,1429.0,1863.0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,";
    public static final String csv1 = "875.0,0.0,994.0,0.0,153.0,0.0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,";
    public static final String csv2 = "469.0,0.0,1561.0,834.0,2506.0,89.0,1113.0,0.0,0.0,2288.0,0.0,0.0,0.0,0.0,2499.0,1476.0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,";

    public void run(String... args) throws IOException, InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {


        MultiLayerNetwork model = KerasModelImport.importKerasSequentialModelAndWeights("data\\model-op.h5");

        predict(model, csv);
        predict(model, csv1);
        predict(model, csv2);
    }

    private void predict(MultiLayerNetwork model, String csv) {
        int total = 0;
        for (int i = 0; i < csv.length(); i++) {
            if (csv.charAt(i)==',') total++;
        }
        System.out.println("total = " + total);

        INDArray oneDZeros = Nd4j.zeros(total+1);

        StringTokenizer st = new StringTokenizer(csv, ",");
        int idx=0;
        for ( ;st.hasMoreTokens(); ) {
            double d = Double.parseDouble(st.nextToken());
            oneDZeros.putScalar(idx++, d);
        }
        print("oneDZeros", oneDZeros);

//        int[] predict = network.predict(oneDZeros);
//        System.out.println("predict = " + Arrays.toString(predict));
        INDArray result = model.output(oneDZeros);

        print("result", result);
    }


    private static void print(String tag, INDArray arr) {
        System.out.println("----------------");
        System.out.println(tag + ":\n" + arr.toString());
        System.out.println("----------------");
    }

    private static void print(String tag, INDArray [] arrays) {
        System.out.println("----------------");
        System.out.println(tag);
        for (INDArray array : arrays) {
            System.out.println("\n" + array);
        }
        System.out.println("----------------");
    }

}

