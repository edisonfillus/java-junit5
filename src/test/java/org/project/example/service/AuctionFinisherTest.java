package org.project.example.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.mockito.InOrder;
import org.project.example.buider.AuctionBuilder;
import org.project.example.model.Auction;
import org.project.example.persistence.interfaces.AuctionDAO;

public class AuctionFinisherTest {

	@Test
	public void deveEncerrarLeiloesQueComecaramUmaSemanaAtras() throws SQLException {

		Calendar veryold = Calendar.getInstance();
		veryold.set(1999, 1, 20);

		Auction auction1 = new AuctionBuilder().to("TV de plasma").atDate(veryold).build();
		Auction auction2 = new AuctionBuilder().to("Geladeira").atDate(veryold).build();
		List<Auction> oldAuctions = Arrays.asList(auction1, auction2);

		// create mock
		AuctionDAO fakeDAO = mock(AuctionDAO.class);
		// Teach the mock to return list of auctions
		when(fakeDAO.findOpenAuctions()).thenReturn(oldAuctions);

        EmailSender fakeSender = mock(EmailSender.class);
		
		AuctionFinisher finisher = new AuctionFinisher(fakeDAO,fakeSender);
		finisher.finishOpenAuctions();

		assertTrue(auction1.isFinished());
		assertTrue(auction2.isFinished());
		assertEquals(2, finisher.getTotalFinished());
	}

	@Test
	public void naoDeveEncerrarLeiloesCasoNaoHajaNenhum() throws SQLException {

		AuctionDAO fakeDAO = mock(AuctionDAO.class);
		when(fakeDAO.findOpenAuctions()).thenReturn(new ArrayList<Auction>());

        EmailSender fakeSender = mock(EmailSender.class);
        
		AuctionFinisher finisher = new AuctionFinisher(fakeDAO,fakeSender);
		finisher.finishOpenAuctions();

		assertEquals(0, finisher.getTotalFinished());
	}

	@Test
	public void deveAtualizarLeiloesEncerrados() throws SQLException {

		Calendar veryold = Calendar.getInstance();
		veryold.set(1999, 1, 20);

		Auction auction1 = new AuctionBuilder().to("TV de plasma").atDate(veryold).build();

		AuctionDAO fakeDAO = mock(AuctionDAO.class);
		when(fakeDAO.findOpenAuctions()).thenReturn(Arrays.asList(auction1));

        EmailSender fakeSender = mock(EmailSender.class);
		
		AuctionFinisher finisher = new AuctionFinisher(fakeDAO,fakeSender);
		finisher.finishOpenAuctions();
		
		//Test with InOrder
		InOrder inOrder = inOrder(fakeDAO, fakeSender);
		inOrder.verify(fakeDAO, times(1)).update(auction1);
		inOrder.verify(fakeSender, times(1)).send(auction1);
				
	}
	
	
	@Test
    public void naoDeveEncerrarLeiloesQueComecaramMenosDeUmaSemanaAtras() throws SQLException {

        Calendar ontem = Calendar.getInstance();
        ontem.add(Calendar.DAY_OF_MONTH, -1);

        Auction leilao1 = new AuctionBuilder().to("TV de plasma")
            .atDate(ontem).build();
        Auction leilao2 = new AuctionBuilder().to("Geladeira")
            .atDate(ontem).build();

        AuctionDAO fakeDao = mock(AuctionDAO.class);
        when(fakeDao.findOpenAuctions()).thenReturn(Arrays.asList(leilao1, leilao2));
        
        EmailSender fakeSender = mock(EmailSender.class);

        AuctionFinisher encerrador = new AuctionFinisher(fakeDao,fakeSender);
        encerrador.finishOpenAuctions();
              
        assertEquals(0, encerrador.getTotalFinished());
        assertFalse(leilao1.isFinished());
        assertFalse(leilao2.isFinished());
        
        //Test if the update is never called
        verify(fakeDao, never()).update(leilao1);
        verify(fakeDao, never()).update(leilao2);
	
	}
	
	
	@Test
    public void deveContinuarExecucaoQuandoDAOFalha() throws SQLException {
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Auction leilao1 = new AuctionBuilder().to("TV de plasma")
            .atDate(antiga).build();
        Auction leilao2 = new AuctionBuilder().to("Geladeira")
            .atDate(antiga).build();

		AuctionDAO fakeDAO = mock(AuctionDAO.class);
        when(fakeDAO.findOpenAuctions()).thenReturn(Arrays.asList(leilao1, leilao2));

        doThrow(new SQLException()).when(fakeDAO).update(leilao1);

        EmailSender fakeSender = mock(EmailSender.class);
        AuctionFinisher encerrador = 
            new AuctionFinisher(fakeDAO, fakeSender);

        encerrador.finishOpenAuctions();
        
        //It shouldn't send the email for 1
        verify(fakeSender,never()).send(leilao1);
        
        //It should update and send email for 2
        verify(fakeDAO).update(leilao2);
        verify(fakeSender).send(leilao2);
        
    }
	
	@Test
    public void nuncaDeveEnviarEmailSeTodosDAOFalham() throws SQLException {
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Auction leilao1 = new AuctionBuilder().to("TV de plasma")
            .atDate(antiga).build();
        Auction leilao2 = new AuctionBuilder().to("Geladeira")
            .atDate(antiga).build();

		AuctionDAO fakeDAO = mock(AuctionDAO.class);
        when(fakeDAO.findOpenAuctions()).thenReturn(Arrays.asList(leilao1, leilao2));

        doThrow(new SQLException()).when(fakeDAO).update(any(Auction.class));
        
        EmailSender fakeSender = mock(EmailSender.class);
        AuctionFinisher encerrador = 
            new AuctionFinisher(fakeDAO, fakeSender);

        encerrador.finishOpenAuctions();
        
        //It shouldn't send any email
        verify(fakeSender,never()).send(any(Auction.class));
        
    }
	

	
	
}
