package org.project.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Bid {
	
	
	@Id @GeneratedValue
	private long id;
	@ManyToOne(optional=false) 
	private Bidder bidder;
	@ManyToOne(optional=false)
	private Auction auction;
	private double value;

	public Bid() {
		super();
	}
	
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

	public Auction getAuction() {
		return auction;
	}

	public void setAuction(Auction auction) {
		this.auction = auction;
	}

}
