package com.main.sellplatform.controller.rest;

import com.main.sellplatform.persistence.entity.Lot;
import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.service.LotService;
import com.main.sellplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/lots")
public class LotRestController {
    private final LotService lotService;
    private final UserService userService;

    @Autowired
    public LotRestController(LotService lotService, UserService userService) {
        this.lotService = lotService;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyAuthority('user:read')")
    @GetMapping("/{id}")
    public List<Lot> getUserLots(@PathVariable Long id) {
        User user = userService.getUser(id);
        if (user != null) {
            return lotService.getUserLots(id);
        }
        return null;
    }

    @PreAuthorize("hasAnyAuthority('user:read')")
    @GetMapping
    public List<Lot> getAllLots() {
        return lotService.getAllLots();
    }

    @PreAuthorize("hasAnyAuthority('user:read')")
    @PostMapping( "/addLot")
    public Boolean addLot(@RequestBody Lot lot) {
        return lotService.addLot(lot, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PreAuthorize("hasAnyAuthority('user:read')")
    @PutMapping("/update")
    public Boolean updateLot(@RequestBody Lot lot) {
        return lotService.updateLot(lot);
    }

    @PreAuthorize("hasAnyAuthority('user:delete')")
    @DeleteMapping( "/delete/{id}")
    public Boolean deleteLot(@PathVariable Long id) {
        return lotService.deleteLot(id);
    }

    @PreAuthorize("hasAnyAuthority('user:read')")
    @GetMapping("/moderation")
    public List<Lot> getAllModeratingLots() {
        return lotService.getAllModeratingLots();
    }

    @PreAuthorize("hasAnyAuthority('user:read')")
    @PostMapping(value = "/moderation", params = "publish")
    public Boolean publishLot(@RequestBody Lot lot) {
        return lotService.publishLot(lot, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PreAuthorize("hasAnyAuthority('user:read')")
    @PostMapping(value = "/moderation", params = "reject")
    public Boolean rejectLot(@RequestBody Lot lot) {
        return lotService.rejectLot(lot, SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
