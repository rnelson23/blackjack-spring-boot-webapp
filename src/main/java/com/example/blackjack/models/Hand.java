package com.example.blackjack.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Hand {

    @Id
    @GeneratedValue
    private long id;
    @OneToMany(cascade = CascadeType.ALL)
    private final List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public void flipLastCard() {
        cards.get(cards.size() - 1).flip();
    }

    public boolean isBlackjack() {
        for (Card card : cards) {
            Rank rank = card.getRank();

            if (rank != Rank.ACE && rank != Rank.TEN) {
                return false;
            }
        }

        return true;
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getValue() {
        int value = 0;
        int numAces = 0;

        for (Card card : cards) {
            if (!card.isFlipped()) { continue; }
            Rank rank = card.getRank();

            if (rank == Rank.ACE) { numAces++; }
            value += rank.getValue();
        }

        while (value > 21 && numAces > 0) {
            value -= 10;
            numAces--;
        }

        return value;
    }
}
