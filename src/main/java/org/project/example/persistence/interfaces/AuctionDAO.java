package org.project.example.persistence.interfaces;

import java.sql.SQLException;
import java.util.List;

import org.project.example.model.Auction;
import org.project.example.model.Bidder;

public interface AuctionDAO {

	void create(Auction leilao) throws SQLException;

	void update(Auction leilao) throws SQLException;
	
	List<Auction> findFinishedAuctions() throws SQLException ;

	List<Auction> findOpenAuctions() throws SQLException ;

	Long countTotalFinished();

	Long countTotalOpen();

	List<Auction> findAuctionsByBidder(Bidder bidder);


}