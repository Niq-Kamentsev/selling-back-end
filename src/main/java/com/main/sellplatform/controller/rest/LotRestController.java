package com.main.sellplatform.controller.rest;

import com.main.sellplatform.controller.dto.lotdto.LotFilterDTO;
import com.main.sellplatform.persistence.entity.Lot;
import com.main.sellplatform.service.LotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/lots")
public class LotRestController {
    private final LotService lotService;

    @Autowired
    public LotRestController(LotService lotService) {
        this.lotService = lotService;
    }

    @PreAuthorize("hasAnyAuthority('user:read')")
    @GetMapping("/user/{username}")
    public List<com.main.sellplatform.entitymanager.testobj.Lot> getUserLots(@PathVariable String username) {
        return lotService.getUserLots(username,null);
    }

    @PreAuthorize("hasAnyAuthority('user:read')")
    @GetMapping("/myLots")
    public List<com.main.sellplatform.entitymanager.testobj.Lot> getMyLots() {
        return lotService.getMyLots(SecurityContextHolder.getContext().getAuthentication().getName(),null);
    }

    @PreAuthorize("hasAnyAuthority('user:read')")
    @GetMapping("/myLots/{sortCol}")
    public List<com.main.sellplatform.entitymanager.testobj.Lot> getMySortLots(@PathVariable LotFilterDTO.Column sortCol) {
        return lotService.getMyLots(SecurityContextHolder.getContext().getAuthentication().getName(),sortCol);
    }

    @GetMapping
    public List<Lot> getAllLots() {
        return lotService.getAllLots();
    }

    @PreAuthorize("hasAnyAuthority('user:write')")
    @PostMapping("/addLot")
    public Boolean addLot(@RequestBody Lot lot) {
        return lotService.addLot(lot, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PreAuthorize("hasAnyAuthority('user:write')")
    @PutMapping("/myLots/update")
    public Boolean updateLot(@RequestBody Lot lot) {
        return lotService.updateLot(lot, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PreAuthorize("hasAnyAuthority('user:delete')")
    @DeleteMapping( "/myLots/delete/{id}")
    public Boolean deleteLot(@PathVariable Long id) {
        return lotService.deleteLot(id, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/search/{keyword}")
    public List<Lot> findLots(@PathVariable String keyword) {
        return lotService.findLots(keyword);
    }

    @GetMapping("/buyableLots")
    public List<com.main.sellplatform.entitymanager.testobj.Lot> getBuyableLots(
            LotFilterDTO filter
            ){
        return lotService.getBuyableLots(filter);
    }
    @GetMapping("/buyableLot/{id}")
    public com.main.sellplatform.entitymanager.testobj.Lot getBuyableLot(@PathVariable Long id){
        return lotService.getBuyableLot(id);
    }
}
