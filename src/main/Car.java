package main;

import javafx.beans.property.SimpleStringProperty;

public class Car {
	private final SimpleStringProperty name;
	private final SimpleStringProperty w1;
	private final SimpleStringProperty w2;
	private final SimpleStringProperty w3;
	private final SimpleStringProperty w4;
	private final SimpleStringProperty q1;
	private final SimpleStringProperty q2;
	private final SimpleStringProperty q3;
	private final SimpleStringProperty q4;
	private double score;
	
	public Car(String name, String w1, String w2, String w3, String w4, String q1, String q2, String q3, String q4, double score){
        this.name = new SimpleStringProperty(name);
        this.w1 = new SimpleStringProperty(w1);
        this.w2 = new SimpleStringProperty(w2);
        this.w3 = new SimpleStringProperty(w3);
        this.w4 = new SimpleStringProperty(w4);
        this.q1 = new SimpleStringProperty(q1);
        this.q2 = new SimpleStringProperty(q2);
        this.q3 = new SimpleStringProperty(q3);
        this.q4 = new SimpleStringProperty(q4);
        this.score = score;
    }

	public String getName() {
		return name.get();
	}

	public String getW1() {
		return w1.get();
	}

	public String getW2() {
		return w2.get();
	}

	public String getW3() {
		return w3.get();
	}

	public String getW4() {
		return w4.get();
	}

	public String getQ1() {
		return q1.get();
	}

	public String getQ2() {
		return q2.get();
	}

	public String getQ3() {
		return q3.get();
	}

	public String getQ4() {
		return q4.get();
	}

	public double getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public void setName(String carName) {
        name.set(carName);
    }	
	
	public void setW1(String w1) {
        this.w1.set(w1);
    }
	
	public void setW2(String w2) {
        this.w2.set(w2);
    }
	
	public void setW3(String w3) {
        this.w3.set(w3);
    }
	
	public void setW4(String w4) {
        this.w4.set(w4);
    }
	
	public void setQ1(String q1) {
        this.q1.set(q1);
    }
	
	public void setQ2(String q2) {
        this.q2.set(q2);
    }
	
	public void setQ3(String q3) {
        this.q3.set(q3);
    }
	
	public void setQ4(String q4) {
        this.q4.set(q4);
    }
}
