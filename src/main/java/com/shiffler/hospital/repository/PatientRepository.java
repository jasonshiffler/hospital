package com.shiffler.hospital.repository;

import com.shiffler.hospital.entity.Patient;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PatientRepository extends CrudRepository<Patient, UUID> {


}
