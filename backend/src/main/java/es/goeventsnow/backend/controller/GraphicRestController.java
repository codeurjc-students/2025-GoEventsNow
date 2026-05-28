package es.goeventsnow.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.goeventsnow.backend.service.TicketService;

@RestController
@RequestMapping("/api/v1/graphics")
public class GraphicRestController {

    private static final List<String> PALETTE = List.of(
            "#4E79A7",
            "#F28E2B",
            "#E15759",
            "#76B7B2",
            "#59A14F",
            "#EDC948",
            "#B07AA1",
            "#FF9DA7",
            "#9C755F",
            "#BAB0AC");

    @Autowired
    private TicketService ticketService;

    @GetMapping("/bargraph")
    public Map<String, Object> getTicketsSoldByEvent() {
        List<Object[]> rows = ticketService.getTicketsSoldByEvent();

        List<String> labels = new ArrayList<>();
        List<Integer> data = new ArrayList<>();

        for (Object[] row : rows) {
            labels.add(String.valueOf(row[0]));
            data.add(((Number) row[1]).intValue());
        }

        return Map.of(
                "labels", labels,
                "data", data,
                "backgroundColor", buildColors(labels.size()));
    }

    @GetMapping("/piechart")
    public Map<String, Object> getTicketsSoldByCategory() {
        List<Object[]> rows = ticketService.getTicketsSoldByCategory();

        List<String> labels = new ArrayList<>();
        List<Integer> data = new ArrayList<>();

        for (Object[] row : rows) {
            labels.add(String.valueOf(row[0]));
            data.add(((Number) row[1]).intValue());
        }

        return Map.of(
                "labels", labels,
                "data", data,
                "backgroundColor", buildColors(labels.size()));
    }

    private List<String> buildColors(int size) {
        List<String> colors = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            colors.add(PALETTE.get(i % PALETTE.size()));
        }
        return colors;
    }
}