package com.main.sellplatform.controller.rest;

import com.main.sellplatform.persistence.entity.Lot;
import com.main.sellplatform.service.LotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Lot> getUserLots(@PathVariable String username) {
        return lotService.getUserLots(username, false);
    }

    @PreAuthorize("hasAnyAuthority('user:read')")
    @GetMapping("/myLots")
    public List<Lot> getMyLots() {
        return lotService.getUserLots(SecurityContextHolder.getContext().getAuthentication().getName(), true);
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

    @PreAuthorize("hasAnyAuthority('user:read')")
    @GetMapping("/category/{category}")
    public List<Lot> getLotsFromCategory(@PathVariable String category) {
        return lotService.getLotsFromCategory(category, null);
    }

    @PreAuthorize("hasAnyAuthority('user:read')")
    @GetMapping("/category/{category}/search{keyword}")
    public List<Lot> findLotsInCategory(@PathVariable String category, @PathVariable String keyword) {
        return lotService.getLotsFromCategory(category, keyword);
    }
}