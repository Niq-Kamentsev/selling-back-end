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
    @GetMapping("/user/{username}")
    public List<Lot> getUserLots(@PathVariable String username) {
        User user = userService.getUserByEmail(username);
        if (user != null) {
            return lotService.getUserLots(user.getId());
        }
        return null;
    }

    @PreAuthorize("hasAnyAuthority('user:read')")
    @GetMapping("/myLots")
    public List<Lot> getMyLots() {
        return lotService.getMyLots(SecurityContextHolder.getContext().getAuthentication().getName());
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
    @DeleteMapping( "/myLots/delete")
    public Boolean deleteLot(@RequestBody Lot lot) {
        return lotService.deleteLot(lot, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/search/{keyword}")
    public List<Lot> findLots(@PathVariable String keyword) {
        return lotService.findLots(keyword);
    }
}
