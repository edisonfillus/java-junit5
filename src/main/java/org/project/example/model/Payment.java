package org.project.example.model;

import java.util.Calendar;

public class Payment {

	private double value;
	private Calendar date;

	public Payment(double valor, Calendar date) {
	        this.value = valor;
	        this.date = date;
	    }

	public double getValue() {
		return value;
	}

	public Calendar getDate() {
		return date;
	}
}
