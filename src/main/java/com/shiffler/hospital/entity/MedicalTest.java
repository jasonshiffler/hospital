package com.shiffler.hospital.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MedicalTest {

    @Id
    @GeneratedValue//(generator = "UUID")
    @Column(name="id", columnDefinition = "uuid")// updatable = false,nullable = false)
    private UUID id;

    @CreationTimestamp
    @Column(name="created_date_time")//, //updatable = false)
    private OffsetDateTime createdDateTime;

    @UpdateTimestamp
    @Column(name="last_modified_date_time")
    private OffsetDateTime lastModifiedDateTime;

    @Size(min=3, max =32)
    @Column(name="test_name")
    private String testName;

    @Length(min = 10, max = 10, message ="The field must be 10 characters long")
    @Column(name="test_code")
    private String testCode;

    //Did the test come back positive. A null value means the result hasn't
    //been received
    @Column(name="is_positive")
    private Boolean isPositive;

    //Has the test order been placed with the testing facility.
    @Column(name="is_ordered")
    private Boolean isOrdered;


    @Column(name="test_status" )
    private MedicalTestStatusEnum testStatus;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Patient patient;


}
