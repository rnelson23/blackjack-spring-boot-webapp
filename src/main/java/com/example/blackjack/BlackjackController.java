package com.example.blackjack;

import com.example.blackjack.models.Deck;
import com.example.blackjack.models.Game;
import com.example.blackjack.models.Hand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BlackjackController {

    @Autowired
    private BlackjackRepository repository;

    @GetMapping("/deal")
    public String deal(@RequestParam double bet) {
        Game game = new Game(bet);

        Hand playerHand = game.getPlayerHand();
        Hand dealerHand = game.getDealerHand();

        if (playerHand.isBlackjack() && !dealerHand.isBlackjack()) {
            game.setMessage("Player has blackjack! Player wins!");
            game.winBlackjack();

        } else if (!playerHand.isBlackjack() && dealerHand.isBlackjack()) {
            game.setMessage("Dealer has blackjack! Dealer wins!");
            game.loseBet();

        } else if (playerHand.isBlackjack() && dealerHand.isBlackjack()) {
            game.setMessage("Both have blackjack! Stand-off!");
            game.tie();
        }

        repository.save(game);
        return "redirect:/games/" + game.getId();
    }

    @GetMapping("/games/{id}")
    public String game(@PathVariable long id, Model model) {
        Game game = repository.findById(id).orElseThrow();

        model.addAttribute("playerHand", game.getPlayerHand());
        model.addAttribute("dealerHand", game.getDealerHand());
        model.addAttribute("message", game.getMessage());
        model.addAttribute("ongoing", game.isOngoing());
        model.addAttribute("money", game.getMoney());
        model.addAttribute("id", game.getId());

        return "game";
    }

    @GetMapping("/games/{id}/deal")
    public String dealAgain(@PathVariable long id, @RequestParam double bet) {
        Game game = repository.findById(id).orElseThrow();

        repository.save(game.dealAgain(bet));
        return "redirect:/games/" + id;
    }

    @PostMapping("/games/{id}/hit")
    public String hit(@PathVariable long id) {
        Game game = repository.findById(id).orElseThrow();

        Deck deck = game.getDeck();
        Hand playerHand = game.getPlayerHand();

        deck.dealFaceUp(playerHand);

        if (playerHand.getValue() > 21) {
            game.setMessage("Player busts! Dealer wins!");
            game.loseBet();
        }

        repository.save(game);
        return "redirect:/games/" + id;
    }

    @PostMapping("/games/{id}/stand")
    public String stand(@PathVariable long id) {
        Game game = repository.findById(id).orElseThrow();

        Deck deck = game.getDeck();
        Hand playerHand = game.getPlayerHand();
        Hand dealerHand = game.getDealerHand();

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
            game.tie();
        }

        repository.save(game);
        return "redirect:/games/" + id;
    }
}
