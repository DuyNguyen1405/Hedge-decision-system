package main;

import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import lib.Hedge;

public class main {
	public static void makeTestCase(Hedge hedge) {
		System.out.println("Car name: " + hedge.getCarName(1));
		System.out.println("Car importance: " + Arrays.toString(hedge.getCarImportance(1)));
		System.out.println("BaseG: " + Arrays.toString(hedge.getBaseG()));
		System.out.println("FmBaseG[0]: " + hedge.getFuzzyHashFm().get(hedge.getBaseG()[0]));
		System.out.println("Alpha: " + hedge.getAlpha());
		System.out.println("Beta: " + hedge.getBeta());
		
		String countSpaceTest = hedge.getPositiveHedgeH()[0] + " " + hedge.getNegativeHedgeH()[0] + " " + hedge.getBaseG()[0];
		System.out.println("Count space: " + hedge.countSpace(countSpaceTest));
		
		String stringTest1 = hedge.getPositiveHedgeH()[0] + " " + hedge.getBaseG()[0];
		String stringTest2 = hedge.getNegativeHedgeH()[0] + " " + hedge.getBaseG()[1];
		System.out.println("Fuzzy compare " + stringTest1 + " + " + stringTest2 + ": "+ hedge.fuzzyCompare(stringTest1,stringTest2));
		System.out.println("Word count: " + hedge.getWordCount());
		System.out.println("Fuzzy sign: " + hedge.getFuzzySign());
		System.out.println("FuzzyHashFM: " + hedge.getFuzzyHashFm());
		System.out.println("FuzzyHashV: " + hedge.getFuzzyHashV());
		
		String stringTest3 = hedge.getPositiveHedgeH()[0] + " " + hedge.getBaseG()[1];
		String stringTest4 = hedge.getPositiveHedgeH()[0] + " " + hedge.getNegativeHedgeH()[0] + " " + hedge.getBaseG()[0];
		hedge.calculateV(stringTest1);
		hedge.calculateV(stringTest2);
		hedge.calculateV(stringTest3);
		hedge.calculateV(stringTest4);
		
		String falseTest = "abcxzuixc";
		hedge.calculateV(falseTest);
	}
	
	public static double[] hedgeDecision(Hedge hedge1, Hedge hedge2, Hashtable<Double, String> resultHash) {
		double[] result = {0,0,0,0,0,0};
		for(int i = 1; i < 6; i++) {
			String[] importanceArray1 = hedge1.getCarImportance(i);
			String[] importanceArray2 = hedge2.getCarImportance(i);
			double tmp1 = 0.0;
			double tmp2 = 0.0;
//			System.out.println(Arrays.toString(importanceArray1));
//			System.out.println(Arrays.toString(importanceArray2));
			for(int j = 0; j < 4; j++) {
				tmp1 = hedge1.getFuzzyHashV().get(importanceArray1[j]);
				tmp2 = hedge2.getFuzzyHashV().get(importanceArray2[j]);
//				System.out.println(tmp1 + " " + tmp2);
				result[i] += tmp1 * tmp2;
//				System.out.println((hedge1.getFuzzyHashV().get(importanceArray1[j]))*(hedge2.getFuzzyHashV().get(importanceArray2[j])));
//				System.out.println(hedge1.getFuzzyHashV().get(importanceArray1[j]));
//				System.out.println(hedge2.getFuzzyHashV().get(importanceArray2[j]));
				
			}
			result[i] = Math.round(result[i]*10000);
			result[i] = result[i]/10000;
//			System.out.println(result[i]);
			resultHash.put(result[i],hedge1.getCarName(i));
		}
		return result;
	}
	
	public static void main(final String[] args) throws IOException {
		double[] result = null;
		String orderList = "";
		String order = "";
		Hashtable<Double, String> resultHash = new Hashtable<Double, String>();
		Hedge hedge = new Hedge(new String[] {"low","high"}, new String[] {"very"},new String[] {"little"},0.5,0.5,0.5, "M1.txt");
		Hedge hedge2 = new Hedge(new String[] {"unimportant","important"}, new String[] {"very"},new String[] {"little"},0.65,0.35,0.4, "M1i.txt");
		makeTestCase(hedge);
		makeTestCase(hedge2);
		result = hedgeDecision(hedge,hedge2,resultHash);
		System.out.println(resultHash);
		Arrays.sort(result);
		for(int i = result.length-1; i > 0; i--) {
			orderList += (resultHash.get(result[i]) + ": " + result[i]);
			orderList += "\n";
			order += resultHash.get(result[i]);
			if (i > 1) order += " > ";
		}
		JOptionPane.showMessageDialog (null, orderList + "\n" + order, "Hedge result", JOptionPane.INFORMATION_MESSAGE);
	}
}
