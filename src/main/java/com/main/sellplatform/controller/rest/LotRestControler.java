package com.main.sellplatform.controller.rest;

import com.main.sellplatform.controller.dto.LotDto;
import com.main.sellplatform.controller.dto.lotdto.LotFilterDTO;
import com.main.sellplatform.persistence.dao.UserDao;
import com.main.sellplatform.persistence.entity.Lot;
import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.service.LotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/lots")
public class LotRestControler {

    private final LotService lotService;
    private final UserDao userDao;


    @Autowired
    public LotRestControler(LotService lotService, UserDao userDao) {
        this.lotService = lotService;
        this.userDao = userDao;
    }

    @PreAuthorize("hasAnyAuthority('user:read')")
    @GetMapping("/myLots")
    public List<Lot> getMyLots() {
        return lotService.getMyLots(SecurityContextHolder.getContext().getAuthentication().getName(), null);
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('user:write')")
    @PostMapping(value = "/createLot", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void createLot(@RequestBody LotDto lotDto) {
        User userAuth = userDao.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Lot lot = lotDto.getLot(userAuth);
        lot.setCreationDate(new Date());
        lotService.createLot(lot);
    }

    @PreAuthorize("hasAnyAuthority('user:read')")
    @GetMapping(value = "/getLots")
    public ResponseEntity<List<Lot>> getLot(LotFilterDTO filterDTO) {
        List<Lot> publishedLot = lotService.getPublishedLot(filterDTO);
        publishedLot.forEach(System.out::println);
        for(Lot lot:publishedLot){
            lot.setOwner(null);
        }
        return ResponseEntity.ok(publishedLot);
    }



    @PostMapping(value = "/setUser")
    public ResponseEntity<?> updateCustomer(LotDto lotDto){
        User userAuth = userDao.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Lot lot = lotDto.getLot(userAuth);
        lot.setOwner(userAuth);
        lotService.updateCustomer(lot);
        return ResponseEntity.ok("ok");
    }


    @GetMapping("/buyableLot/{id}")
    public Lot getBuyableLot(@PathVariable Long id) {
        Lot buyableLot = lotService.getBuyableLot(id);
        return buyableLot;
    }

}
