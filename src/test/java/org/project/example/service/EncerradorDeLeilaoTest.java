package org.project.example.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.project.example.buider.LeilaoBuilder;
import org.project.example.dao.LeilaoDAO;
import org.project.example.model.Leilao;

public class EncerradorDeLeilaoTest {
	@Test
	public void deveEncerrarLeiloesQueComecaramUmaSemanaAtras() {

		Calendar antiga = Calendar.getInstance();
		antiga.set(1999, 1, 20);

		Leilao leilao1 = new LeilaoBuilder().para("TV de plasma").naData(antiga).constroi();
		Leilao leilao2 = new LeilaoBuilder().para("Geladeira").naData(antiga).constroi();
		List<Leilao> leiloesAntigos = Arrays.asList(leilao1, leilao2);

		// criamos o mock
		LeilaoDAO daoFalso = mock(LeilaoDAO.class);
		// ensinamos ele a retornar a lista de leiloes antigos
		when(daoFalso.correntes()).thenReturn(leiloesAntigos);

		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso);
		encerrador.encerra();

		assertTrue(leilao1.isEncerrado());
		assertTrue(leilao2.isEncerrado());
		assertEquals(2, encerrador.getTotalEncerrados());
	}

}
