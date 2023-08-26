package com.example.ep.web;

import com.example.ep.domain.TourRating;
import com.example.ep.service.TourRatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RatingController.class)
class RatingControllerServerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TourRatingService tourRatingServiceMock;

    @Test
    void shouldFetchAHalDocument() throws Exception {
        given(tourRatingServiceMock.lookupAll()).willReturn(
                List.of(
                        new TourRating(null, 1, 5, "comment 1"),
                        new TourRating(null, 2, 3, "comment 2")
                ));

        mvc.perform(get("/ratings").accept(MediaTypes.HAL_JSON_VALUE))
                .andDo(print())

                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.tourRatings[0].customerId", is(1)))
                .andExpect(jsonPath("$._embedded.tourRatings[0].score", is(5)))
                .andExpect(jsonPath("$._embedded.tourRatings[0].comment", is("comment 1"))).andExpect(jsonPath("$._embedded.tourRatings[0].customerId", is(1)))
                .andExpect(jsonPath("$._embedded.tourRatings[1].customerId", is(2)))
                .andExpect(jsonPath("$._embedded.tourRatings[1].score", is(3)))
                .andExpect(jsonPath("$._embedded.tourRatings[1].comment", is("comment 2")));
    }

}
