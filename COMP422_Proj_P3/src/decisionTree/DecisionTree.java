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
    private J48 tree = new J48();
    private ArrayList<String> falseClassifications = new ArrayList<String>();

    public DecisionTree()
    {
        try
        {
            Instances trainingData = new Instances( new BufferedReader(
                    new InputStreamReader( ClassLoader
                            .getSystemResourceAsStream( "imageFeaturesTrain.arff" ) ) ) );
            trainingData.setClassIndex( trainingData.numAttributes() - 1 );

            Instances testData = new Instances( new BufferedReader(
                    new InputStreamReader( ClassLoader
                            .getSystemResourceAsStream( "imageFeaturesTest.arff" ) ) ) );
            testData.setClassIndex( trainingData.numAttributes() - 1 );

            tree.setUnpruned( true );
            tree.buildClassifier( trainingData );
            System.out.println( tree.toString() );
            float correct = 0;
            float incorrect = 0;
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
            Collections.sort(falseClassifications);
            System.out.println( "Accuracy: " + correct / ( correct + incorrect ) );
            System.out.println( "False classifications: " + incorrect / ( correct + incorrect ) );
            Set<String> uniquest = new HashSet<String>(falseClassifications);
            for ( String s : uniquest )
            {
                System.out.println(s +" : "+ Collections.frequency( falseClassifications, s ));
            }
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // public DecisionTree() {
    // // TODO Auto-generated constructor stub
    // }

}
