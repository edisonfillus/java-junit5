package org.project.example.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.project.example.buider.AuctionBuilder;
import org.project.example.model.Auction;
import org.project.example.persistence.interfaces.AuctionDAO;

public class AuctionFinisherTest {

	@Test
	public void deveEncerrarLeiloesQueComecaramUmaSemanaAtras() {

		Calendar veryold = Calendar.getInstance();
		veryold.set(1999, 1, 20);

		Auction auction1 = new AuctionBuilder().to("TV de plasma").atDate(veryold).build();
		Auction auction2 = new AuctionBuilder().to("Geladeira").atDate(veryold).build();
		List<Auction> oldAuctions = Arrays.asList(auction1, auction2);

		// create mock
		AuctionDAO fakeDAO = mock(AuctionDAO.class);
		// Teach the mock to return list of auctions
		when(fakeDAO.openAuctions()).thenReturn(oldAuctions);

		AuctionFinisher finisher = new AuctionFinisher(fakeDAO);
		finisher.finishOpenAuctions();

		assertTrue(auction1.isFinished());
		assertTrue(auction2.isFinished());
		assertEquals(2, finisher.getTotalFinished());
	}

	@Test
	public void naoDeveEncerrarLeiloesCasoNaoHajaNenhum() {

		AuctionDAO fakeDAO = mock(AuctionDAO.class);
		when(fakeDAO.openAuctions()).thenReturn(new ArrayList<Auction>());

		AuctionFinisher finisher = new AuctionFinisher(fakeDAO);
		finisher.finishOpenAuctions();

		assertEquals(0, finisher.getTotalFinished());
	}

	@Test
	public void deveAtualizarLeiloesEncerrados() {

		Calendar veryold = Calendar.getInstance();
		veryold.set(1999, 1, 20);

		Auction auction1 = new AuctionBuilder().to("TV de plasma").atDate(veryold).build();

		AuctionDAO fakeDAO = mock(AuctionDAO.class);
		when(fakeDAO.openAuctions()).thenReturn(Arrays.asList(auction1));

		AuctionFinisher finisher = new AuctionFinisher(fakeDAO);
		finisher.finishOpenAuctions();

		verify(fakeDAO, times(1)).update(auction1);
	}

}
