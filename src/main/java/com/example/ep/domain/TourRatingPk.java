package com.example.ep.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TourRatingPk implements Serializable {

    @ManyToOne
    private Tour tour;

    @Column(insertable = false, updatable = false, nullable = false)
    private Integer customerId;

    public TourRatingPk() {
    }

    public TourRatingPk(Tour tour, Integer customerId) {
        this.tour = tour;
        this.customerId = customerId;
    }

    public Tour getTour() {
        return tour;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TourRatingPk that = (TourRatingPk) o;

        if (!Objects.equals(tour, that.tour)) return false;
        return Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        int result = tour != null ? tour.hashCode() : 0;
        result = 31 * result + (customerId != null ? customerId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TourRatingPk{" +
                "tour=" + tour +
                ", customerId=" + customerId +
                '}';
    }
}
