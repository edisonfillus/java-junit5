package org.project.example.buider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.project.example.model.Bid;
import org.project.example.model.Auction;
import org.project.example.model.Bidder;

/**
 * Test Data Builder para Leilao
 * @author Edison Klafke Fillus
 *
 */
public class AuctionBuilder {
	private String descricao;
	private Calendar data;
	private List<Bid> lances;
	private boolean encerrado;

	public AuctionBuilder() {
		this.data = Calendar.getInstance();
		lances = new ArrayList<Bid>();
	}
	
	public AuctionBuilder to(String descricao) {
		this.descricao = descricao;
		return this;
	}
	
	public AuctionBuilder atDate(Calendar data) {
		this.data = data;
		return this;
	}

	public AuctionBuilder bid(Bidder usuario, double valor) {
		lances.add(new Bid(usuario, valor));
		return this;
	}

	public AuctionBuilder encerrado() {
		this.encerrado = true;
		return this;
	}

	public Auction build() {
		Auction leilao = new Auction(descricao, data);
		for(Bid lanceDado : lances) leilao.propoe(lanceDado);
		if(encerrado) leilao.encerra();
				
		return leilao;
	}
	
}
