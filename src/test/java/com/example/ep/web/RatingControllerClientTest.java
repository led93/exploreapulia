package com.example.ep.web;

import com.example.ep.domain.Tour;
import com.example.ep.domain.TourRating;
import com.example.ep.service.TourRatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureTestDatabase
class RatingControllerClientTest {
    private static final String RATINGS_URL = "/ratings";

    //These Tour and rating ids do not already exist in the db
    private static final int TOUR_ID = 999;
    private static final int RATING_ID = 555;
    private static final int CUSTOMER_ID = 1000;
    private static final int SCORE = 3;
    private static final String COMMENT = "comment";

    @MockBean
    private TourRatingService tourRatingServiceMock;

    @Mock
    private TourRating tourRatingMock;

    @Mock
    private Tour tourMock;

    @Autowired
    private TestRestTemplate restTemplate;


    @BeforeEach
    void setupReturnValuesOfMockMethods() {
        when(tourRatingMock.getTour()).thenReturn(tourMock);
        when(tourMock.getId()).thenReturn(TOUR_ID);
        when(tourRatingMock.getComment()).thenReturn(COMMENT);
        when(tourRatingMock.getScore()).thenReturn(SCORE);
        when(tourRatingMock.getCustomerId()).thenReturn(CUSTOMER_ID);
    }

    /**
     * HTTP GET /ratings
     */
    @Test
    void getRatings() {
        when(tourRatingServiceMock.lookupAll()).thenReturn(Arrays.asList(tourRatingMock, tourRatingMock, tourRatingMock));

        ResponseEntity<CollectionModel<EntityModel<TourRating>>> response = restTemplate.exchange(
                RATINGS_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody())).hasSize(3);
    }

    /**
     * HTTP GET /ratings/{id}
     */
    @Test
    void getOne() {

        when(tourRatingServiceMock.lookupRatingById(RATING_ID)).thenReturn(Optional.of(tourRatingMock));

        ResponseEntity<RatingDto> response =
                restTemplate.getForEntity(RATINGS_URL + "/" + RATING_ID, RatingDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getCustomerId()).isEqualTo(CUSTOMER_ID);
        assertThat(Objects.requireNonNull(response.getBody()).getComment()).isEqualTo(COMMENT);
        assertThat(Objects.requireNonNull(response.getBody()).getScore()).isEqualTo(SCORE);
    }
}
