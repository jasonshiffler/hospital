package com.shiffler.hospital.repository;

import com.shiffler.hospital.entity.MedicalTest;
import com.shiffler.hospital.entity.MedicalTestOrderStatusEnum;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface MedicalTestRepository  extends CrudRepository<MedicalTest, UUID> {

    //Find a Medical Test Order with a particular  status
    @Query("select m from MedicalTest m where m.testOrderStatusEnum = ?1")
    List<MedicalTest> findByMedicalTestOrderStatus(MedicalTestOrderStatusEnum medicalTestOrderStatusEnum);


    @Query("select m from MedicalTest m where m.testOrderStatusEnum = ?1 " +
            "or m.testOrderStatusEnum = ?2 or m.testOrderStatusEnum = ?3 ")
    List<MedicalTest> findByMedicalTestMultipleStatus(MedicalTestOrderStatusEnum medicalTestOrderStatusEnum1,
                                                      MedicalTestOrderStatusEnum medicalTestOrderStatusEnum2,
                                                      MedicalTestOrderStatusEnum medicalTestOrderStatusEnum3);

}
