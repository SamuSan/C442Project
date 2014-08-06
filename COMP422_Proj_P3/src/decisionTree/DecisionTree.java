package decisionTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import weka.classifiers.trees.J48;
import weka.core.Instances;

public class DecisionTree {
	private J48 tree = new J48();

	public DecisionTree() {
		try {
			Instances data = new Instances(new BufferedReader(
					new InputStreamReader(ClassLoader
							.getSystemResourceAsStream("imageFeatures.arff"))));
			data.setClassIndex(data.numAttributes() - 1);

			tree.setUnpruned(true);
			tree.buildClassifier(data);
			System.out.println(tree.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public DecisionTree() {
	// // TODO Auto-generated constructor stub
	// }

}
