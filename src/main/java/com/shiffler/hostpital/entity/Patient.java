package com.shiffler.hostpital.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;
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
    @Column(name="created_date_time")//, //updatable = false)
    //@Null
    private OffsetDateTime createdDateTime;

    @UpdateTimestamp
    //@Null
    @Column(name="last_modified_date_time")
    private OffsetDateTime lastModifiedDateTime;

    @Size(min = 1, max = 32)
    private String firstName;

    @Size(min = 1, max = 32)
    private String lastName;

    @Size(min= 1, max = 1)
    private String middleInitial;

    @Past
    private LocalDate birthday;


}
