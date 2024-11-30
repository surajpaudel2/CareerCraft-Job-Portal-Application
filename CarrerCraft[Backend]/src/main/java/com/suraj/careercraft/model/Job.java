package com.suraj.careercraft.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.suraj.careercraft.model.enums.JobStatus;
import com.suraj.careercraft.model.enums.JobType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private BigDecimal salary;

    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type")
    private JobType jobType; // Added JobType enum field

    @ElementCollection
    @CollectionTable(name = "job_requirements", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "requirement")
    private List<String> requirements;

    @CreationTimestamp
    @Column(name = "posted_at", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime postedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // Changed to LocalDateTime for consistency

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "employer_id", referencedColumnName = "id")
    private EmployerProfile employer;
}
