package com.shiffler.hostpital.repository;

import com.shiffler.hostpital.entity.Patient;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PatientRepository extends CrudRepository<Patient, UUID> {


}
