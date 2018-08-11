package com.xxx;

import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

@Service
public class ModelService {

    private MultiLayerNetwork model;

    @PostConstruct
    public void init() throws UnsupportedKerasConfigurationException, IOException, InvalidKerasConfigurationException {

        File tempDir = new File("./target");
        File modelFile = File.createTempFile("model-", ".h5", tempDir);
        try (FileOutputStream fos = new FileOutputStream(modelFile); InputStream is = ModelService.class.getResourceAsStream("/tiny-model-op.h5")) {
            org.apache.commons.compress.utils.IOUtils.copy(is, fos, 100000);
        }

        model = KerasModelImport.importKerasSequentialModelAndWeights(modelFile.getAbsolutePath());
    }

    void predict(String csv) {
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

        INDArray result = model.output(oneDZeros);

        print("result", result);
    }


    private static void print(String tag, INDArray arr) {
        System.out.println("----------------");
        System.out.println(tag + ":\n" + arr.toString());
        System.out.println("----------------");
    }
}

