package lib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;

public class Hedge {
	private static BufferedReader reader;
	private String[][] words;
	private String[] baseG;
	private String[] positiveHedgeH;
	private String[] negativeHedgeH;
	private double alpha;
	private double beta;
	private double theta;
	private double muBasePositiveHedgeH;
	private double muBaseNegativeHedgeH;
	private Hashtable<String, Double> fuzzyHashFm = new Hashtable<String, Double>();
	private Hashtable<String, Double> fuzzyHashMu = new Hashtable<String, Double>();
	private Hashtable<String, Double> fuzzyHashV = new Hashtable<String, Double>();
	private Hashtable<String, Integer> wordCount = new Hashtable<String, Integer>();
	private Hashtable<String, Integer> fuzzySign = new Hashtable<String, Integer>();
	
	public Hedge(String[] baseG, String[] positiveHedgeH, String[] negativeHedgeH, double muBasePositiveHedgeH, double muBaseNegativeHedgeH, double theta, String fileName) throws IOException{
        this.baseG = baseG;
        this.positiveHedgeH = positiveHedgeH;
        this.negativeHedgeH = negativeHedgeH;
        this.muBasePositiveHedgeH = muBasePositiveHedgeH;
        this.muBaseNegativeHedgeH = muBaseNegativeHedgeH;
        this.theta = theta;
        this.readDataFromFile(fileName);
        this.fuzzyHashMu.put(positiveHedgeH[0], this.muBasePositiveHedgeH);
        this.fuzzyHashMu.put(negativeHedgeH[0], this.muBaseNegativeHedgeH);
        this.fuzzyHashFm.put(baseG[0], theta);
        this.fuzzyHashFm.put(baseG[1], 1-theta);
        this.fuzzySign.put(baseG[0], -1);
        this.fuzzySign.put(baseG[1], 1);
        this.setAlpha();
        this.setBeta();
        this.fuzzyHashV.put("theta", theta);
        this.fuzzyHashV.put(baseG[0], this.getTheta() - (this.getAlpha()*fuzzyHashFm.get(baseG[0])));
		this.fuzzyHashV.put(baseG[1], this.getTheta() + (this.getAlpha()*fuzzyHashFm.get(baseG[1])));
        this.setHash("FuzzySign");
        this.setHash("FuzzyHashFm");
        this.setHash("FuzzyHashV");
    }

	public int countSpace(String str) {
		int result = 0;
		result = str.length() - str.replace(" ", "").length();
		return result;
	}
	
	public void readDataFromFile(String name) throws IOException{
		FileReader file = new FileReader(name);
		reader = new BufferedReader(file);
		String line = reader.readLine();
		String[] arr = null;
		arr = line.split(" ");
	
		int i=0,j=0;
		words = new String[6][5];
		do{
			for(i=0;i<6;i++){
				arr = line.split(",");
				for(j=0;j<5;j++){
					words[i][j] = arr[j];
					int space = countSpace(words[i][j]);
					if (!wordCount.contains(words[i][j]) && (words[i][j].contains(baseG[0]) || (words[i][j].contains(baseG[1])))) {
						wordCount.put(words[i][j], space);
					}
					//System.out.print(words[i][j] + " ");
				}
				line = reader.readLine();
			}
	}while(line != null);
		reader.close();
	}
	
	public int fuzzyCompare(String fuzzyComponent1, String fuzzyComponent2) {
		int result = 0;
		int base = 0;
		String[] arr1 = null;
		String[] arr2 = null;
		arr1 = fuzzyComponent1.split(" ");
		arr2 = fuzzyComponent2.split(" ");
		if((arr1[arr1.length-1].equals(baseG[0])) && (arr2[arr2.length-1].equals(baseG[1]))) {
			result = -1; 
			base = 1;
		}else if ((arr2[arr2.length-1].equals(baseG[0])) && (arr1[arr1.length-1].equals(baseG[1]))) {
			result = 1;
			base = 1;
		}else if (arr1[arr1.length-1].equals(arr2[arr2.length-1])) {
			if(arr1[arr1.length-1].equals(baseG[0])) {
				base = -1;
			}else base = 1;
			
			int i = arr1.length-2;
			int j = arr2.length-2;
			while(i>=0 && j>=0) {
				if(Arrays.asList(positiveHedgeH).contains(arr1[i]) && Arrays.asList(negativeHedgeH).contains(arr2[j])) {
					result = 1;
					break;
				}else if (Arrays.asList(positiveHedgeH).contains(arr2[j]) && Arrays.asList(negativeHedgeH).contains(arr1[i])) {
					result = -1;
					break;
				}else if (arr1[i].equals(arr2[j])) {
					result = 0;
				}
				i--;
				j--;
			}
			if((i == 0) && (Arrays.asList(positiveHedgeH).contains(arr1[i])) || (j == 0 && Arrays.asList(negativeHedgeH).contains(arr2[j]))) {
				result = 1;
			}else if((i == 0) && (Arrays.asList(negativeHedgeH).contains(arr1[i])) || (j == 0 && Arrays.asList(positiveHedgeH).contains(arr2[j]))) {
				result = -1;
			}
		}
		return result*base;
	}
	
	public String makeCompareString(String[] arr) {
		String compareString = "";
		for(int j = 1; j < arr.length; j++) {
			if(j != 1) compareString = compareString + " " + arr[j];
			else compareString += arr[j];
		}
		return compareString;
	}
	
	public void setHedgeFm(String key) {
		String[] arr = null;
		String compareString = "";
		double comp1 = 0;
		double comp2 = 0;
		double result = 0;
		arr = key.split(" ");
		compareString = makeCompareString(arr);
		comp1 = fuzzyHashMu.get(arr[0]);
		comp2 = fuzzyHashFm.get(compareString);
		result = comp1 * comp2;
		result = Math.round(result*10000);
		result = result/10000;
		fuzzyHashFm.put(key, result);
		System.out.println("Fm " + key + ": " + result);
	}
	
	public void calculateHedgeFm(String key) {
		String compareString = "";
		String[] arr = key.split(" ");
		compareString = makeCompareString(arr);
		if (Arrays.asList(baseG).contains(arr[arr.length-1]) && !compareString.isEmpty()){
			if(fuzzyHashFm.containsKey(compareString)) setHedgeFm(key);
			else {
				calculateHedgeFm(compareString);
				setHedgeFm(key);
			}
		} else System.out.println("Key: " + key + " is invalid");
	}
	
	public void setSign(String key) {
		String[] arr = null;
		String compareString = "";
		int result = 0;
		int positive = fuzzySign.get(baseG[1]);
		int negative = fuzzySign.get(baseG[0]);
		arr = key.split(" ");
		compareString = makeCompareString(arr);
		if(arr[arr.length-1].equals(baseG[0])) {
			result = fuzzyCompare(key,compareString);
			if(result == 1) {
				fuzzySign.put(key, -negative);
			}else if(result == -1) {
				fuzzySign.put(key, negative);
			}
		}else if(arr[arr.length-1].equals(baseG[1])) {
			result = fuzzyCompare(key,compareString);
			if(result == 1) {
				fuzzySign.put(key, positive);
			}else if(result == -1) {
				fuzzySign.put(key, -positive);
			}
		}
	}

	public double calculateW(String key) {
		double signKey;
		double signKeyTemp;
		double w = 0.0;
		String p = positiveHedgeH[positiveHedgeH.length-1];
		String keyTemp = p + " " + key;
		if(!fuzzySign.containsKey(key)) setSign(key);
		setSign(keyTemp);
		signKey = fuzzySign.get(key);
		signKeyTemp = fuzzySign.get(keyTemp);
		w = 0.5*(1.0 - signKey*signKeyTemp*(this.beta - this.alpha));
		return w;
	}
	
	public void setV(String key) {
		double w;
		double result;
		String compareString = "";
		String[] arr = null;
		w = calculateW(key);
		arr = key.split(" ");
		compareString = makeCompareString(arr);
		result = Math.round((fuzzyHashV.get(compareString) + fuzzySign.get(key)*(1-w)*fuzzyHashFm.get(key))*10000);
		result = result/10000;
		fuzzyHashV.put(key, result);
		System.out.println("V " + key + ": " + result);
	}
	
	public void calculateV(String key) {
		String compareString = "";
		String[] arr = key.split(" ");
		compareString = makeCompareString(arr);
		if (Arrays.asList(baseG).contains(arr[arr.length-1]) && !compareString.isEmpty()) {
			if(fuzzyHashV.containsKey(compareString) && fuzzySign.containsKey(key) && fuzzyHashFm.containsKey(key)) setV(key);
			else {
				calculateV(compareString);
				setSign(key);
				calculateHedgeFm(key);
				setV(key);
			}
		} else System.out.println("Key: " + key + " is invalid");

	}
	
	public void setHash(String type) {
		Collection<Integer> c = getWordCount().values();
		for(int i = 1; i < Collections.max(c)+1; i++) {
			for (String key : getWordCount().keySet()) {
				switch(type) {
					case "FuzzyHashFm": calculateHedgeFm(key);
					case "FuzzySign": setSign(key);
					case "FuzzyHashV": calculateV(key);
				}
			}
		}
	}
	
	public String[] getBaseG() {
		return baseG;
	}

	public void setBaseG(String[] baseG) {
		this.baseG = baseG;
	}

	public String[] getPositiveHedgeH() {
		return positiveHedgeH;
	}

	public void setPositiveHedgeH(String[] positiveHedgeH) {
		this.positiveHedgeH = positiveHedgeH;
	}

	public String[] getNegativeHedgeH() {
		return negativeHedgeH;
	}

	public void setNegativeHedgeH(String[] negativeHedgeH) {
		this.negativeHedgeH = negativeHedgeH;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha() {
		double result = 0;
		for(int i = 0;i < negativeHedgeH.length;i++) {
			result += this.fuzzyHashMu.get(negativeHedgeH[i]);
		}
		this.alpha = result;
	}

	public double getBeta() {
		return beta;
	}

	public void setBeta() {
		double result = 0;
		for(int i = 0;i < positiveHedgeH.length;i++) {
			result += this.fuzzyHashMu.get(positiveHedgeH[i]);
		}
		this.beta = result;
	}

	public double getTheta() {
		return theta;
	}

	public void setTheta(double theta) {
		this.theta = theta;
	}

	public Hashtable<String, Double> getFuzzyHashFm() {
		return fuzzyHashFm;
	}

	public void setFuzzyHashFm(Hashtable<String, Double> fuzzyHashFm) {
		this.fuzzyHashFm = fuzzyHashFm;
	}
	
	public String[][] getWords() {
		return words;
	}

	public void setWords(String[][] words) {
		this.words = words;
	}

	public Hashtable<String, Integer> getWordCount() {
		return wordCount;
	}

	public void setWordCount(Hashtable<String, Integer> wordCount) {
		this.wordCount = wordCount;
	}

	public String getCarName(int carNumber) {
		return this.words[carNumber][0];
	}
	
	public String[] getCarImportance(int carNumber) {
		return Arrays.copyOfRange(this.words[carNumber],1,5);
	}
	
	public Hashtable<String, Double> getFuzzyHashMu() {
		return fuzzyHashMu;
	}
	
	public Hashtable<String, Integer> getFuzzySign() {
		return fuzzySign;
	}
	
	public Hashtable<String, Double> getFuzzyHashV() {
		return fuzzyHashV;
	}
}
