package org.project.example.persistence.impl.hsqldb;

import javax.persistence.EntityManager;

import org.project.example.model.Bidder;

public class HSQLDBBidderDAO {

	private final EntityManager em;

	public HSQLDBBidderDAO(EntityManager em) {
	        this.em = em;
	    }

	public Bidder findById(int id) {
		return em.find(Bidder.class, id);
	}

	public Bidder findByNameEmail(String name, String email) {
		return (Bidder) em.createQuery(
				  "SELECT b "
				+ "FROM Bidder b "
				+ "WHERE b.name = :name and b.email = :email")
				.setParameter("name", name).setParameter("email", email)
				.getSingleResult();
	}

	public void create(Bidder bidder) {
		em.persist(bidder);
	}
}
