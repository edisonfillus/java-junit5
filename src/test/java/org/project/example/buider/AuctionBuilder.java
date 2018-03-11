package org.project.example.buider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.project.example.model.Bid;
import org.project.example.model.Auction;
import org.project.example.model.Bidder;

/**
 * Test Data Builder for Auction
 * @author Edison Klafke Fillus
 *
 */
public class AuctionBuilder {
	private String description;
	private Calendar date;
	private List<Bid> bids;
	private boolean finished;

	public AuctionBuilder() {
		this.date = Calendar.getInstance();
		bids = new ArrayList<Bid>();
	}
	
	public AuctionBuilder to(String descricao) {
		this.description = descricao;
		return this;
	}
	
	public AuctionBuilder atDate(Calendar date) {
		this.date = date;
		return this;
	}

	public AuctionBuilder bid(Bidder usuario, double valor) {
		bids.add(new Bid(usuario, valor));
		return this;
	}

	public AuctionBuilder encerrado() {
		this.finished = true;
		return this;
	}

	public Auction build() {
		Auction auction = new Auction(description, date);
		for(Bid bid : bids) auction.addBid(bid);
		if(finished) auction.finish();
				
		return auction;
	}
	
}
