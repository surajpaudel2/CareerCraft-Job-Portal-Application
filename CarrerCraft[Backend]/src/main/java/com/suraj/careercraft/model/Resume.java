package com.suraj.careercraft.model;

import lombok.*;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "resumes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship with JobSeekerProfile (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "job_seeker_id", referencedColumnName = "id")
    private JobSeekerProfile jobSeeker;

    @Column(name = "resume_url")
    private String resumeUrl;  // Store the path or URL to the resume file

    @Column(name = "uploaded_at")
    private Timestamp uploadedAt;
}
