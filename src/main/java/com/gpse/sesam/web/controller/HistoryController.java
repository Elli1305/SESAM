package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.door.history.History;
import com.gpse.sesam.domain.location.door.history.HistoryService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Secured({"ADMINSTRATOR", "EDITOR"})
    @GetMapping("/histories")
    public java.util.List<History> getHistories() {
        return historyService.getHistories();
    }

    @Secured({"ADMINSTRATOR", "EDITOR"})
    @GetMapping("/history/{id}")
    public Optional<History> getHistoryById(@PathVariable("id") final Long id) {
        return historyService.getHistory(id);
    }

    @Secured({"ADMINSTRATOR", "EDITOR"})
    @GetMapping("/historiesByDoor")
    public java.util.List<History> getHistoriesByDoor(@PathVariable("id") final Long id) {
        return historyService.historiesByDoor(id);
    }
}
