package com.aayush.springsecurity.controller;

import com.aayush.springsecurity.entity.Card;
import com.aayush.springsecurity.repository.CardRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardsController {

  private final CardRepository cardRepository;

  public CardsController(CardRepository cardRepository) {
    this.cardRepository = cardRepository;
  }

  @GetMapping("/myCards")
  public List<Card> getCardDetails(@RequestParam int id) {
    return cardRepository.findByCustomerId(id);

  }
}
