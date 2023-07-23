package com.aayush.springsecurity.controller;

import com.aayush.springsecurity.entity.Contact;
import com.aayush.springsecurity.repository.ContactRepository;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
public class ContactController {

  private final ContactRepository contactRepository;

  public ContactController(ContactRepository contactRepository) {
    this.contactRepository = contactRepository;
  }


  @PostMapping("/contact")
  @PreFilter("filterObject.contactName != 'Test'") // only works if the input is a list

  public List<Contact> saveContactInquiryDetails(@RequestBody List<Contact> contacts) {
    Contact contact = contacts.get(0);
    contact.setContactId(getServiceReqNumber());
    contact.setCreateDt(new Date(System.currentTimeMillis()));
    contact = contactRepository.save(contact);
    return Arrays.asList(contact);
  }

  public String getServiceReqNumber() {
    Random random = new Random();
    int ranNum = random.nextInt(999999999 - 9999) + 9999;
    return "SR"+ranNum;
  }
}
