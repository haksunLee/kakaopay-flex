package com.kakaopay.flex.domain.repository;

import com.kakaopay.flex.domain.entity.Flex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlexRepository extends JpaRepository<Flex, String> {
    @Query("SELECT p FROM Flex p ORDER BY created_date DESC")
    List<Flex> findAllDesc();

    Optional<Flex> findByToken(String token);

    Optional<Flex> findByTokenAndCreatedDateGreaterThan(String token, LocalDateTime createdDate);

    @Transactional
    @Modifying
    @Query("DELETE Flex")
    void deleteAllData();
}
