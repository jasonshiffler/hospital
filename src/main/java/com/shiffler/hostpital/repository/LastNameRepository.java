package com.shiffler.hostpital.repository;

import com.shiffler.hostpital.entity.LastName;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface LastNameRepository extends CrudRepository<LastName, UUID> {
}
