package org.project.example.persistence.impl.hsqldb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.project.example.buider.AuctionBuilder;
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
	

	@Test
    public void shouldReturnAllAuctionsFromBidder() throws SQLException {
        // create a bidder
        Bidder mauricio = new Bidder("Mauricio Aniche", "mauricio@aniche.com.br");

        	
        // create 2 auctions
        Auction auction1 = new Auction("Geladeira", 1500.0, mauricio, false);
        Auction auction2 = new Auction("XBox", 700.0, mauricio, false);

        // persist in database
        bidderDAO.create(mauricio);
        auctionDAO.create(auction1);
        auctionDAO.create(auction2);
        
        List<Auction> bidderAuctions = auctionDAO.findAuctionsByBidder(mauricio);
        
        assertEquals(2L, bidderAuctions.size());
        assertEquals(mauricio.getName(), bidderAuctions.get(0).getBids().get(0).getBidder().getName());
    }
	
	@Test
    public void shouldReturnOnlyDistinctAuctionsFromBidder() throws SQLException {
        // create a bidder
        Bidder mauricio = new Bidder("Mauricio Aniche", "mauricio@aniche.com.br");
        Bidder eduardo = new Bidder("Eduardo Aniche", "eduardo@aniche.com.br");
        
        // create 2 auctions
        Auction auction1 = new Auction("Geladeira", 1500.0, mauricio, false);
        Auction auction2 = new AuctionBuilder().to("XBox")
        		.bid(mauricio, 100.0)
        		.bid(eduardo, 120.0)
        		.bid(mauricio, 150.0)
        		.build();
        
        // persist in database
        bidderDAO.create(mauricio);
        bidderDAO.create(eduardo);
        auctionDAO.create(auction1);
        auctionDAO.create(auction2);
        
        List<Auction> bidderAuctions = auctionDAO.findAuctionsByBidder(mauricio);
        
        assertEquals(2L, bidderAuctions.size());
        assertEquals(mauricio.getName(), bidderAuctions.get(0).getBids().get(0).getBidder().getName());
    }
	
	@Test
	public void shouldDeleteAuction() throws SQLException {
		//Create Auction
        Bidder bidder = new Bidder("Mauricio Aniche", "mauricio@aniche.com.br");
        Auction auction = new AuctionBuilder().to("XBox")
        		.bid(bidder, 100.0)
        		.build();	
        bidderDAO.create(bidder);
        auctionDAO.create(auction);
		
		//Force Hibernate to send commands to database
		em.flush(); 
		em.clear();
		
		//Get Auction from database
		Auction auctionOnDB = auctionDAO.findById(auction.getId());
		
		//Assert creation
		assertNotNull(auctionOnDB);
		
		//Remove Bidder from database
		auctionDAO.remove(auctionOnDB);
		
		//Force Hibernate to send commands to database
		em.flush(); 
		em.clear();
				
		//Try to find removed Bidder
		Auction auctionRemoved = auctionDAO.findById(auction.getId());
		
		//Assert it not exist anymore
		assertNull(auctionRemoved);

	}


	
	
}
