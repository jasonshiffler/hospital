package com.shiffler.hospital.repository;

import com.shiffler.hospital.entity.MedicalTest;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MedicalTestRepository  extends CrudRepository<MedicalTest, UUID> {
}
