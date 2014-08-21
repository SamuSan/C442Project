package decisionTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import weka.classifiers.trees.J48;
import weka.core.Instances;

public class DecisionTree
{
    private J48 tree; 
    private ArrayList<String> falseClassifications = new ArrayList<String>();
    private Instances trainingData;
    private Instances testData;

    public DecisionTree( String train, String test )
    {
        tree = new J48();
        tree.setUnpruned( true );
        createTrainingSet( train );
        createTestSet( test );


    }

    private void createTestSet( String test )
    {
        try
        {
            testData = new Instances( new BufferedReader(
                    new InputStreamReader( ClassLoader
                            .getSystemResourceAsStream( test ) ) ) );
            testData.setClassIndex( testData.numAttributes() - 1 );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    private void createTrainingSet( String train )
    {
        try
        {
            trainingData = new Instances( new BufferedReader(
                    new InputStreamReader( ClassLoader
                            .getSystemResourceAsStream( train ) ) ) );
            trainingData.setClassIndex( trainingData.numAttributes() - 1 );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    public void classify()
    {
        float correct = 0;
        float incorrect = 0;
        try
        {
            tree.buildClassifier( trainingData );
            System.out.println( tree.toString() );
            for ( int i = 0; i < testData.numInstances(); i++ )
            {
                double pred = tree.classifyInstance( testData.instance( i ) );
                System.out.print( "ID: " + testData.instance( i ).value( 0 ) );
                System.out.print( ", actual: " + testData.classAttribute().value( (int) testData.instance( i ).classValue() ) );
                System.out.println( ", predicted: " + testData.classAttribute().value( (int) pred ) );
                if ( testData.classAttribute().value( (int) testData.instance( i ).classValue() ) == testData.classAttribute().value( (int) pred ) )
                {
                    correct++;
                }
                else
                {
                    falseClassifications.add( testData.classAttribute().value( (int) testData.instance( i ).classValue() ) );
                    incorrect++;
                }
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        Collections.sort( falseClassifications );
        System.out.println( "Accuracy: " + correct / ( correct + incorrect ) );
        System.out.println( "False classifications: " + incorrect / ( correct + incorrect ) );
        Set<String> uniquest = new HashSet<String>( falseClassifications );
        for ( String s : uniquest )
        {
            System.out.println( s + " : " + Collections.frequency( falseClassifications, s ) );
        }
    }

}
