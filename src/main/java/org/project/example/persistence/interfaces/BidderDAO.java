package org.project.example.persistence.interfaces;

import org.project.example.model.Bidder;

public interface BidderDAO {

	Bidder findById(int id);

	Bidder findByNameEmail(String name, String email);

	void create(Bidder bidder);

}