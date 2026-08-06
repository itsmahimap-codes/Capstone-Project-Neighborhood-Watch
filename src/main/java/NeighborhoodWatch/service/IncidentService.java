package NeighborhoodWatch.service;
import java.util.List;
import NeighborhoodWatch.entity.Incident;
import NeighborhoodWatch.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Incident updateStatus(Long id, String status) {
    Incident incident = incidentRepository.findById(id).orElse(null);

    if (incident != null) {
        incident.setStatus(status);
        return incidentRepository.save(incident);
    }

    return null;
}
public void deleteIncident(Long id){
    incidentRepository.deleteById(id);
}
}
