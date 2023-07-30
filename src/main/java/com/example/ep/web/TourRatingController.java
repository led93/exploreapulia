package com.example.ep.web;

import com.example.ep.domain.Tour;
import com.example.ep.domain.TourRating;
import com.example.ep.domain.TourRatingPk;
import com.example.ep.repository.TourRatingRepository;
import com.example.ep.repository.TourRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/tours/{tourId}/ratings")
public class TourRatingController {
    private TourRatingRepository tourRatingRepository;
    private TourRepository tourRepository;

    @Autowired
    public TourRatingController(TourRatingRepository tourRatingRepository, TourRepository tourRepository) {
        this.tourRatingRepository = tourRatingRepository;
        this.tourRepository = tourRepository;
    }

    protected TourRatingController() {
    }

    @GetMapping
    List<RatingDto> getAllRatingsForTour(@PathVariable(value = "tourId") int tourId) {
        verifyTour(tourId);
        return tourRatingRepository.findByPkTourId(tourId).stream()
                .map(RatingDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/average")
    public Map<String, Double> getAverage(@PathVariable(value = "tourId") int tourId) {
        verifyTour(tourId);
        return Map.of("average", tourRatingRepository.findByPkTourId(tourId).stream()
                .mapToInt(TourRating::getScore)
                .average()
                .orElseThrow(()-> new NoSuchElementException("Tour has no ratings")));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTourRating(@PathVariable(value = "tourId") int tourId,
                                 @Valid @RequestBody RatingDto ratingDto) {
        Tour tour = verifyTour(tourId);
        TourRatingPk tourRatingPk = new TourRatingPk(tour, ratingDto.getCustomerId());
        tourRatingRepository.save(new TourRating(tourRatingPk, ratingDto.getScore(), ratingDto.getComment()));
    }

    /**
     * Verify and return the Tour given a tourId
     *
     * @param tourId tour identifier
     * @return the found Tour
     * @throws NoSuchElementException if no Tour found.
     */
    private Tour verifyTour(int tourId) throws NoSuchElementException {
        return tourRepository.findById(tourId).orElseThrow(() ->
                new NoSuchElementException("Tour does not exist " + tourId));
    }

    /**
     * Exception handler if NoSuchElementException is thrown in this Controller
     *
     * @param ex exception
     * @return Error message String.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException ex) {
        return ex.getMessage();
    }
}
