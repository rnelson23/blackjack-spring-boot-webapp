package com.example.blackjack;

import com.example.blackjack.models.Game;
import org.springframework.data.repository.CrudRepository;

public interface BlackjackRepository extends CrudRepository<Game, Long> {}
