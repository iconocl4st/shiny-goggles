package org.hallock.dota.control.others;

import java.awt.image.BufferedImage;

public class TestSimpleNN {

    private static byte[] getBytes(BufferedImage image) {
        int totalPixels = image.getWidth() * image.getHeight();
        byte[] bytes = new byte[totalPixels * 4];

        int k = 0;
        for (int i=0;i<image.getWidth();i++) {
            for (int j=0;j<image.getHeight();j++) {
                int color = image.getRGB(i, j);
                bytes[k++] = (byte)((color >>  0) & 0xff);
                bytes[k++] = (byte)((color >>  8) & 0xff);
                bytes[k++] = (byte)((color >> 16) & 0xff);
                bytes[k++] = (byte)((color >> 24) & 0xff);
            }
        }

        return bytes;
    }

    public static void runSimpleNN() {

        float[][] x = new float[][] {
                {1, 2, 4},
                {1, 3, 6},
                {1, 56, 3},
                {1, 4, 4},
                {1, 67, 4},
                {2, 2, 5},
                {2, 6, 6},
                {2, 5, 2},
                {2, 2, 7},
                {2, 56, 9},
        };
        int[] t = new int[] {
                0,
                0,
                0,
                0,
                0,
                1,
                1,
                1,
                1,
                1
        };

        // a good one: https://github.com/narenkmanoharan/ImageNet-Classifier-Tensorflow/blob/master/alex_net.py
//
//        NeuralNetwork neuralNetwork = new NeuralNetwork(x, t, new INeuralNetworkCallback() {
//            @Override
//            public void success(Result result) {
//                float[] valueToPredict = new float[] {-0.205f, 0.780f};
//                System.out.println("Success percentage: " + result.getSuccessPercentage());
////                System.out.println("Predicted result: " + result.predictValue(valueToPredict));
//            }
//
//            @Override
//            public void failure(Error error) {
//                System.out.println("Error: " + error.getDescription());
//            }
//        });
//
//        neuralNetwork.startLearning();
    }
}
