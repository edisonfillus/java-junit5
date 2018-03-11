package org.project.example.persistence.impl.hsqldb;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

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
        encerrado.finish();

        // persistimos todos no banco
        bidderDAO.create(mauricio);
        auctionDAO.create(ativo);
        auctionDAO.create(encerrado);

        // invocamos a acao que queremos testar
        // pedimos o total para o DAO
        long total = auctionDAO.countTotalOpen();
        List<Auction> openAuctions = auctionDAO.findOpenAuctions();
        
        assertEquals(1L, total);
        assertEquals(1L, openAuctions.size());
        assertEquals("Geladeira", openAuctions.get(0).getDescription());
    }
	
	@Test
    public void deveContarLeiloesEncerrados() throws SQLException {
        // criamos um usuario
        Bidder mauricio = new Bidder("Mauricio Aniche", "mauricio@aniche.com.br");

        // criamos os dois leiloes
        Auction ativo = new Auction("Geladeira", 1500.0, mauricio, false);
        Auction encerrado = new Auction("XBox", 700.0, mauricio, false);
        encerrado.finish();

        // persistimos todos no banco
        bidderDAO.create(mauricio);
        auctionDAO.create(ativo);
        auctionDAO.create(encerrado);

        // invocamos a acao que queremos testar
        // pedimos o total para o DAO
        long total = auctionDAO.countTotalFinished();
        List<Auction> finishedAuctions = auctionDAO.findFinishedAuctions();
        
        assertEquals(1L, total);
        assertEquals(1L, finishedAuctions.size());
        assertEquals("XBox", finishedAuctions.get(0).getDescription());
    }
	

	
	
}
