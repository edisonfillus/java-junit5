package org.project.example.persistence.impl.hsqldb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.project.example.model.Bidder;
import org.project.example.persistence.interfaces.BidderDAO;

public class HSQLDBBidderDAOTest {

	private EntityManagerFactory sessionFactory;
	private EntityManager em;
	private BidderDAO bidderDAO;

	@Before
	public void beforeTests() {
		sessionFactory = Persistence.createEntityManagerFactory("hsqldb-test");
		em = sessionFactory.createEntityManager();
		bidderDAO = new HSQLDBBidderDAO(em);
		em.getTransaction().begin();
	}

	@After
	public void afterTests() {
		em.getTransaction().rollback(); // Undo everything
		em.close();
	}

	@Test
	public void deveEncontrarPeloNomeEEmail() {

		String name = "teste";
		String email = "teste@example.com";

		Bidder newBidder = new Bidder(name, email);

		bidderDAO.create(newBidder);

		Bidder persistedBidder = bidderDAO.findByNameEmail(name, email);

		assertEquals(newBidder.getId(), persistedBidder.getId());
		assertEquals(name, persistedBidder.getName());
		assertEquals(email, persistedBidder.getEmail());
	}

	@Test
	public void deveRetornarNuloSeNaoEncontrarUsuario() {

		String name = "notfound";
		String email = "notfound@example.com";

		Bidder persistedBidder = bidderDAO.findByNameEmail(name, email);

		assertNull(email, persistedBidder);
	}

	@Test
	public void shouldDeleteBidder() {
		// Create Bidder
		Bidder bidder = new Bidder("Mauricio Aniche", "mauricio@aniche.com.br");
		bidderDAO.create(bidder);

		// Force Hibernate to send commands to database
		em.flush();
		em.clear();

		// Get Bidder from database
		Bidder bidderOnDB = bidderDAO.findByNameEmail("Mauricio Aniche", "mauricio@aniche.com.br");

		// Assert creation
		assertNotNull(bidderOnDB);

		// Remove Bidder from database
		bidderDAO.remove(bidderOnDB);

		// Force Hibernate to send commands to database
		em.flush();
		em.clear();

		// Try to find removed Bidder
		Bidder bidderRemoved = bidderDAO.findByNameEmail("Mauricio Aniche", "mauricio@aniche.com.br");

		// Assert it not exist anymore
		assertNull(bidderRemoved);

	}

	@Test
	public void shouldUpdateBidder() {
		//Create Bidder
		Bidder bidder = new Bidder("Mauricio Aniche", "mauricio@aniche.com.br");
		bidderDAO.create(bidder);

		//Change values
		bidder.setName("João da Silva");
		bidder.setEmail("joao@silva.com.br");

		// Force Hibernate to send commands to database, update should be triggered automatically
		em.flush();
		em.clear();
		
		//Should find updated Bidder
		Bidder updatedBidder = bidderDAO.findByNameEmail("João da Silva", "joao@silva.com.br");
		assertNotNull(updatedBidder);

		//Should not find Bidder with old values
		Bidder oldBidder = bidderDAO.findByNameEmail("Mauricio Aniche", "mauricio@aniche.com.br");
		assertNull(oldBidder);

	}

}
