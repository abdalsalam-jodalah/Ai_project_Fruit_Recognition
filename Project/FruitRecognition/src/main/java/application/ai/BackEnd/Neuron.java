package application.ai.BackEnd;

public class Neuron {
    int numberOfInput;
    int numberOfOutput;
    double[] InputsWeight;
    double[] OutputsWeight;
    double threshold;


    Neuron(int numberOfInput,int numberOfOutput){
        this.numberOfInput=numberOfInput;
        this.numberOfOutput=numberOfOutput;
        this.InputsWeight =new double[numberOfInput];
        this.OutputsWeight =new double[numberOfOutput];
    }
}
