package com.example.blackjack;

import com.example.blackjack.models.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackjackRepository extends CrudRepository<Game, Long> {}
