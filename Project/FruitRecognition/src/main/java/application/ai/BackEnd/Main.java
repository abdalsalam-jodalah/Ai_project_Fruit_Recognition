package application.ai.BackEnd;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int perceptronNumber;
        double learningRate;
        int maximumNumberOfEpochs;
        String goal;
        String sweetness;
        String color;

        Scanner input = new Scanner(System.in);

        System.out.println("Enter Number of Perceptron : ");
        perceptronNumber=Integer.parseInt(input.nextLine());//-----perceptron number
        System.out.println("Enter Learning Rate : ");
        learningRate=Double.parseDouble(input.nextLine());//---------learning rate
        System.out.println("Enter Maximum number of epochs : ");
        maximumNumberOfEpochs=Integer.parseInt(input.nextLine());//---------Maximum number of epochs
        System.out.println("Enter Goal : ");
        goal=input.nextLine();//-------------------------------------------Goal

//
//            System.out.println("Enter Sweetness : ");
//            sweetness = input.nextLine();//---------------sweetness variable
//            System.out.println("Enter Color : ");
//            color = input.nextLine();//-------------------color variable
//            System.out.println("___________________________");
//            System.out.println("Sweetness : " + sweetness);
//            System.out.println("Color : " + color);
//            System.out.println("___________________________");
            NeuralNetwork object=new NeuralNetwork();
       // ArrayList<Neuron> hiddenList= object.createNeural(perceptronNumber);
        System.out.println("Enter activation fucntion ");
        String activation=input.nextLine();

        String filePath="/Users/yazanmansour/Downloads/data.txt";
        object.trainData(filePath,perceptronNumber,learningRate,activation,maximumNumberOfEpochs,Double.parseDouble(goal));
        filePath="/Users/yazanmansour/Downloads/test.txt";
        ArrayList<Fruit> fruits=object.readDataFromFile(filePath);
        for(int i=0;i<fruits.size();i++)
        object.testData(fruits.get(i).color,Integer.parseInt(fruits.get(i).sweetness),fruits.get(i).name,activation);


        System.out.println("True data in learning: "+object.trueData);
        System.out.println("Training Accuracy : "+object.trainingAcc+"%");
       double accu=(double) object.counter/fruits.size();
        System.out.println("output accuracy : "+accu*100+"%");

      // Fruit object = new Fruit(sweetness,color);



    }
}