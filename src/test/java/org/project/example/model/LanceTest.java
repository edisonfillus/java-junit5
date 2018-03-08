package org.project.example.model;

import org.junit.Test;
import org.project.example.model.Lance;
import org.project.example.model.Usuario;

public class LanceTest {
	
	@Test(expected = IllegalArgumentException.class)
	public void deveRecusarLancesComValorDeZero() {
		new Lance(new Usuario("John Doe"), 0.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void deveRecusarLancesComValorNegativo() {
		new Lance(new Usuario("John Doe"), -10.0);
	}
	

}
