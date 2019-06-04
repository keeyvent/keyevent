package demo.neuralrnn;

import org.junit.Test;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;

import static org.junit.Assert.*;

public class BasicPerceptronTest {

    @Test
    public void testBasicPerceptron() {
        NeuralNetwork neuralNetwork = new Perceptron(2, 1);
        DataSet trainingSet = new DataSet(2, 1);
        trainingSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
        trainingSet.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
        trainingSet.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
        trainingSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{1}));
        neuralNetwork.learn(trainingSet);
        neuralNetwork.save("or_perceptron.nnet");

        NeuralNetwork nnw = NeuralNetwork.load("or_perceptron.nnet");
        nnw.setInput(1, 1);
        nnw.calculate();
        double[] networkOutput = nnw.getOutput();

        assertNotNull(networkOutput);
        assertEquals(1, networkOutput.length);
        assertEquals(1.0, networkOutput[0], 0.001);
    }
}
