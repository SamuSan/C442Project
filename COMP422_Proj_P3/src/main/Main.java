package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main {

	private static String[] files = { "mfeat-fou" 
	 , "mfeat-fac", "mfeat-kar",
	 "mfeat-pix", "mfeat-zer", "mfeat-mor" };
	private static HashMap<Integer, ArrayList<ArrayList<String>>> table = createClassMap();

	public static void main(String[] args) {

		for (String s : files) {

			System.out.println("Printing file : " + s);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					ClassLoader.getSystemResourceAsStream("mfeat-digits/" + s)));
			try {
				String line = reader.readLine();
				while (line != null) {
					int count = 0;
					for (int k = 0; k < 10; k++) {
						// System.out.println("K" + k);

						ArrayList<ArrayList<String>> strings = new ArrayList<ArrayList<String>>();
						for (int i = 0; i < 200; i++) {
							// System.out.println("i" + i);
							String[] split = line.split(" ");

							ArrayList<String> features = new ArrayList<String>();
							for (String str : split) {
								if (isNumeric(str)) {
									features.add(str);
								}
							}
							if (features.size() > 0) {
								strings.add(features);
							}

							line = reader.readLine();
							count++;
						}
						addToTable(strings, k);

					}
					System.out.println(count);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 printTable();
		System.out.println("Done");

	}

	private static HashMap<Integer, ArrayList<ArrayList<String>>> createClassMap() {

		HashMap<Integer, ArrayList<ArrayList<String>>> temp = new HashMap<Integer, ArrayList<ArrayList<String>>>();
		for (int i = 0; i < 10; i++) {
			temp.put(i, new ArrayList<ArrayList<String>>());
			for (int j = 0; j < 200; j++) {
				temp.get(i).add(new ArrayList<String>());
			}
		}
		System.out.println(temp.get(0).size() + " Num of lists of lists");
		System.out.println(temp.get(0).get(0).size() + " size lists");
		return temp;
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional
												// '-' and decimal.
	}

	public static void addToTable(ArrayList<ArrayList<String>> values, int k) {
		// if(table.get(k).get(0).size() == 0){
		System.out.println("Initial size" + table.get(k).size());
		for (int i = 0; i < values.size(); i++) {
			System.out.println(table.get(k).get(i).size());
			table.get(k).get(i).addAll(values.get(i));
			System.out.println(table.get(k).get(i).size());
		}
		System.out.println("Post add "  +table.get(k).size());
		// }

	}

	public static void printTable() {
		for (ArrayList<ArrayList<String>> s : table.values()) {
			System.out.println("Instances list" + s.size());
			for (ArrayList<String> list : s) {
				System.out.println("Features list" + list.size());
			}
		}
	}
}
