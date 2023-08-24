package com.example.ep.service;

import com.example.ep.domain.Tour;
import com.example.ep.domain.TourRating;
import com.example.ep.repository.TourRatingRepository;
import com.example.ep.repository.TourRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class TourRatingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TourRatingService.class);
    private final TourRepository tourRepository;
    private final TourRatingRepository tourRatingRepository;

    public TourRatingService(TourRatingRepository tourRatingRepository, TourRepository tourRepository) {
        this.tourRepository = tourRepository;
        this.tourRatingRepository = tourRatingRepository;
    }

    public void createNew(int tourId, Integer customerId, Integer score, String comment) throws NoSuchElementException {
        LOGGER.info("Create Rating for tour {} of customers {}", tourId, customerId);
        tourRatingRepository.save(new TourRating(verifyTour(tourId), customerId,
                score, comment));
    }

    public Optional<TourRating> lookupRatingById(int id) {
        return tourRatingRepository.findById(id);
    }

    public List<TourRating> lookupAll() {
        LOGGER.info("Lookup all Ratings");
        return StreamSupport.stream(tourRatingRepository.findAll().spliterator(), false).toList();
    }

    public Page<TourRating> lookupRatings(int tourId, Pageable pageable) throws NoSuchElementException {
        LOGGER.info("Lookup Rating for tour {}", tourId);
        return tourRatingRepository.findByTourId(verifyTour(tourId).getId(), pageable);
    }

    public TourRating update(int tourId, Integer customerId, Integer score, String comment)
            throws NoSuchElementException {
        LOGGER.info("Update all of Rating for tour {} of customers {}", tourId, customerId);
        TourRating rating = verifyTourRating(tourId, customerId);
        rating.setScore(score);
        rating.setComment(comment);
        return tourRatingRepository.save(rating);
    }

    public TourRating updateSome(int tourId, Integer customerId, Integer score, String comment)
            throws NoSuchElementException {
        LOGGER.info("Update some of Rating for tour {} of customers {}", tourId, customerId);
        TourRating rating = verifyTourRating(tourId, customerId);
        if (score != null) {
            rating.setScore(score);
        }
        if (comment != null) {
            rating.setComment(comment);
        }
        return tourRatingRepository.save(rating);
    }

    public void delete(int tourId, Integer customerId) throws NoSuchElementException {
        LOGGER.info("Delete Rating for tour {} and customer {}", tourId, customerId);
        TourRating rating = verifyTourRating(tourId, customerId);
        tourRatingRepository.delete(rating);
    }

    public Double getAverageScore(int tourId) throws NoSuchElementException {
        LOGGER.info("Get average score of tour {}", tourId);
        List<TourRating> ratings = tourRatingRepository.findByTourId(verifyTour(tourId).getId());
        OptionalDouble average = ratings.stream().mapToInt(TourRating::getScore).average();
        return average.isPresent() ? average.getAsDouble() : null;
    }

    public void rateMany(int tourId, int score, Integer[] customers) {
        LOGGER.info("Rate tour {} by customers {}", tourId, Arrays.asList(customers));
        tourRepository.findById(tourId).ifPresent(tour -> {
            for (Integer customerId : customers) {
                LOGGER.debug("Attempt to create Tour Rating for customer {}", customerId);
                tourRatingRepository.save(new TourRating(tour, customerId, score));
            }
        });
    }

    private Tour verifyTour(int tourId) throws NoSuchElementException {
        return tourRepository.findById(tourId).orElseThrow(() ->
                new NoSuchElementException("Tour does not exist " + tourId)
        );
    }

    TourRating verifyTourRating(int tourId, int customerId) throws NoSuchElementException {
        return tourRatingRepository.findByTourIdAndCustomerId(tourId, customerId).orElseThrow(() ->
                new NoSuchElementException("Tour-Rating pair for request("
                        + tourId + " for customer" + customerId));
    }
}
