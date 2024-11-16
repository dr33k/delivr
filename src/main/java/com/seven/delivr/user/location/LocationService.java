package com.seven.delivr.user.location;

import com.seven.delivr.base.UpsertService;
import com.seven.delivr.requests.AppPageRequest;
import com.seven.delivr.user.User;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
@Service
public class LocationService implements UpsertService<LocationUpsertRequest, UUID> {
    private final LocationRepository locationRepository;
    private final User principal;
    private final EntityManager em;

    public LocationService(LocationRepository locationRepository,
                           User principal,
                           EntityManager entityManager) {
        this.locationRepository = locationRepository;
        this.principal = principal;
        this.em = entityManager;
    }

    public List<LocationRecord> getAll(AppPageRequest request) {
        return locationRepository.findAllByUserId(principal.getId()).stream().map(LocationRecord::map).toList();
    }

    @Override
    public Location get(UUID id) {
        return locationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Location upsert(LocationUpsertRequest request) {
        Location location;
        if (request.id == null) {
            location = Location.creationMap(request);
            location.setUser(em.getReference(User.class, principal.getId()));
        } else {
            location = locationRepository.findById(request.id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            Location.updateMap(request, location);
        }
        locationRepository.save(location);
        return location;
    }

    @Override
    public void delete(UUID id) {
        locationRepository.deleteById(id);
    }
}
