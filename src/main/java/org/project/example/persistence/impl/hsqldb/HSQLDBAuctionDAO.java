package org.project.example.persistence.impl.hsqldb;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;

import org.project.example.model.Auction;
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
		return null;
	}

	@Override
	public List<Auction> findOpenAuctions() throws SQLException {
		return null;
	}
	
	public Long countTotalFinished() {
        return (Long) em.createQuery(
        		  "SELECT count(a) "
        		+ "FROM Auction a "
        		+ "WHERE a.finished = false")
                .getSingleResult();
		
    }

}
