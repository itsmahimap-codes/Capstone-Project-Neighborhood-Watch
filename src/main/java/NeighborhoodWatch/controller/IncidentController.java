package NeighborhoodWatch.controller;

import java.io.IOException;
import java.util.List;

import NeighborhoodWatch.entity.Incident;
import NeighborhoodWatch.service.IncidentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/incidents")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    @PostMapping("/report")
    public Incident reportIncident(
            @RequestBody Incident incident) {

        return incidentService.createIncident(incident);
    }

    @GetMapping
    public List<Incident> getAllIncidents() {

        return incidentService.getAllIncidents();
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<?> uploadImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile image) {

        try {

            Incident incident =
                    incidentService.addImage(id, image);

            if (incident == null) {

                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(
                    "Image uploaded successfully"
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());

        } catch (IOException e) {

            return ResponseEntity.internalServerError()
                    .body("Failed to upload image");
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(
            @PathVariable Long id) {

        Incident incident =
                incidentService.getIncidentById(id);

        if (incident == null ||
                incident.getImageData() == null) {

            return ResponseEntity.notFound().build();
        }

        MediaType mediaType =
                MediaType.parseMediaType(
                        incident.getImageType()
                );

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(incident.getImageData());
    }

    @PutMapping("/{id}")
    public Incident updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return incidentService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public String deleteIncident(
            @PathVariable Long id) {

        incidentService.deleteIncident(id);

        return "Incident deleted successfully";
    }
}