package NeighborhoodWatch.controller;
import java.util.List;
import NeighborhoodWatch.entity.Incident;
import NeighborhoodWatch.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/incidents")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    @PostMapping("/report")
    public Incident reportIncident(@RequestBody Incident incident) {
        return incidentService.createIncident(incident);
    }

    @GetMapping
    public List<Incident> getAllIncidents() {
        return incidentService.getAllIncidents();
    }
    @PutMapping("/{id}")
public Incident updateStatus(@PathVariable Long id,
                             @RequestParam String status) {
    return incidentService.updateStatus(id, status);
}
@DeleteMapping("/{id}")
public String deleteIncident(@PathVariable Long id) {
    incidentService.deleteIncident(id);
    return "Incident deleted successfully";
}
}