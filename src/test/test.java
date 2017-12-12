package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class test {
	public static void main(String[] args) throws IOException {
		BufferedReader reader;
		String name = "src/test/map/M1.txt";
		FileReader file = new FileReader(name);
		reader = new BufferedReader(file);
		String line = reader.readLine();
		System.out.println(line);
	}
}
