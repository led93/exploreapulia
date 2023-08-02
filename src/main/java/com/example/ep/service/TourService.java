package com.example.ep.service;

import com.example.ep.domain.Tour;
import com.example.ep.domain.TourPackage;
import com.example.ep.repository.TourPackageRepository;
import com.example.ep.repository.TourRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TourService {
    private final TourRepository tourRepository;
    private final TourPackageRepository tourPackageRepository;

    public TourService(TourRepository tourRepository, TourPackageRepository tourPackageRepository) {
        this.tourRepository = tourRepository;
        this.tourPackageRepository = tourPackageRepository;
    }

    public Tour createTour(String  title, String tourPackageName, Map<String,String> details) {
        TourPackage tourPackage = tourPackageRepository.findByName(tourPackageName)
                .orElseThrow(() -> new RuntimeException("Tour package does not exist: " + tourPackageName));
        return tourRepository.save(new Tour(title, tourPackage, details));
    }

    public long total() {
        return tourRepository.count();
    }
}
