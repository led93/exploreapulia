package com.example.ep.repository;

import com.example.ep.domain.Tour;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourRepository extends CrudRepository<Tour, Integer> {
    List<Tour> findByTourPackageCode(@Param("code") String code);
}
