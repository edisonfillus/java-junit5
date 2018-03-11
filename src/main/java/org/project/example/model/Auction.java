package org.project.example.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Auction {

	@Id @GeneratedValue
	private int id;
	private String description;
	private Calendar date;
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="auction")
	private List<Bid> bids;
	private boolean finished;

	public Auction() {
		super();
	}
	
	public Auction(String description) {
		this(description, Calendar.getInstance());
	}

	public Auction(String description, Calendar date) {
		this.setDescription(description);
		this.date = date;
		this.bids = new ArrayList<>();
	}

	public Auction(String description, double d, Bidder mauricio, boolean b) {
		this.setDescription(description);
		this.finished = b;
		this.bids = new ArrayList<>();
		this.addBid(new Bid(mauricio, d));
	}

	public void addBid(Bid bid) {
		if (bids.isEmpty() || canBid(bid.getBidder())) {
			bids.add(bid);
			bid.setAuction(this);
		}
	}

	private boolean canBid(Bidder bidder) {
		return !lastBid().getBidder().equals(bidder) && countBidFrom(bidder) < 5;
	}

	private int countBidFrom(Bidder bidder) {
		int total = 0;
		for (Bid l : bids) {
			if (l.getBidder().equals(bidder))
				total++;
		}
		return total;
	}

	private Bid lastBid() {
		return bids.get(bids.size() - 1);
	}

	public String getDescription() {
		return description;
	}

	public List<Bid> getBids() {
		return Collections.unmodifiableList(bids);
	}

	public Calendar getDate() {
		return (Calendar) date.clone();
	}

	public void finish() {
		this.finished = true;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
