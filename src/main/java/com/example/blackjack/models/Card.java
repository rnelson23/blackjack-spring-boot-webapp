package com.example.blackjack.models;

import jakarta.persistence.*;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue
    private long id;
    private Rank rank;
    private boolean flipped;
    private String face;

    protected Card() {}

    public Card(Rank rank, String suit) {
        this.rank = rank;
        this.flipped = false;

        this.face = String.format("""
                ┌───────────┐
                │ %s      %s │
                │           │
                │           │
                │     %s     │
                │           │
                │           │
                │ %s      %s │
                └───────────┘""", rank.rightPad(), suit, suit, suit, rank.leftPad());
    }

    public Rank getRank() {
        return rank;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public Card flip() {
        flipped = !flipped;
        return this;
    }

    @Override
    public String toString() {
        if (flipped) { return face; }

        return """
                ┌───────────┐
                │ ░░░░░░░░░ │
                │ ░░░░░░░░░ │
                │ ░░░░░░░░░ │
                │ ░░░░░░░░░ │
                │ ░░░░░░░░░ │
                │ ░░░░░░░░░ │
                │ ░░░░░░░░░ │
                └───────────┘""";
    }
}
