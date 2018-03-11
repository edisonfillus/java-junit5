package org.project.example.persistence.impl.hsqldb;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.project.example.model.Bidder;
import org.project.example.persistence.interfaces.BidderDAO;

public class HSQLDBBidderDAO implements BidderDAO {

	private final EntityManager em;

	public HSQLDBBidderDAO(EntityManager em) {
	        this.em = em;
	    }

	/* (non-Javadoc)
	 * @see org.project.example.persistence.impl.hsqldb.BidderDAO#findById(int)
	 */
	@Override
	public Bidder findById(int id) {
		return em.find(Bidder.class, id);
	}

	/* (non-Javadoc)
	 * @see org.project.example.persistence.impl.hsqldb.BidderDAO#findByNameEmail(java.lang.String, java.lang.String)
	 */
	@Override
	public Bidder findByNameEmail(String name, String email) {
		Bidder found = null;
		try {
			found = (Bidder) em.createQuery(
				  "SELECT b "
				+ "FROM Bidder b "
				+ "WHERE b.name = :name and b.email = :email")
				.setParameter("name", name).setParameter("email", email)
				.getSingleResult();
		} catch (NoResultException e) {
			//Do nothing, just return null.
		} 
		return found;
		
	}

	/* (non-Javadoc)
	 * @see org.project.example.persistence.impl.hsqldb.BidderDAO#create(org.project.example.model.Bidder)
	 */
	@Override
	public void create(Bidder bidder) {
		em.persist(bidder);
	}
}
