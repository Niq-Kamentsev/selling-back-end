package com.main.sellplatform.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.sellplatform.entitymanager.EntityManager;
import com.main.sellplatform.entitymanager.GeneralObject;
import com.main.sellplatform.entitymanager.annotation.Objtype;
import com.main.sellplatform.entitymanager.testobj.Bid;
import com.main.sellplatform.persistence.entity.Lot;
import com.main.sellplatform.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/bids")
public class BidRestController {

    @Autowired
    BidService bidService;

    @Autowired
    EntityManager entityManager;

    @PreAuthorize("hasAnyAuthority('user:write')")
    @PostMapping("/makeBid")
    public Boolean makeBid(@RequestBody Bid bid) {
        return bidService.makeBid(bid, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PreAuthorize("hasAnyAuthority('user:write')")
    @PostMapping("/makeBid/{email}")
    public Boolean makeBid(@RequestBody Bid bid, @PathVariable String email) throws JsonProcessingException {
        return bidService.makeBid(bid, email);
    }

    @PostMapping("/delete/{id}")
    public void delete(@PathVariable Long id) throws JsonProcessingException {
        GeneralObject object = new GeneralObject();object.setId(id);
        entityManager.delete(object);
    }
}
