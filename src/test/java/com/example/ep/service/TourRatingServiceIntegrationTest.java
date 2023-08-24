package com.example.ep.service;

import com.example.ep.domain.TourRating;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class TourRatingServiceIntegrationTest {
    private static final int CUSTOMER_ID = 456;
    private static final int TOUR_ID = 1;
    private static final int NOT_A_TOUR_ID = 123;

    @Autowired
    private TourRatingService service;

    //Happy Path delete existing TourRating.
    @Test
    void delete() {
        List<TourRating> tourRatings = service.lookupAll();
        service.delete(tourRatings.get(0).getTour().getId(), tourRatings.get(0).getCustomerId());
        assertThat(service.lookupAll()).hasSize(tourRatings.size() - 1);
    }

    //UnHappy Path, Tour NOT_A_TOUR_ID does not exist
    @Test
    void deleteException() {
        assertThrows(NoSuchElementException.class, () -> {
            service.delete(NOT_A_TOUR_ID, 1234);
        });
    }

    //Happy Path to Create a new Tour Rating
    @Test
    void createNew() {
        //would throw NoSuchElementException if TourRating for TOUR_ID by CUSTOMER_ID already exists
        service.createNew(TOUR_ID, CUSTOMER_ID, 2, "it was fair");

        //Verify New Tour Rating created.
        TourRating newTourRating = service.verifyTourRating(TOUR_ID, CUSTOMER_ID);
        assertThat(newTourRating.getTour().getId()).isEqualTo(TOUR_ID);
        assertThat(newTourRating.getCustomerId()).isEqualTo(CUSTOMER_ID);
        assertThat(newTourRating.getScore()).isEqualTo(2);
        assertThat(newTourRating.getComment()).isEqualTo("it was fair");
    }

    //UnHappy Path, Tour NOT_A_TOUR_ID does not exist
    @Test
    void createNewException() {
        assertThrows(NoSuchElementException.class, () -> {
            service.createNew(NOT_A_TOUR_ID, CUSTOMER_ID, 2, "it was fair");
        });
    }

    //Happy Path many customers Rate one tour
    @Test
    void rateMany() {
        int ratings = service.lookupAll().size();
        service.rateMany(TOUR_ID, 5, new Integer[]{100, 101, 102});
        assertThat(service.lookupAll()).hasSize(ratings + 3);
    }

    //Unhappy Path, 2nd Invocation would create duplicates in the database, DataIntegrityViolationException thrown
    @Test
    void rateManyProveRollback() {
        Integer[] customerIds = {100, 101, 102};
        service.rateMany(TOUR_ID, 3, customerIds);
        assertThrows(DataIntegrityViolationException.class, () -> {
            service.rateMany(TOUR_ID, 3, customerIds);
        });
    }

    //Happy Path, Update a Tour Rating already in the database
    @Test
    void update() {
        createNew();
        TourRating tourRating = service.update(TOUR_ID, CUSTOMER_ID, 1, "one");
        assertThat(tourRating.getTour().getId()).isEqualTo(TOUR_ID);
        assertThat(tourRating.getCustomerId()).isEqualTo(CUSTOMER_ID);
        assertThat(tourRating.getScore()).isEqualTo(1);
        assertThat(tourRating.getComment()).isEqualTo("one");


    }

    //Unhappy path, no Tour Rating exists for tourId=1 and customer=1
    @Test
    void updateException() {
        assertThrows(NoSuchElementException.class, () -> {
            service.update(1, 1, 1, "one");
        });
    }

    //Happy Path, Update a Tour Rating already in the database
    @Test
    void updateSome() {
        createNew();
        TourRating tourRating = service.updateSome(TOUR_ID, CUSTOMER_ID, 1, "one");
        assertThat(tourRating.getTour().getId()).isEqualTo(TOUR_ID);
        assertThat(tourRating.getCustomerId()).isEqualTo(CUSTOMER_ID);
        assertThat(tourRating.getScore()).isEqualTo(1);
        assertThat(tourRating.getComment()).isEqualTo("one");
    }

    //Unhappy path, no Tour Rating exists for tourId=1 and customer=1
    @Test
    void updateSomeException() {
        assertThrows(NoSuchElementException.class, () -> {
            service.updateSome(1, 1, 1, "one");
        });
    }

    //Happy Path get average score of a Tour.
    @Test
    void getAverageScore() {
        assertEquals(5.0, service.getAverageScore(TOUR_ID));
    }

    //UnHappy Path, Tour NOT_A_TOUR_ID does not exist
    @Test
    void getAverageScoreException() {
        assertThrows(NoSuchElementException.class, () -> {
            service.getAverageScore(NOT_A_TOUR_ID); //That tour does not exist
        });
    }
}
