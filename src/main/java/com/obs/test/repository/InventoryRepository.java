package com.obs.test.repository;

import com.obs.test.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface InventoryRepository extends PagingAndSortingRepository<Inventory, Long>, JpaRepository<Inventory, Long> {
    Optional<Inventory> findInventoryByItemId(Long itemId);
}
