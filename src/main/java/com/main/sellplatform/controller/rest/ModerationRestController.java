package com.main.sellplatform.controller.rest;

import com.main.sellplatform.persistence.entity.Lot;
import com.main.sellplatform.persistence.entity.ModeratingLot;
import com.main.sellplatform.service.ModerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/lots")
public class ModerationRestController {
    private final ModerationService moderationService;

    @Autowired
    public ModerationRestController(ModerationService moderationService) {
        this.moderationService = moderationService;
    }

    @PreAuthorize("hasAnyAuthority('moder:read')")
    @GetMapping("/moderation")
    public List<Lot> getAllModeratingLots() {
        return moderationService.getAllModeratingLots();
    }

    @PreAuthorize("hasAnyAuthority('moder:write')")
    @PostMapping(value = "/moderation", params = "publish")
    public Boolean publishLot(@RequestBody Lot lot) {
        return moderationService.publishLot(lot, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PreAuthorize("hasAnyAuthority('moder:write')")
    @PostMapping(value = "/moderation", params = "reject")
    public Boolean rejectLot(@RequestBody Lot lot) {
        return moderationService.rejectLot(lot, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PreAuthorize("hasAnyAuthority('moder:read')")
    @GetMapping("/banned")
    public List<Lot> getBannedLots() {
        return moderationService.getBannedLots();
    }

    @PreAuthorize("hasAnyAuthority('moder:write')")
    @PutMapping(value = "/banned", params = "ban")
    public Boolean banLot(@RequestBody Lot lot) {
        return moderationService.banLot(lot);
    }

    @PreAuthorize("hasAnyAuthority('moder:write')")
    @PutMapping(value = "/banned", params = "unban")
    public Boolean unbanLot(@RequestBody Lot lot) {
        return moderationService.unbanLot(lot);
    }

    @PreAuthorize("hasAnyAuthority('moder:read')")
    @GetMapping(value = "/banned/search/{keyword}")
    public List<Lot> findBannedLots(@PathVariable String keyword) {
        return moderationService.findBannedLots(keyword);
    }

    @PreAuthorize("hasAnyAuthority('moder:read')")
    @GetMapping(value = "/moderation/history")
    public List<ModeratingLot> getMyModerationHistory() {
        return moderationService.getModeratorHistory(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PreAuthorize("hasAnyAuthority('moder:write')")
    @PutMapping("/moderation/history/update")
    public Boolean updateMyModeratedLot(@RequestBody ModeratingLot lot) {
        return moderationService.updateModeratedLot(lot, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PreAuthorize("hasAnyAuthority('moder:write')")
    @PutMapping( "/moderation/history/cancel")
    public Boolean cancelMyModerationDecision(@RequestBody ModeratingLot lot) {
        return moderationService.cancelModerationDecision(lot, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PreAuthorize("hasAnyAuthority('admin:read')")
    @GetMapping(value = "/moderation/history/all")
    public List<ModeratingLot> getAllModerationHistory() {
        return moderationService.getAllModerationHistory();
    }

    @PreAuthorize("hasAnyAuthority('admin:write')")
    @PutMapping("/moderation/history/all/update")
    public Boolean updateModeratedLot(@RequestBody ModeratingLot lot) {
        return moderationService.updateModeratedLot(lot, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PreAuthorize("hasAnyAuthority('admin:write')")
    @PutMapping( "/moderation/history/all/cancel")
    public Boolean cancelModerationDecision(@RequestBody ModeratingLot lot) {
        return moderationService.cancelModerationDecision(lot, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PreAuthorize("hasAnyAuthority('admin:read')")
    @GetMapping(value = "/moderation/history/{username}")
    public List<ModeratingLot> getModeratorHistory(@PathVariable String username) {
        return moderationService.getModeratorHistory(username);
    }

    @PreAuthorize("hasAnyAuthority('admin:write')")
    @PutMapping("/moderation/history/{username}/update")
    public Boolean updateModeratedLotByModerator(@RequestBody ModeratingLot lot, @PathVariable String username) {
        return moderationService.updateModeratedLot(lot, username);
    }

    @PreAuthorize("hasAnyAuthority('admin:write')")
    @PutMapping( "/moderation/history/{username}/cancel")
    public Boolean cancelModeratorDecision(@RequestBody ModeratingLot lot, @PathVariable String username) {
        return moderationService.cancelModerationDecision(lot, username);
    }
}
