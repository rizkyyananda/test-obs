package com.obs.test.repository;

import com.obs.test.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ItemRepository extends PagingAndSortingRepository<Item, Long>, JpaRepository<Item, Long> {
    Page<Item> findAll(Specification<Item> specification, Pageable pageable);
    Page<Item> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
