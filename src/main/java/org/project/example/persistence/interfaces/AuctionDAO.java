package org.project.example.persistence.interfaces;

import java.util.List;

import org.project.example.model.Auction;

public interface AuctionDAO {

	void create(Auction leilao);

	List<Auction> encerrados();

	List<Auction> openAuctions();

	void update(Auction leilao);

}