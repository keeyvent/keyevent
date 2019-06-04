package demo.neuralrnn.nnw;

import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.random.WeightsRandomizer;

import java.util.Random;

public class BPPerceptron extends MultiLayerPerceptron {
    private  Long weight = 138L;
    public BPPerceptron(int... neuronsInLayers) {
        super(TransferFunctionType.SIGMOID, neuronsInLayers);
        setWeight(weight);

    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
        randomizeWeights(new WeightsRandomizer(new Random(weight)));
    }
}
