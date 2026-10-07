package NeighborhoodWatch.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import NeighborhoodWatch.entity.Incident;
import NeighborhoodWatch.repository.IncidentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

@Service
public class IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    public Incident createIncident(Incident incident) {

        incident.setStatus("Pending");

        return incidentRepository.save(incident);
    }

    public List<Incident> getAllIncidents() {

        return incidentRepository.findAll();
    }

    public Incident getIncidentById(Long id) {

        return incidentRepository.findById(id).orElse(null);
    }

    public Incident addImage(Long id, MultipartFile image) throws IOException {

        Incident incident = incidentRepository.findById(id).orElse(null);

        if (incident == null) {
            return null;
        }

        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Please select an image");
        }

        if (image.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Image size must be less than 5 MB");
        }

        String contentType = image.getContentType();

        Set<String> allowedTypes = Set.of(
                "image/jpeg",
                "image/png",
                "image/gif",
                "image/webp"
        );

        if (contentType == null || !allowedTypes.contains(contentType)) {
            throw new IllegalArgumentException(
                    "Only JPG, PNG, GIF and WEBP images are allowed"
            );
        }

        incident.setImageData(image.getBytes());
        incident.setImageType(contentType);

        return incidentRepository.save(incident);
    }

    public Incident updateStatus(Long id, String status) {

        Incident incident = incidentRepository.findById(id).orElse(null);

        if (incident != null) {

            incident.setStatus(status);

            return incidentRepository.save(incident);
        }

        return null;
    }

    public void deleteIncident(Long id) {

        incidentRepository.deleteById(id);
    }
}
