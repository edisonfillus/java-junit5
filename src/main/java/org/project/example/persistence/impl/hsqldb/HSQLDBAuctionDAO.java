package org.project.example.persistence.impl.hsqldb;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;

import org.project.example.model.Auction;
import org.project.example.model.Bidder;
import org.project.example.persistence.interfaces.AuctionDAO;

public class HSQLDBAuctionDAO implements AuctionDAO {

	private final EntityManager em;

	public HSQLDBAuctionDAO(EntityManager em) {
		super();
		this.em = em;
	}

	@Override
	public void create(Auction leilao) throws SQLException {
		em.persist(leilao);
	}

	@Override
	public void update(Auction leilao) throws SQLException {
		em.merge(leilao);
	}

	@Override
	public List<Auction> findFinishedAuctions() throws SQLException {
		return em.createQuery("SELECT a FROM Auction a WHERE a.finished = true",	Auction.class).getResultList();
	}

	@Override
	public List<Auction> findOpenAuctions() throws SQLException {
		return em.createQuery("SELECT a FROM Auction a WHERE a.finished = false",	Auction.class).getResultList();
	}

	public Long countTotalFinished() {
		return em.createQuery("SELECT count(a) FROM Auction a WHERE a.finished = true", Long.class).getSingleResult();

	}

	public Long countTotalOpen() {
		return em.createQuery("SELECT count(a) FROM Auction a WHERE a.finished = false",Long.class).getSingleResult();
	}

	public List<Auction> porPeriodo(Calendar start, Calendar finish) {
		return em.createQuery("SELECT a FROM Auction a WHERE a.date BETWEEN :start and :finish and a.finished = false",
				Auction.class).setParameter("start", start).setParameter("finish", finish).getResultList();
	}
	
	public List<Auction> findAuctionsByBidder(Bidder bidder) {
		return em.createQuery("SELECT DISTINCT(bid.auction) FROM Bid bid WHERE bid.bidder = :bidder",Auction.class)
				.setParameter("bidder", bidder).getResultList();
	}

}
