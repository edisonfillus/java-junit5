package org.project.example.service;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.project.example.model.Auction;
import org.project.example.model.Payment;
import org.project.example.persistence.interfaces.AuctionDAO;
import org.project.example.persistence.interfaces.PaymentDAO;

public class PaymentGenerator {

	private final PaymentDAO paymentDAO;
	private final AuctionDAO auctionDAO;
	private final Auctioneer auctioneer;
	private final ServerClock clock;

	
	public PaymentGenerator(AuctionDAO auctionDAO, PaymentDAO paymentDAO, Auctioneer auctioneer) {
		this(auctionDAO,paymentDAO,auctioneer, new RealServerClock());
	}
	
	public PaymentGenerator(AuctionDAO auctionDAO, PaymentDAO paymentDAO, Auctioneer auctioneer, ServerClock clock) {
		this.auctionDAO = auctionDAO;
		this.paymentDAO = paymentDAO;
		this.auctioneer = auctioneer;
		this.clock = clock;
	}
	

	public void generate() throws SQLException {

		List<Auction> leiloesEncerrados = auctionDAO.findFinishedAuctions();
		for (Auction leilao : leiloesEncerrados) {
			auctioneer.evaluate(leilao);

			Payment novoPagamento = new Payment(auctioneer.getMaiorLance(), nextWorkDay());
			paymentDAO.create(novoPagamento);
		}
	}

	private Calendar nextWorkDay() {
		Calendar data = clock.now();
		int diaDaSemana = data.get(Calendar.DAY_OF_WEEK);

		if (diaDaSemana == Calendar.SATURDAY)
			data.add(Calendar.DAY_OF_MONTH, 2);
		else if (diaDaSemana == Calendar.SUNDAY)
			data.add(Calendar.DAY_OF_MONTH, 1);

		return data;
	}
}
