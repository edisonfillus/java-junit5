package org.project.example.persistence.impl.hsqldb;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.project.example.model.Auction;
import org.project.example.model.Bidder;
import org.project.example.persistence.interfaces.AuctionDAO;
import org.project.example.persistence.interfaces.BidderDAO;

public class HSQLDBAuctionDAOTest {

	private EntityManagerFactory sessionFactory;
	private EntityManager em;
	private AuctionDAO auctionDAO;
	private BidderDAO bidderDAO;
	
	@Before
	public void beforeTests() {
		sessionFactory = Persistence.createEntityManagerFactory("hsqldb");
        em = sessionFactory.createEntityManager();
        auctionDAO = new HSQLDBAuctionDAO(em);
        bidderDAO = new HSQLDBBidderDAO(em);
        em.getTransaction().begin();        
	}
	
	@After
	public void afterTests() {
		em.getTransaction().rollback();  //Undo everything
		em.close();
	}
	
	@Test
    public void deveContarLeiloesNaoEncerrados() throws SQLException {
        // criamos um usuario
        Bidder mauricio = new Bidder("Mauricio Aniche", "mauricio@aniche.com.br");

        // criamos os dois leiloes
        Auction ativo = new Auction("Geladeira", 1500.0, mauricio, false);
        Auction encerrado = new Auction("XBox", 700.0, mauricio, false);
        encerrado.encerra();

        // persistimos todos no banco
        bidderDAO.create(mauricio);
        auctionDAO.create(ativo);
        auctionDAO.create(encerrado);

        // invocamos a acao que queremos testar
        // pedimos o total para o DAO
        long total = auctionDAO.countTotalFinished();

        assertEquals(1L, total);
    }
	
	
}
