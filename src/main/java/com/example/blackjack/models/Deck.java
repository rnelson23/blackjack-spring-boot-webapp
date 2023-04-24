package com.example.blackjack.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
public class Deck {

    @Id
    @GeneratedValue
    private long id;
    @OneToMany(cascade = CascadeType.ALL)
    private final List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();

        for (String suit : new String[]{"♣", "♦", "♥", "♠"}) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    public void dealFaceUp(Hand hand) {
        hand.getCards().add(cards.remove(new Random().nextInt(cards.size())).flip());
    }

    public void dealFaceDown(Hand hand) {
        hand.getCards().add(cards.remove(new Random().nextInt(cards.size())));
    }
}
