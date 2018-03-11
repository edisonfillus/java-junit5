package org.project.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Bidder {

	@Id @GeneratedValue
	private int id;
	private String name;
	private String email;
	
	public Bidder() {
		super();
	}
	
	public Bidder(String nome) {
		this(0, nome);
	}

	public Bidder(int id, String name) {
		this.id = id;
		this.setName(name);
	}

	public Bidder(String name, String email) {
		this.setName(name);
		this.setEmail(email);
	}
	

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
