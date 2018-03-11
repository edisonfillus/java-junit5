package org.project.example.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.project.example.buider.AuctionBuilder;
import org.project.example.model.Bid;
import org.project.example.model.Auction;
import org.project.example.model.Bidder;
import org.project.example.service.Auctioneer;

public class AuctioneerTest {

	private Auctioneer auctioneer;
	private Bidder john;
	private Bidder joseph;
	private Bidder maria;
	private Bidder peter;

	@Before
	public void createAuction() {
		this.auctioneer = new Auctioneer();
		this.john = new Bidder("John");
		this.joseph = new Bidder("Joseph");
		this.maria = new Bidder("Maria");
		this.peter = new Bidder("Peter");

	}

	@Test
	public void deveEntenderLancesEmOrdemCrescente() {
		
        Auction leilao = new AuctionBuilder().to("Playstation 3 Novo")
                .bid(john, 300.0)
                .bid(maria, 250.0)
                .bid(joseph, 400.0)
                .build();

		// executando a acao
		auctioneer.evaluate(leilao);

		// comparando a saida com o esperado
		Assert.assertEquals(400.0, auctioneer.getMaiorLance(), 0.0001);
		Assert.assertEquals(250.0, auctioneer.getMenorLance(), 0.0001);
	}

	@Test
	public void deveEntenderLancesEmOrdemCrescenteComOutrosValores() {
		Auction leilao = new AuctionBuilder().to("Playstation 3 Novo")
				.bid(maria, 1000.0)
				.bid(john, 2000.0)
				.bid(joseph, 3000.0)
				.build();

		auctioneer.evaluate(leilao);

		assertEquals(3000.0, auctioneer.getMaiorLance(), 0.0001);
		assertEquals(1000.0, auctioneer.getMenorLance(), 0.0001);
	}

	@Test
	public void deveEntenderLeilaoComApenasUmLance() {
		Auction leilao = new AuctionBuilder().to("Playstation 3 Novo")
				.bid(john, 1000.0)
				.build();
		
		auctioneer.evaluate(leilao);

		assertEquals(1000.0, auctioneer.getMaiorLance(), 0.0001);
		assertEquals(1000.0, auctioneer.getMenorLance(), 0.0001);
	}

	@Test
	public void deveEntenderLancesEmOrdemAleatória() {

		Auction leilao = new AuctionBuilder().to("Playstation 3 Novo")
				.bid(maria, 500.0)
				.bid(john, 2000.0)
				.bid(joseph, 700.0)
				.bid(peter, 100.0)
				.build();

		auctioneer.evaluate(leilao);
		List<Bid> maiores = auctioneer.getTresMaiores();

		assertEquals(2000.0, auctioneer.getMaiorLance(), 0.0001);
		assertEquals(100.0, auctioneer.getMenorLance(), 0.0001);
		assertEquals(3, maiores.size());

		assertEquals(2000.0, maiores.get(0).getValue(), 0.0001);
		assertEquals(700.0, maiores.get(1).getValue(), 0.0001);
		assertEquals(500.0, maiores.get(2).getValue(), 0.0001);
	}

	@Test
	public void deveEncontrarOsTresMaioresLances() {
		
		Auction leilao = new AuctionBuilder().to("Playstation 3 Novo")
				.bid(john, 100.0)
				.bid(maria, 200.0)
				.bid(john, 300.0)
				.bid(maria, 400.0)
				.build();
		
		auctioneer.evaluate(leilao);

		List<Bid> maiores = auctioneer.getTresMaiores();

		assertEquals(3, maiores.size());
		assertEquals(400.0, maiores.get(0).getValue(), 0.0001);
		assertEquals(300.0, maiores.get(1).getValue(), 0.0001);
		assertEquals(200.0, maiores.get(2).getValue(), 0.0001);
	}

	
	@Test
	public void deveDevolverTodosLancesCasoNaoHajaNoMinimo3() {
		Auction leilao = new AuctionBuilder().to("Playstation 3 Novo")
				.bid(john, 100.0)
				.bid(maria, 200.0)
				.build();
		
		auctioneer.evaluate(leilao);

		List<Bid> maiores = auctioneer.getTresMaiores();

		assertEquals(2, maiores.size());
		assertEquals(200.0, maiores.get(0).getValue(), 0.00001);
		assertEquals(100.0, maiores.get(1).getValue(), 0.00001);
	}
	
	@Test(expected=RuntimeException.class)
	public void naoDeveAvaliarLeiloesSemNenhumLanceDado() {
	    Auction leilao = new AuctionBuilder()
	        .to("Playstation 3 Novo")
	        .build();
	    auctioneer.evaluate(leilao);
	    
	}

	


}
