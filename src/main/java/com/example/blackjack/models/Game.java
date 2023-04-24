package com.example.blackjack.models;

import jakarta.persistence.*;
import java.util.Random;

@Entity
@Table(name = "games")
public class Game {

    @Id
    private long id;
    private double bet;
    private double money;
    private boolean ongoing;
    private String message;
    @OneToOne(cascade = CascadeType.ALL)
    private Deck deck;
    @OneToOne(cascade = CascadeType.ALL)
    private Hand playerHand;
    @OneToOne(cascade = CascadeType.ALL)
    private Hand dealerHand;

    protected Game() {}

    public Game(double bet) {
        this.id = new Random().nextLong(Long.MAX_VALUE);
        this.bet = bet;
        this.money = 0D;
        this.ongoing = true;
        this.deck = new Deck();
        this.playerHand = new Hand();
        this.dealerHand = new Hand();

        deck.dealFaceUp(playerHand);
        deck.dealFaceUp(dealerHand);
        deck.dealFaceUp(playerHand);
        deck.dealFaceDown(dealerHand);
    }

    public Long getId() {
        return id;
    }

    public String getMoney() {
        return (money >= 0 ? "$" : "-$") + String.format("%.2f", Math.abs(money));
    }

    public Deck getDeck() {
        return deck;
    }

    public Hand getPlayerHand() {
        return playerHand;
    }

    public Hand getDealerHand() {
        return dealerHand;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void winBet() {
        money += bet;
        ongoing = false;
    }

    public void winBlackjack() {
        money += bet * 1.5;
        ongoing = false;
    }

    public void loseBet() {
        money -= bet;
        ongoing = false;
    }

    public void tie() {
        ongoing = false;
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public Game dealAgain(double bet) {
        this.bet = bet;
        ongoing = true;

        deck = new Deck();
        playerHand = new Hand();
        dealerHand = new Hand();

        deck.dealFaceUp(playerHand);
        deck.dealFaceUp(dealerHand);
        deck.dealFaceUp(playerHand);
        deck.dealFaceDown(dealerHand);

        return this;
    }
}
