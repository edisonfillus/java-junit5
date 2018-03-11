package org.project.example.service;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.project.example.buider.AuctionBuilder;
import org.project.example.model.Auction;
import org.project.example.model.Bidder;
import org.project.example.model.Payment;
import org.project.example.persistence.interfaces.AuctionDAO;
import org.project.example.persistence.interfaces.PaymentDAO;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;

public class PaymentGereratorTest {
	@Test
	public void deveGerarPagamentoParaUmLeilaoEncerrado() throws SQLException {

		AuctionDAO auctionDAO = mock(AuctionDAO.class);
		PaymentDAO paymentDAO = mock(PaymentDAO.class);

		Auction auction = new AuctionBuilder().to("Playstation").bid(new Bidder("José da Silva"), 2000.0)
				.bid(new Bidder("Maria Pereira"), 2500.0).build();

		when(auctionDAO.findFinishedAuctions()).thenReturn(Arrays.asList(auction));

		PaymentGenerator generator = new PaymentGenerator(auctionDAO, paymentDAO, new Auctioneer());
		generator.generate();

		ArgumentCaptor<Payment> argumento = ArgumentCaptor.forClass(Payment.class);
		verify(paymentDAO).create(argumento.capture());
		Payment pagamentoGerado = argumento.getValue();

		assertEquals(2500.0, pagamentoGerado.getValue(), 0.00001);
	}

	@Test
	public void deveEmpurrarParaOProximoDiaUtil() throws SQLException {
		AuctionDAO auctionDAO = mock(AuctionDAO.class);
		PaymentDAO paymentDAO = mock(PaymentDAO.class);
		ServerClock clock = mock(ServerClock.class);

		// date 7/april/2012 - saturday
		Calendar sabado = Calendar.getInstance();
		sabado.set(2012, Calendar.APRIL, 7);

		// teach mock to say that "today" is saturday!
		when(clock.now()).thenReturn(sabado);

		Auction auction = new AuctionBuilder().to("Playstation").bid(new Bidder("José da Silva"), 2000.0)
				.bid(new Bidder("Maria Pereira"), 2500.0).build();

		

		when(auctionDAO.findFinishedAuctions()).thenReturn(Arrays.asList(auction));

		PaymentGenerator generator = new PaymentGenerator(auctionDAO, paymentDAO, new Auctioneer(),clock);
		generator.generate();
		
		ArgumentCaptor<Payment> argumento = ArgumentCaptor.forClass(Payment.class);
		verify(paymentDAO).create(argumento.capture());
		Payment pagamentoGerado = argumento.getValue();

		assertEquals(Calendar.MONDAY, pagamentoGerado.getDate().get(Calendar.DAY_OF_WEEK));
		assertEquals(9, pagamentoGerado.getDate().get(Calendar.DAY_OF_MONTH));
	}

}
