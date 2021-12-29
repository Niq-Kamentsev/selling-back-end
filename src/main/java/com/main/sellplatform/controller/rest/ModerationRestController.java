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
@RequestMapping("api/lots/moderation")
public class ModerationRestController {
//    private final ModerationService moderationService;
//
//    @Autowired
//    public ModerationRestController(ModerationService moderationService) {
//        this.moderationService = moderationService;
//    }
//
//    @PreAuthorize("hasAnyAuthority('moder:read')")
//    @GetMapping
//    public List<Lot> getAllModeratingLots() {
//        return moderationService.getAllModeratingLots();
//    }
//
//    @PreAuthorize("hasAnyAuthority('moder:write')")
//    @PostMapping("/publish/{lotId}")
//    public Boolean publishLot(@PathVariable Long lotId) {
//        return moderationService.publishLot(lotId, SecurityContextHolder.getContext().getAuthentication().getName());
//    }
//
//    @PreAuthorize("hasAnyAuthority('moder:write')")
//    @PostMapping("/reject/{lotId}")
//    public Boolean rejectLot(@RequestBody String cause, @PathVariable Long lotId) {
//        return moderationService.rejectLot(lotId, SecurityContextHolder.getContext().getAuthentication().getName(), cause);
//    }
//
//    @PreAuthorize("hasAnyAuthority('moder:read')")
//    @GetMapping("/banned")
//    public List<Lot> getBannedLots() {
//        return moderationService.getBannedLots();
//    }
//
//    @PreAuthorize("hasAnyAuthority('moder:write')")
//    @PutMapping("/banned/unban/{lotId}")
//    public Boolean unbanLot(@PathVariable Long lotId) {
//        return moderationService.unbanLot(lotId);
//    }
//
//    @PreAuthorize("hasAnyAuthority('moder:write')")
//    @PutMapping("/ban/{lotId}")
//    public Boolean banLot(@PathVariable Long lotId) {
//        return moderationService.banLot(lotId);
//    }
//
//    @PreAuthorize("hasAnyAuthority('moder:read')")
//    @GetMapping(value = "/banned/search/{keyword}")
//    public List<Lot> findBannedLots(@PathVariable String keyword) {
//        return moderationService.findBannedLots(keyword);
//    }
//
//    @PreAuthorize("hasAnyAuthority('moder:read')")
//    @GetMapping(value = "/history")
//    public List<ModeratingLot> getMyModerationHistory() {
//        return moderationService.getModeratorHistory(SecurityContextHolder.getContext().getAuthentication().getName());
//    }
//
//    @PreAuthorize("hasAnyAuthority('moder:write')")
//    @PutMapping("/history/update/{moderLotId}")
//    public Boolean updateMyModeratedLot(@PathVariable Long moderLotId) {
//        return moderationService.updateModeratedLot(moderLotId, SecurityContextHolder.getContext().getAuthentication().getName());
//    }
//
//    @PreAuthorize("hasAnyAuthority('moder:write')")
//    @PutMapping( "/history/cancel/{moderLotId}")
//    public Boolean cancelMyModerationDecision(@PathVariable Long moderLotId) {
//        return moderationService.cancelModerationDecision(moderLotId, SecurityContextHolder.getContext().getAuthentication().getName());
//    }
//
//    @PreAuthorize("hasAnyAuthority('admin:read')")
//    @GetMapping(value = "/history/all")
//    public List<ModeratingLot> getAllModerationHistory() {
//        return moderationService.getAllModerationHistory();
//    }
//
//    @PreAuthorize("hasAnyAuthority('admin:write')")
//    @PutMapping("/history/all/update/{moderLotId}")
//    public Boolean updateModeratedLot(@PathVariable Long moderLotId) {
//        return moderationService.updateModeratedLot(moderLotId, SecurityContextHolder.getContext().getAuthentication().getName());
//    }
//
//    @PreAuthorize("hasAnyAuthority('admin:write')")
//    @PutMapping( "/history/all/cancel/{moderLotId}")
//    public Boolean cancelModerationDecision(@PathVariable Long moderLotId) {
//        return moderationService.cancelModerationDecision(moderLotId, SecurityContextHolder.getContext().getAuthentication().getName());
//    }
//
//    @PreAuthorize("hasAnyAuthority('admin:read')")
//    @GetMapping(value = "/history/{username}")
//    public List<ModeratingLot> getModeratorHistory(@PathVariable String username) {
//        return moderationService.getModeratorHistory(username);
//    }
//
//    @PreAuthorize("hasAnyAuthority('admin:write')")
//    @PutMapping("/history/{username}/update/{moderLotId}")
//    public Boolean updateModeratedLotByModerator(@PathVariable Long moderLotId, @PathVariable String username) {
//        return moderationService.updateModeratedLot(moderLotId, username);
//    }
//
//    @PreAuthorize("hasAnyAuthority('admin:write')")
//    @PutMapping( "/history/{username}/cancel/{moderLotId}")
//    public Boolean cancelModeratorDecision(@PathVariable Long moderLotId, @PathVariable String username) {
//        return moderationService.cancelModerationDecision(moderLotId, username);
//    }
}
