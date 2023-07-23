package com.example.ep.service;

import com.example.ep.domain.Difficulty;
import com.example.ep.domain.Region;
import com.example.ep.domain.Tour;
import com.example.ep.domain.TourPackage;
import com.example.ep.repository.TourPackageRepository;
import com.example.ep.repository.TourRepository;
import org.springframework.stereotype.Service;

@Service
public class TourService {
    private TourRepository tourRepository;
    private TourPackageRepository tourPackageRepository;

    public TourService(TourRepository tourRepository, TourPackageRepository tourPackageRepository) {
        this.tourRepository = tourRepository;
        this.tourPackageRepository = tourPackageRepository;
    }

    public Tour createTour(String title, String description, String blurp, Integer price, String duration,
                           String bullets, String keywords, String tourPackageName, Difficulty difficulty,
                           Region region) {
        TourPackage tourPackage = tourPackageRepository.findByName(tourPackageName)
                .orElseThrow(() -> new RuntimeException("Tour package does not exist " + tourPackageName));

        return tourRepository.save(new Tour(title, description, blurp, price,duration, bullets,keywords, tourPackage,
                difficulty, region));
    }

    public long total() {
        return tourRepository.count();
    }
}
