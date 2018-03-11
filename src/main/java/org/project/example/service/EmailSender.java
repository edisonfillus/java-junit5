package org.project.example.service;

import org.project.example.model.Auction;

public interface EmailSender {
    void send(Auction auction);
}
