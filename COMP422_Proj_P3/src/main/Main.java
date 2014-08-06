package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import weka.classifiers.trees.j48.ModelSelection;
import decisionTree.DecisionTree;

public class Main
{

    private static String[] files = { "mfeat-fou", "mfeat-fac", "mfeat-kar",
            "mfeat-pix", "mfeat-zer", "mfeat-mor" };
    private static HashMap<Integer, ArrayList<ArrayList<String>>> table = createClassMap();
    private static HashMap<String, Integer> attsNumMap = new HashMap<String, Integer>();
    private static ArrayList<String> attDeclarations = new ArrayList<String>();
    private static int numAtts = 0;

    public static void main( String[] args )
    {

        createDataFile();
         createARFF();
       // DecisionTree dt = new DecisionTree();

    }

    // Creates a .arff format file from the created tabulated data file
    private static void createARFF()
    {
        for ( String s : files )
        {
            int attCount = attsNumMap.get( s );
            for ( int i = 1; i <= attCount; i++ )
            {
                String string = s + i;
                attDeclarations.add( "@ATTRIBUTE " + string + " NUMERIC\n" );
            }
        }
        attDeclarations.add( "@ATTRIBUTE class {0,1,2,3,4,5,6,7,8,9}" );
        try
        {
            File fileTrain = new File( "src/imageFeaturesTrain.arff" );
            File fileTest = new File( "src/imageFeaturesTest.arff" );
            FileWriter writerTrain = new FileWriter( fileTrain );
            FileWriter writerTest = new FileWriter( fileTest );
            writerTrain.write( "@RELATION numerals\n\n" );
            writerTest.write( "@RELATION numerals\n\n" );
            for ( String s : attDeclarations )
            {
                writerTrain.write( s );
                writerTest.write( s );
            }

            writerTrain.write( "\n\n@DATA \n" );
            writerTest.write( "\n\n@DATA \n" );
            BufferedReader reader = new BufferedReader( new InputStreamReader(
                    ClassLoader.getSystemResourceAsStream( "featuresTabulated" ) ) );
            String line = reader.readLine();
            int lineCount = 1;
            while ( line != null )
            {

                if ( lineCount < 100 )
                {
                    writerTrain.write( line + "\n" );
                }
                else
                {
                    writerTest.write( line + "\n" );
                }
                line = reader.readLine();
                if ( lineCount == 200 )
                {
                    lineCount = 0;
                }
                else
                {
                    lineCount++;
                }
            }
            writerTrain.close();
            writerTest.close();
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println( ".arff Writing Done." );
    }

    // Tabulates and combine data files to one file
    private static void createDataFile()
    {
        System.out.println( "Creating dataset" );
        for ( String s : files )
        {

            System.out.println( "Processing file : " + s );
            BufferedReader reader = new BufferedReader( new InputStreamReader(
                    ClassLoader.getSystemResourceAsStream( "mfeat-digits/" + s ) ) );
            try
            {
                int count = 0;
                String line = reader.readLine();
                while ( line != null )
                {

                    for ( int k = 0; k < 10; k++ )
                    {
                        ArrayList<ArrayList<String>> strings = new ArrayList<ArrayList<String>>();
                        for ( int i = 0; i < 200; i++ )
                        {

                            String[] split = line.split( " " );
                            ArrayList<String> features = new ArrayList<String>();
                            for ( String str : split )
                            {
                                if ( isNumeric( str ) )
                                {
                                    features.add( str );
                                }
                            }
                            if ( attsNumMap.get( s ) == null )
                            {
                                attsNumMap.put( s, features.size() );
                            }
                            if ( features.size() > 0 )
                            {

                                strings.add( features );
                            }
                            line = reader.readLine();
                            count++;
                        }
                        addToTable( strings, k );
                    }
                }
                System.out.println( "Total lines processed : " + count );
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
        // printTable();
        try
        {
            File fileOutput = new File( "src/featuresTabulated" );
            FileWriter writer = new FileWriter( fileOutput );
            int linecount = 0;
            for ( int k = 0; k < 10; k++ )
            {
                ArrayList<ArrayList<String>> s = table.get( k );
                for ( ArrayList<String> list : s )
                {
                    numAtts = list.size();
                    String out = "";
                    for ( int i = 0; i < list.size(); i++ )
                    {

                        if ( i == list.size() - 1 )
                        {
                            out += list.get( i ) + "," + k;
                        }
                        else
                        {
                            out += list.get( i ) + ",";
                        }
                        // if(i == 648){
                        // System.out.println("Linecoutn: "+ linecount +
                        // " String ;" + out);
                        // }
                    }

                    writer.write( out + "\n" );
                    linecount++;
                }
            }
            writer.close();
            System.out.println( "Total lines processed : " + linecount );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        System.out.println( "File Collation Done" );
    }

    private static HashMap<Integer, ArrayList<ArrayList<String>>> createClassMap()
    {

        HashMap<Integer, ArrayList<ArrayList<String>>> temp = new HashMap<Integer, ArrayList<ArrayList<String>>>();
        for ( int i = 0; i < 10; i++ )
        {
            temp.put( i, new ArrayList<ArrayList<String>>() );
            for ( int j = 0; j < 200; j++ )
            {
                temp.get( i ).add( new ArrayList<String>() );
            }
        }
        System.out.println( temp.get( 0 ).size() + " Num of lists of lists" );
        System.out.println( temp.get( 0 ).get( 0 ).size() + " size lists" );
        return temp;
    }

    public static boolean isNumeric( String str )
    {
        return str.matches( "-?\\d+(\\.\\d+)?" ); // match a number with optional
                                                  // '-' and decimal.
    }

    public static void addToTable( ArrayList<ArrayList<String>> values, int k )
    {
        for ( int i = 0; i < values.size(); i++ )
        {
            table.get( k ).get( i ).addAll( values.get( i ) );
        }
    }

    public static void printTable()
    {
        for ( ArrayList<ArrayList<String>> s : table.values() )
        {
            // System.out.println("Instances list" + s.size());
            for ( ArrayList<String> list : s )
            {
                System.out.println( "Features list" + list.size() );
            }
        }
    }
}
