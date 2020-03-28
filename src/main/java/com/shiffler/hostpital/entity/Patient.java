package com.shiffler.hostpital.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Patient {

    @Id
    @GeneratedValue
    @Column(name="id", columnDefinition = "uuid")
    private UUID id;

    @CreationTimestamp
    @Column(name="created_date_time", updatable = false)
    private OffsetDateTime createdDateTime;

    @UpdateTimestamp
    @Column(name="last_modified_date_time")
    private OffsetDateTime lastModifiedDateTime;

    @Pattern(regexp="^[a-zA-Z ]{2,32}$",
            message = "Firstname can only have between 2 and 32 letters, spaces are allowed")
    private String firstName;

    @Pattern(regexp="^[a-zA-Z ]{2,32}$",
            message = "Lastname can only have between 2 and 32 letters, spaces are allowed")
    private String lastName;

    @Pattern(regexp="^[a-zA-Z ]$", message = " The middle Initial must be a letter")
    private String middleInitial;

    @Past
    private LocalDate dateOfBirth;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "medical_test_id")
    private List<MedicalTest> medicalTests = new ArrayList<>();


}
