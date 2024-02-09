package application.ai.BackEnd;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class NeuralNetwork {
    Neuron inputSweetnessNeuron;
    Neuron inputColorNeuron;
    Neuron outputApple;
    Neuron outputOrange;
    Neuron outputBanana;
    public double crossEntropyTraining;
    public double crossEntropyTest;
    public  ArrayList<Fruit>  readDataFromFile(String filePath){

        ArrayList<Fruit> fruitsList=new ArrayList<>();

        try {

            Scanner scanner = new Scanner(new File(filePath));

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                String []data = line.split(" ");
                String name = data[0];

                String sweetness = data[1];
                String color = data[2];

                if(data[2].equals("red"))color="1";
                else if(data[2].equals("orange"))color="2";
                else if(data[2].equals("yellow"))color="3";

                if(data[0].equals("apple"))name="1 0 0";
                else if(data[0].equals("orange"))name="0 1 0";
                else if(data[0].equals("banana"))name="0 0 1";
                Fruit fruit = new Fruit(name,sweetness,color);
                fruitsList.add(fruit);
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            e.printStackTrace();
        }
        return fruitsList;

    }
    private double getRandomWeight(int input) {
        double minValue = -(2.4 / input);
        double maxValue = (2.4 / input);
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        double value=(minValue + (maxValue - minValue) * Math.random());
        String dble=decimalFormat.format(value);
        value=Double.parseDouble(dble);
        return value;
    }
    public ArrayList<Neuron> createNeural(int numberOfPerceptron){
        //--------------------------- creating neural network---------------------------//
        // ---------------------------input layer---------------------------//
        inputSweetnessNeuron =new Neuron(1,numberOfPerceptron);
        inputColorNeuron =new Neuron(1,numberOfPerceptron);
        //---------------------------output layer---------------------------//
        outputApple =new Neuron(numberOfPerceptron,1);
        outputOrange =new Neuron(numberOfPerceptron,1);
        outputBanana =new Neuron(numberOfPerceptron,1);

        outputApple.threshold=getRandomWeight(numberOfPerceptron);
        outputOrange.threshold=getRandomWeight(numberOfPerceptron);
        outputBanana.threshold=getRandomWeight(numberOfPerceptron);
        //-----------------------creating hidden Layer---------------------//
        ArrayList<Neuron> hiddenList=new ArrayList<>();
        for(int i = 0;i < numberOfPerceptron;i++){

            Neuron perceptron=new Neuron(2,3);
                int input=2;

                perceptron.threshold=getRandomWeight(input);

                inputSweetnessNeuron.OutputsWeight[i] = getRandomWeight(input);
                inputColorNeuron.OutputsWeight[i] =getRandomWeight(input);
                perceptron.InputsWeight[0] = inputSweetnessNeuron.OutputsWeight[i];
                perceptron.InputsWeight[1] = inputColorNeuron.OutputsWeight[i];

                input=numberOfPerceptron;
                perceptron.OutputsWeight[0] = getRandomWeight(input);
                perceptron.OutputsWeight[1] =getRandomWeight(input);
                perceptron.OutputsWeight[2] = getRandomWeight(input);
                outputApple.InputsWeight[i] = perceptron.OutputsWeight[0];
                outputOrange.InputsWeight[i] = perceptron.OutputsWeight[1];
                outputBanana.InputsWeight[i] = perceptron.OutputsWeight[2];

            hiddenList.add(perceptron);
        }

        return hiddenList;

    }
    private double activation(double x){
      return   1.0 / (1.0 + Math.exp(-x));
    }

    public double size;
    public int returnedEpoch=0;
    public void trainData(String filePath , int numberOfPerceptron , double learningRate,String activationFunction, int epoch,double goal) {
        ArrayList<Fruit> fruitList = readDataFromFile(filePath);
        ArrayList<Neuron> hiddenList = createNeural(numberOfPerceptron);
        boolean flagReturen=false;
        crossEntropyTraining=0;
        trainingAcc=0;
        trueData=0;
        size=fruitList.size();
        int acc=0;
        for(int ephocs = 0;ephocs<epoch;ephocs++){

        for (int i = 0; i < fruitList.size(); i++) {



                ArrayList<Double> YaInput = new ArrayList<>();
                ArrayList<Double> YaOutput = new ArrayList<>();
                ArrayList<Double> Weights = new ArrayList<>();
                ArrayList<Double> Weights1 = new ArrayList<>();
                ArrayList<Double> InputWeights = new ArrayList<>();
                inputSweetnessNeuron.InputsWeight[0] = Integer.parseInt(fruitList.get(i).sweetness);
                inputColorNeuron.InputsWeight[0] = Integer.parseInt(fruitList.get(i).color);

              //calculate Ya for input  step 1



                for (int j = 0; j < hiddenList.size(); j++) {

                    double ya = ((inputSweetnessNeuron.InputsWeight[0] * hiddenList.get(j).InputsWeight[0]) + (inputColorNeuron.InputsWeight[0] * hiddenList.get(j).InputsWeight[1])) - hiddenList.get(j).threshold;
                    if(activationFunction.equals("sigmoid")) {
                        ya = activation(ya);
                    }
                    else if(activationFunction.equals("tanh")){
                        ya=activationTanh(ya);
                    }
                    else if(activationFunction.equals("relu"))
                    {
ya=relu(ya);
                    }

                    YaInput.add(ya);

                }

            //calculate Ya for output
                // step 2
                  double ya=0;

                    for (int k = 0; k < hiddenList.size(); k++) {

                        ya = ya + (outputApple.InputsWeight[k] * YaInput.get(k));
                    }
            ya=  ya-outputApple.threshold;


            YaOutput.add(ya);
             ya=0;

            for (int k = 0; k < hiddenList.size(); k++) {

                ya = ya + (outputOrange.InputsWeight[k] * YaInput.get(k));
            }
            ya=  ya-outputOrange.threshold;

YaOutput.add(ya);

            ya=0;

            for (int k = 0; k < hiddenList.size(); k++) {

                ya = ya + (outputBanana.InputsWeight[k] * YaInput.get(k));
            }
            ya=  ya-outputBanana.threshold;


                    YaOutput.add(ya);
        ArrayList <Double> soft=new ArrayList<>();
        for(int j=0;j<YaOutput.size();j++) {
        double x=0;
    x=Math.exp(YaOutput.get(j))/(Math.exp(YaOutput.get(0))+Math.exp(YaOutput.get(1))+Math.exp(YaOutput.get(2)));
    soft.add(x);
}




                  String[]data=fruitList.get(i).name.split(" ");

                //step 3



            double[] myArray = {0.0, 0.0, 0.0};
            if(soft.get(0)>soft.get(1)&&soft.get(0)>soft.get(2) ){
             myArray[0]=1.0;

            }
            else   if(soft.get(1)>soft.get(0)&&soft.get(1)>soft.get(2)){

                 myArray[1]=1.0;
            }
            else   if(soft.get(2)>soft.get(0)&&soft.get(2)>soft.get(1)){
                myArray[2]=1.0;

            }




                double Error1 = Integer.parseInt(data[0]) - myArray[0];
                double Error2 = Integer.parseInt(data[1]) - myArray[1];
                double Error3 = Integer.parseInt(data[2]) - myArray[2];

            if(soft.get(0)>soft.get(1)&&soft.get(0)>soft.get(2)&&fruitList.get(i).name.equals("1 0 0") ){
               acc++;
            }
            else   if(soft.get(1)>soft.get(0)&&soft.get(1)>soft.get(2) &&fruitList.get(i).name.equals("0 1 0") ){
               acc++;
            }
            else   if(soft.get(2)>soft.get(0)&&soft.get(2)>soft.get(1)&&fruitList.get(i).name.equals("0 0 1") ){
             acc++;
            }


            String[] data1 = fruitList.get(i).name.split(" ");

            double[] trueProbabilities = {Double.parseDouble(data1[0]), Double.parseDouble(data1[1]), Double.parseDouble(data1[2])};

            // Calculate cross entropy
            for (int j = 0; j < trueProbabilities.length; j++) {
                crossEntropyTraining += trueProbabilities[j] * Math.log(soft.get(j) + 1e-10);
            }
            double crossEntropy=-crossEntropyTraining/(size*ephocs);
            returnedEpoch=ephocs;


            double Weight1 = 0;
            double Weight2=0;
            double Weight3 =0;
            //step 4
if(activationFunction.equals("sigmoid")) {
     Weight1 = YaOutput.get(0) * (1 - YaOutput.get(0)) * Error1;
     Weight2 = YaOutput.get(1) * (1 - YaOutput.get(1)) * Error2;
     Weight3 = YaOutput.get(2) * (1 - YaOutput.get(2)) * Error3;

}
else if (activationFunction.equals("tanh")){
     Weight1 = tanhDerivative(YaOutput.get(0)) * Error1;
     Weight2 = tanhDerivative(YaOutput.get(1)) * Error2;
     Weight3 = tanhDerivative(YaOutput.get(2)) * Error3;



}
else if(activationFunction.equals("relu"))
{
    Weight1 = reluDerivative(YaOutput.get(0)) * Error1;
    Weight2 = reluDerivative(YaOutput.get(1)) * Error2;
    Weight3 = reluDerivative(YaOutput.get(2)) * Error3;
}
            Weights.add(Weight1);
            Weights.add(Weight2);
            Weights.add(Weight3);


                //step 5

                for (int j = 0; j < hiddenList.size(); j++) {

                    for (int k = 0; k < hiddenList.get(j).numberOfOutput; k++) {
                        hiddenList.get(j).OutputsWeight[k] = learningRate * YaInput.get(j) * Weights.get(k);


                    }
                    outputApple.threshold = (learningRate * outputApple.threshold * Weight1)+outputApple.threshold;
                    outputOrange.threshold = (learningRate * outputOrange.threshold * Weight2)+outputOrange.threshold;
                    outputBanana.threshold = (learningRate * outputBanana.threshold * Weight3)+ outputBanana.threshold;


                    //note


                }


                //step 6


                for(int j = 0; j < hiddenList.size() ; j ++){
                    double w=0;
                     w=(outputApple.InputsWeight[j]*Weights.get(0))+(outputOrange.InputsWeight[j]*Weights.get(1))+(outputBanana.InputsWeight[j]*Weights.get(2));

                     Weights1.add(w);
                }

                for (int j = 0; j < hiddenList.size(); j++) {
                    if(activationFunction.equals("sigmoid")) {
                        double inputWeight = YaInput.get(j) * (1 - YaInput.get(j)) * Weights1.get(j);
                        InputWeights.add(inputWeight);
                    }
                    else if(activationFunction.equals("tanh")){
                        double inputWeight = tanhDerivative(YaInput.get(j)) * Weights1.get(j);
                        InputWeights.add(inputWeight);
                    }
                    else if(activationFunction.equals("relu")){
                        double inputWeight = reluDerivative(YaInput.get(j)) * Weights1.get(j);
                        InputWeights.add(inputWeight);
                    }


                }


            //step 7
                //update on output for input values

                for (int j = 0; j < numberOfPerceptron; j++) {
                    inputSweetnessNeuron.OutputsWeight[j] = (learningRate * Integer.parseInt(fruitList.get(i).sweetness) * InputWeights.get(j)) + inputSweetnessNeuron.OutputsWeight[j];
                    hiddenList.get(j).threshold=(learningRate*hiddenList.get(j).threshold*InputWeights.get(j))+hiddenList.get(j).threshold;
                }
                for (int j = 0; j < numberOfPerceptron; j++) {
                    inputColorNeuron.OutputsWeight[j] = (learningRate * Integer.parseInt(fruitList.get(i).color) * InputWeights.get(j)) + inputColorNeuron.OutputsWeight[j];

                }

                //step 8

            //update on input  for output values
                for (int k = 0; k < outputApple.numberOfInput; k++) {

                    outputApple.InputsWeight[k] = hiddenList.get(k).OutputsWeight[0] + outputApple.InputsWeight[k];
                    outputOrange.InputsWeight[k] = hiddenList.get(k).OutputsWeight[1] + outputOrange.InputsWeight[k];
                    outputBanana.InputsWeight[k] = hiddenList.get(k).OutputsWeight[2] + outputBanana.InputsWeight[k];

                }

                //assign inputs to outputs

                for (int j = 0; j < hiddenList.size(); j++) {
                    hiddenList.get(j).InputsWeight[0] = inputSweetnessNeuron.OutputsWeight[j];
                    hiddenList.get(j).InputsWeight[1] = inputColorNeuron.OutputsWeight[j];

                    hiddenList.get(j).OutputsWeight[0] = outputApple.InputsWeight[j];
                    hiddenList.get(j).OutputsWeight[1] = outputOrange.InputsWeight[j];
                    hiddenList.get(j).OutputsWeight[2] = outputBanana.InputsWeight[j];



                }




            if(goal>=crossEntropy) {
                flagReturen=true;
                break;
            }


        }

        list=hiddenList;
            if(flagReturen){
                break;
            }
}
        System.out.println("-----------------------------------------------------------");

int total=fruitList.size()*epoch;


      double accy= (double) acc /total;
      trueData=acc;

    trainingAcc=(100*accy);

    }
    int trueData;
    public double trainingAcc;
    private double activationTanh(double x){
        return Math.tanh(x);
}

//  private double getYa(ArrayList<Neuron> hiddenList, ArrayList<Double> yaInput, ArrayList<Double> yaOutput, double ya, Neuron outputNameNeuron) {
       ya=activation(ya);

        yaOutput.add(ya);

        ya=0;
        for (int k = 0; k < hiddenList.size(); k++) {
            ya = ya + (outputNameNeuron.InputsWeight[k] * yaInput.get(k));

        }
        return ya;
    }
    private static double tanhDerivative(double x) {
        double tanhX = Math.tanh(x);
        return 1.0 - tanhX * tanhX;
    }
    public static double relu(double x) {
        return Math.max(0, x);
    }
    public static double reluDerivative(double x) {
        return (x > 0) ? 1.0 : 0.0;
    }
    public  int counter=0;
    public String output;
    public void testData(String color,int sweetness,String goal,String activationFunction){
        crossEntropyTest=0;

        if(color.equals("1")){
            System.out.print("Color : red");
        }
        else if(color.equals("2")){
            System.out.print("Color : orange");
        }
        else if(color.equals("3")){
            System.out.print("Color : yellow");
        }
        System.out.print(" Sweetness : "+sweetness);

ArrayList <Fruit>fruits=new ArrayList<>();

        int sweet=sweetness;

        if(goal.equals("1 0 0")){
            System.out.println(" Goal : apple");

        }
        else if(goal.equals("0 1 0")){
            System.out.println(" Goal : orange");


        }
        else if(goal.equals("0 0 1")){
            System.out.println(" Goal : banana");

        }



        ArrayList<Double> YaInput = new ArrayList<>();
        ArrayList<Double> YaOutput = new ArrayList<>();
        inputSweetnessNeuron.InputsWeight[0] = sweet;
        inputColorNeuron.InputsWeight[0] = Integer.parseInt(color);

        //calculate Ya for input  step 1



        for (int j = 0; j < list.size(); j++) {

            double ya = ((inputSweetnessNeuron.InputsWeight[0] * list.get(j).InputsWeight[0]) + (inputColorNeuron.InputsWeight[0] * list.get(j).InputsWeight[1])) - list.get(j).threshold;
            if(activationFunction.equals("sigmoid")) {
                ya = activation(ya);
            }
            else if(activationFunction.equals("tanh")){
                ya=activationTanh(ya);
            }
            else if(activationFunction.equals("relu"))
            {
                ya=relu(ya);
            }

            YaInput.add(ya);

        }

        //calculate Ya for output
        // step 2
        double ya=0;

        for (int k = 0; k < list.size(); k++) {

            ya = ya + (outputApple.InputsWeight[k] * YaInput.get(k));
        }
        ya=  ya-outputApple.threshold;


        YaOutput.add(ya);
        ya=0;

        for (int k = 0; k < list.size(); k++) {

            ya = ya + (outputOrange.InputsWeight[k] * YaInput.get(k));
        }
        ya=  ya-outputOrange.threshold;

        YaOutput.add(ya);

        ya=0;

        for (int k = 0; k < list.size(); k++) {

            ya = ya + (outputBanana.InputsWeight[k] * YaInput.get(k));
        }
        ya=  ya-outputBanana.threshold;


        YaOutput.add(ya);
        ArrayList <Double> soft=new ArrayList<>();
        for(int j=0;j<YaOutput.size();j++) {
            double x=0;
            x=Math.exp(YaOutput.get(j))/(Math.exp(YaOutput.get(0))+Math.exp(YaOutput.get(1))+Math.exp(YaOutput.get(2)));
            soft.add(x);
        }




        String[]data=goal.split(" ");

        //step 3




        if(soft.get(0)>soft.get(1)&&soft.get(0)>soft.get(2) ){
            output="apple";
            System.out.println("output : apple");
        }
        else   if(soft.get(1)>soft.get(0)&&soft.get(1)>soft.get(2)  ){
            output="orange";
            System.out.println("output : orange");
        }
        else   if(soft.get(2)>soft.get(0)&&soft.get(2)>soft.get(1)){
            output="banana";
         System.out.println("output : banana");
        }
        System.out.println("______________________________________________________");


        if(soft.get(0)>soft.get(1)&&soft.get(0)>soft.get(2) &&goal.equals("1 0 0")){
           counter++;
        }
        else   if(soft.get(1)>soft.get(0)&&soft.get(1)>soft.get(2)  &&goal.equals("0 1 0")){
           counter++;
        }
        else   if(soft.get(2)>soft.get(0)&&soft.get(2)>soft.get(1) &&goal.equals("0 0 1")){
            counter++;
        }

        data=goal.split(" ");


        double[] trueProbabilities = {Double.parseDouble(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2])};

        // Calculate cross entropy
        for (int j = 0; j < trueProbabilities.length; j++) {
            crossEntropyTest += trueProbabilities[j] * Math.log(soft.get(j) );
        }

    }

    ArrayList <Neuron>list;

}
