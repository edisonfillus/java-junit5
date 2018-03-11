package org.project.example.model;

import org.junit.Test;
import org.project.example.model.Bid;
import org.project.example.model.Bidder;

public class BidTest {
	
	@Test(expected = IllegalArgumentException.class)
	public void deveRecusarLancesComValorDeZero() {
		new Bid(new Bidder("John Doe"), 0.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void deveRecusarLancesComValorNegativo() {
		new Bid(new Bidder("John Doe"), -10.0);
	}
	

}
