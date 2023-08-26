package com.example.ep.web;

import com.example.ep.domain.TourRating;
import com.example.ep.service.TourRatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping(path = "/ratings")
public class RatingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RatingController.class);
    private final TourRatingService tourRatingService;

    public RatingController(TourRatingService tourRatingService) {
        this.tourRatingService = tourRatingService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<TourRating>>> getAll() {
        LOGGER.info("GET /ratings");
        List<EntityModel<TourRating>> ratings = tourRatingService.lookupAll().stream()
                .map(tourRating -> EntityModel.of(tourRating,
                        linkTo(methodOn(RatingController.class).getRating(tourRating.getId())).withSelfRel(),
                        linkTo(methodOn(RatingController.class).getAll()).withRel("ratings")))
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(ratings, linkTo(methodOn(RatingController.class).getAll()).withSelfRel())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<TourRating>> getRating(@PathVariable Integer id) {
        LOGGER.info("GET /ratings/{}", id);
        return tourRatingService.lookupRatingById(id)
                .map(t -> EntityModel.of(t,
                        linkTo(methodOn(RatingController.class).getRating(t.getId())).withSelfRel(),
                        linkTo(methodOn(RatingController.class).getAll()).withRel("ratings")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

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
        LOGGER.error("Unable to complete transaction", ex);
        return ex.getMessage();
    }
}
