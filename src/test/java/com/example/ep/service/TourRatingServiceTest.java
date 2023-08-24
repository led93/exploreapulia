package com.example.ep.service;

import com.example.ep.domain.Tour;
import com.example.ep.domain.TourRating;
import com.example.ep.repository.TourRatingRepository;
import com.example.ep.repository.TourRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class TourRatingServiceTest {

    private static final int CUSTOMER_ID = 123;
    private static final int TOUR_ID = 1;
    private static final int TOUR_RATING_ID = 100;

    @Mock
    private TourRepository tourRepositoryMock;
    @Mock
    private TourRatingRepository tourRatingRepositoryMock;
    @InjectMocks
    private TourRatingService service;

    @Mock
    private Tour tourMock;
    @Mock
    private TourRating tourRatingMock;

    /**
     * Mock responses to commonly invoked methods.
     */
    @BeforeEach
    void setupReturnValuesOfMockMethods() {
        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(tourMock));
        when(tourMock.getId()).thenReturn(TOUR_ID);
        when(tourRatingRepositoryMock.findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID)).thenReturn(Optional.of(tourRatingMock));
    }

    /**************************************************************************************
     *
     * Verify the service return value
     *
     **************************************************************************************/

    @Test
    void lookupRatingById() {
        when(tourRatingRepositoryMock.findById(TOUR_RATING_ID)).thenReturn(Optional.of(tourRatingMock));

        Optional<TourRating> tourRating = service.lookupRatingById(TOUR_RATING_ID);
        assertThat(tourRating).contains(tourRatingMock);
    }

    @Test
    void lookupAll() {
        when(tourRatingRepositoryMock.findAll()).thenReturn(List.of(tourRatingMock));
        assertThat(service.lookupAll().get(0)).isEqualTo(tourRatingMock);
    }

    @Test
    void getAverageScore() {
        when(tourRatingRepositoryMock.findByTourId(TOUR_ID)).thenReturn(List.of(tourRatingMock));
        when(tourRatingMock.getScore()).thenReturn(10);
        assertThat(service.getAverageScore(TOUR_ID)).isEqualTo(10.0);
    }

    @Test
    void lookupRatings() {
        //create mocks of Pageable and Page (only needed in this test)
        Pageable pageable = mock(Pageable.class);
        Page<TourRating> page = mock(Page.class);
        when(tourRatingRepositoryMock.findByTourId(1, pageable)).thenReturn(page);

        assertThat(service.lookupRatings(TOUR_ID, pageable)).isEqualTo(page);
    }

    /**************************************************************************************
     *
     * Verify the invocation of dependencies.
     *
     **************************************************************************************/

    @Test
    void delete() {
        service.delete(1, CUSTOMER_ID);

        //verify tourRatingRepository.delete invoked
        verify(tourRatingRepositoryMock).delete(any(TourRating.class));
    }

    @Test
    void rateMany() {
        service.rateMany(TOUR_ID, 10, new Integer[]{CUSTOMER_ID, CUSTOMER_ID + 1});

        //verify tourRatingRepository.save invoked twice
        verify(tourRatingRepositoryMock, times(2)).save(any(TourRating.class));
    }

    @Test
    void update() {
        service.update(TOUR_ID, CUSTOMER_ID, 5, "great");

        //verify tourRatingRepository.save invoked once
        verify(tourRatingRepositoryMock).save(any(TourRating.class));

        //verify and tourRating setter methods invoked
        verify(tourRatingMock).setComment("great");
        verify(tourRatingMock).setScore(5);
    }

    @Test
    void updateSome() {
        service.updateSome(TOUR_ID, CUSTOMER_ID, 1, "awful");

        //verify tourRatingRepository.save invoked once
        verify(tourRatingRepositoryMock).save(any(TourRating.class));

        //verify and tourRating setter methods invoked
        verify(tourRatingMock).setComment("awful");
        verify(tourRatingMock).setScore(1);
    }

    /**************************************************************************************
     *
     * Verify the invocation of dependencies
     * Capture parameter values.
     * Verify the parameters.
     *
     **************************************************************************************/

    @Test
    void createNew() {
        //prepare to capture a TourRating Object
        ArgumentCaptor<TourRating> tourRatingCaptor = ArgumentCaptor.forClass(TourRating.class);

        //invoke createNew
        service.createNew(TOUR_ID, CUSTOMER_ID, 2, "ok");

        //verify tourRatingRepository.save invoked once and capture the TourRating Object
        verify(tourRatingRepositoryMock).save(tourRatingCaptor.capture());

        //verify the attributes of the Tour Rating Object
        assertThat(tourRatingCaptor.getValue().getTour()).isEqualTo(tourMock);
        assertThat(tourRatingCaptor.getValue().getCustomerId()).isEqualTo(CUSTOMER_ID);
        assertThat(tourRatingCaptor.getValue().getScore()).isEqualTo(2);
        assertThat(tourRatingCaptor.getValue().getComment()).isEqualTo("ok");

    }
}
