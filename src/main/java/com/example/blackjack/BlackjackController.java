package com.example.blackjack;

import com.example.blackjack.models.Deck;
import com.example.blackjack.models.Game;
import com.example.blackjack.models.Hand;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
public class BlackjackController {

    private final BlackjackRepository repository;

    public BlackjackController(BlackjackRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/deal")
    public String deal(@RequestParam int bet) {
        Game game = new Game(new Random().nextLong(Long.MAX_VALUE), bet);

        Hand playerHand = game.getPlayerHand();
        Hand dealerHand = game.getDealerHand();

        if (playerHand.isBlackjack() && !dealerHand.isBlackjack()) {
            game.setMessage("Player has blackjack! Player wins!");
            game.winBlackjack();

        } else if (!playerHand.isBlackjack() && dealerHand.isBlackjack()) {
            game.setMessage("Dealer has blackjack! Dealer wins!");
            game.winBet();

        } else if (playerHand.isBlackjack() && dealerHand.isBlackjack()) {
            game.setMessage("Both have blackjack! Stand-off!");
        }

        repository.save(game);
        return "redirect:/" + game.getId() + "/game";
    }

    @GetMapping("/{id}/deal")
    public String dealAgain(@PathVariable long id, @RequestParam int bet) {
        Game game = repository.findById(id).orElseThrow().dealAgain(bet);

        repository.save(game);
        return "redirect:/" + game.getId() + "/game";
    }

    @GetMapping("/{id}/game")
    public String game(@PathVariable long id, Model model) {
        Game game = repository.findById(id).orElseThrow();

        model.addAttribute("playerHand", game.getPlayerHand());
        model.addAttribute("dealerHand", game.getDealerHand());
        model.addAttribute("message", game.getMessage());
        model.addAttribute("money", game.getMoney());

        return game.isOngoing() ? "game" : "end";
    }

    @GetMapping("/{id}/hit")
    public String hit(@PathVariable long id) {
        Game game = repository.findById(id).orElseThrow();
        game.getDeck().dealFaceUp(game.getPlayerHand());

        if (game.getPlayerHand().getValue() > 21) {
            game.setMessage("Player busts! Dealer wins!");
            game.loseBet();
        }

        repository.save(game);
        return "redirect:/" + game.getId() + "/game";
    }

    @GetMapping("/{id}/stand")
    public String stand(@PathVariable long id) {
        Game game = repository.findById(id).orElseThrow();

        Deck deck = game.getDeck();
        Hand dealerHand = game.getDealerHand();
        Hand playerHand = game.getPlayerHand();

        dealerHand.flipLastCard();

        while (dealerHand.getValue() < 17) {
            deck.dealFaceUp(dealerHand);
        }

        if (dealerHand.getValue() > 21) {
            game.setMessage("Dealer busts! Player wins!");
            game.winBet();

        } else if (playerHand.getValue() > dealerHand.getValue()) {
            game.setMessage("Player wins!");
            game.winBet();

        } else if (playerHand.getValue() < dealerHand.getValue()) {
            game.setMessage("Dealer wins!");
            game.loseBet();

        } else {
            game.setMessage("Stand-off!");
            game.tieBet();
        }

        repository.save(game);
        return "redirect:/" + game.getId() + "/game";
    }
}
