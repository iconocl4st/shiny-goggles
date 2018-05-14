package org.hallock.dota.control.others;

import org.tensorflow.*;

import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;


/*
https://github.com/tahaemara/object-recognition-tensorflow/blob/master/src/main/java/com/emaraic/ObjectRecognition/Recognizer.java
 */
public class TestTensorFlow {

    public static void test() throws UnsupportedEncodingException {
//        try (Graph g = new Graph()) {
//            final String value = "Hello from " + TensorFlow.version();
//
//            // Construct the computation graph with a single operation, a constant
//            // named "MyConst" with a value "value".
//            try (Tensor t = Tensor.create(value.getBytes("UTF-8"))) {
//                // The Java API doesn't yet include convenience functions for adding operations.
//                g.opBuilder("Const", "MyConst").setAttr("dtype", t.dataType()).setAttr("value", t).build();
//            }
//
//            // Execute the "MyConst" operation in a Session.
//            try (Session s = new Session(g);
//                 Tensor output = s.runner().fetch("MyConst").run().get(0)) {
//                System.out.println(new String(output.bytesValue(), "UTF-8"));
//            }
//        }
    }

//
//    private static byte[] getBytes(BufferedImage image) {
//        int totalPixels = image.getWidth() * image.getHeight();
//        byte[] bytes = new byte[totalPixels * 4];
//
//        int k = 0;
//        for (int i=0;i<image.getWidth();i++) {
//            for (int j=0;j<image.getHeight();j++) {
//                int color = image.getRGB(i, j);
//                bytes[k++] = (byte)((color >>  0) & 0xff);
//                bytes[k++] = (byte)((color >>  8) & 0xff);
//                bytes[k++] = (byte)((color >> 16) & 0xff);
//                bytes[k++] = (byte)((color >> 24) & 0xff);
//            }
//        }
//
//        return bytes;
//    }
//
//
//
//    private static int maxIndex(float[] probabilities) {
//        int best = 0;
//        for (int i = 1; i < probabilities.length; ++i) {
//            if (probabilities[i] > probabilities[best]) {
//                best = i;
//            }
//        }
//        return best;
//    }
//
//    public static void labelImage(BufferedImage image) {
//        byte[] imageBytes = getBytes(image);
//
//        try (Tensor image = Tensor.create(imageBytes)) {
//            float[] labelProbabilities = executeInceptionGraph(graphDef, image);
//            int bestLabelIdx = maxIndex(labelProbabilities);
//            System.out.println("The best was " + bestLabelIdx);
//        }
//    }
//
//    private static Tensor<Float> constructAndExecuteGraphToNormalizeImage(byte[] imageBytes) {
//        try (Graph g = new Graph()) {
//            GraphBuilder b = new GraphBuilder(g);
//            // Some constants specific to the pre-trained model at:
//            // https://storage.googleapis.com/download.tensorflow.org/models/inception5h.zip
//            //
//            // - The model was trained with images scaled to 224x224 pixels.
//            // - The colors, represented as R, G, B in 1-byte each were converted to
//            //   float using (value - Mean)/Scale.
//            final int H = 224;
//            final int W = 224;
//            final float mean = 117f;
//            final float scale = 1f;
//
//            // Since the graph is being constructed once per execution here, we can use a constant for the
//            // input image. If the graph were to be re-used for multiple input images, a placeholder would
//            // have been more appropriate.
//            final Output<String> input = b.constant("input", imageBytes);
//            final Output<Float> output =
//                    b.div(
//                            b.sub(
//                                    b.resizeBilinear(
//                                            b.expandDims(
//                                                    b.cast(b.decodeJpeg(input, 3), Float.class),
//                                                    b.constant("make_batch", 0)),
//                                            b.constant("size", new int[] {H, W})),
//                                    b.constant("mean", mean)),
//                            b.constant("scale", scale));
//            try (Session s = new Session(g)) {
//                // Generally, there may be multiple output tensors, all of them must be closed to prevent resource leaks.
//                return s.runner().fetch(output.op().name()).run().get(0).expect(Float.class);
//            }
//        }
//    }
//
//    private static float[] executeInceptionGraph(byte[] graphDef, Tensor image) {
//        try (Graph g = new Graph()) {
//            g.importGraphDef(graphDef);
//            try (Session s = new Session(g);
//                 Tensor result = s.runner().feed("DecodeJpeg/contents", image).fetch("softmax").run().get(0)) {
//                final long[] rshape = result.shape();
//                if (result.numDimensions() != 2 || rshape[0] != 1) {
//                    throw new RuntimeException(
//                            String.format(
//                                    "Expected model to produce a [1 N] shaped tensor where N is the number of labels, instead it produced one with shape %s",
//                                    Arrays.toString(rshape)));
//                }
//                int nlabels = (int) rshape[1];
//                return result.copyTo(new float[1][nlabels])[0];
//            }
//        }
//    }
}
