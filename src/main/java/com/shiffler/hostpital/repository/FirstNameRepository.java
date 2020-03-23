package com.shiffler.hostpital.repository;

import com.shiffler.hostpital.entity.FirstName;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FirstNameRepository extends CrudRepository<FirstName, UUID> {




}
