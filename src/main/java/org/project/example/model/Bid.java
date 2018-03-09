package org.project.example.model;

public class Bid {

	private Bidder bidder;
	private double value;


	public Bid(Bidder bidder, double value) {
		if (value <= 0) 
			throw new IllegalArgumentException();
		this.bidder = bidder;
		this.value = value;
	}

	public Bidder getBidder() {
		return bidder;
	}

	public double getValue() {
		return value;
	}

}
