package demo.neuralrnn.nnw;

import org.neuroph.nnet.MultiLayerPerceptron;

public class TradeBidNetWork extends MultiLayerPerceptron {
    public TradeBidNetWork(int... neuronsInLayers) {
        super(neuronsInLayers);
    }
}
