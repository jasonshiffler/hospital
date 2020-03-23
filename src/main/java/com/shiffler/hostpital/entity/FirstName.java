package com.shiffler.hostpital.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="first_names")
public class FirstName {

    @Id
    @GeneratedValue
    @Column(name="id", columnDefinition = "uuid", updatable = false)
    private UUID id;

    @CreationTimestamp
    @Column(name="created_date_time", updatable = false)
    private OffsetDateTime createdDateTime;

    @UpdateTimestamp
    @Column(name="last_modified_date_time")
    private OffsetDateTime lastModifiedDateTime;

    @Pattern(regexp="^[a-zA-Z ]{2,32}$",
            message = "Firstname can only have between 2 and 32 letters, spaces are allowed")
    @Column(name="first_name")
    @NotNull
    private String firstName;

    @Column(name="male_name")
    @NotNull
    private boolean isMaleName;

}
