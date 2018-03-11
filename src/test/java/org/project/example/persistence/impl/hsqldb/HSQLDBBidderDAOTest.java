package org.project.example.persistence.impl.hsqldb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
	private BidderDAO usuarioDao;

	@Before
	public void beforeTests() {
		sessionFactory = Persistence.createEntityManagerFactory("hsqldb-test");
        em = sessionFactory.createEntityManager();
        usuarioDao = new HSQLDBBidderDAO(em);
        em.getTransaction().begin();        
	}
	
	@After
	public void afterTests() {
		em.getTransaction().rollback();  //Undo everything
		em.close();
	}
	
    @Test
    public void deveEncontrarPeloNomeEEmail() {
    	        
        String name = "teste";
        String email = "teste@example.com";
        		
        Bidder newBidder = new Bidder(name, email);
        
        
        usuarioDao.create(newBidder);
       
        Bidder persistedBidder = usuarioDao.findByNameEmail(name, email);
 
        assertEquals(newBidder.getId(), persistedBidder.getId());
        assertEquals(name, persistedBidder.getName());
        assertEquals(email, persistedBidder.getEmail());
    }
    
    @Test
    public void deveRetornarNuloSeNaoEncontrarUsuario() {
        
        String name = "notfound";
        String email = "notfound@example.com";
        		
        Bidder persistedBidder = usuarioDao.findByNameEmail(name, email);
 
        assertNull(email, persistedBidder);
    }
    
}
