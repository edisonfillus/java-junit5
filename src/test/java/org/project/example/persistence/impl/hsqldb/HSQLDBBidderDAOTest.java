package org.project.example.persistence.impl.hsqldb;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;
import org.project.example.model.Bidder;

public class HSQLDBBidderDAOTest {
	
    @Test
    public void deveEncontrarPeloNomeEEmail() {
    	EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("hsqldb");
        EntityManager em = sessionFactory.createEntityManager();
        HSQLDBBidderDAO usuarioDao = new HSQLDBBidderDAO(em);
        
        
        String name = "teste";
        String email = "teste@example.com";
        		
        // criando um usuario e salvando antes
        // de invocar o porNomeEEmail
        Bidder newBidder = new Bidder(name, email);
        
        em.getTransaction().begin();
        usuarioDao.create(newBidder);
        em.getTransaction().commit();
       
        Bidder persistedBidder = usuarioDao.findByNameEmail(name, email);
 
        assertEquals(newBidder.getId(), persistedBidder.getId());
        assertEquals(name, persistedBidder.getName());
        assertEquals(email, persistedBidder.getEmail());
        
        em.close();
    }
}
