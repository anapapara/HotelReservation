package com.reservation.repository;

import com.reservation.domain.Hotel;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    List<Hotel> findAll();

    @Override
    <S extends Hotel> List<S> saveAll(Iterable<S> entities);

    @Override
    <S extends Hotel> long count(Example<S> example);

    Optional<Hotel> findById(Integer id);

}
