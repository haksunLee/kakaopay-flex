package com.kakaopay.flex.domain.repository;

import com.kakaopay.flex.domain.entity.FlexItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FlexItemRepository extends JpaRepository<FlexItem, Long> {
    @Query("SELECT p FROM FlexItem p ORDER BY created_date DESC")
    List<FlexItem> findAllDesc();

}
