package com.knight.hairreservation.repository;

import com.knight.hairreservation.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}