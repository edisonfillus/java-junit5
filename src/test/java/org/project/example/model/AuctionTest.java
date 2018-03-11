package org.project.example.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.project.example.model.Bid;
import org.project.example.model.Auction;
import org.project.example.model.Bidder;

public class AuctionTest {
	@Test
	public void deveReceberUmLance() {
		Auction leilao = new Auction("Macbook Pro 15");
		assertEquals(0, leilao.getBids().size());

		leilao.addBid(new Bid(new Bidder("Steve Jobs"), 2000.0));

		assertEquals(1, leilao.getBids().size());
		assertEquals(2000.0, leilao.getBids().get(0).getValue(), 0.00001);
	}

	@Test
	public void deveReceberVariosLances() {
		Auction leilao = new Auction("Macbook Pro 15");
		leilao.addBid(new Bid(new Bidder("Steve Jobs"), 2000.0));
		leilao.addBid(new Bid(new Bidder("Steve Wozniak"), 3000.0));

		assertEquals(2, leilao.getBids().size());
		assertEquals(2000.0, leilao.getBids().get(0).getValue(), 0.00001);
		assertEquals(3000.0, leilao.getBids().get(1).getValue(), 0.00001);
	}
	

	
	 
}
