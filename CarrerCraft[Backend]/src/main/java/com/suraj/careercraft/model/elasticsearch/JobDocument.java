package com.suraj.careercraft.model.elasticsearch;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.suraj.careercraft.model.enums.JobType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Document(indexName = "jobs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobDocument {

    @Id
    private String id; // Primary Key

    @Field(type = FieldType.Keyword)
    private String employerId;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title; // Full-text search field

    @JsonIgnore
    @Field(type = FieldType.Keyword)
    private String titleKeyword; // Exact match for filtering, sorting, etc.

    @Field(type = FieldType.Text, analyzer = "standard")
    private String description; // Full-text search

    @Field(type = FieldType.Text, analyzer = "standard")
    private String location; // Full-text search

    @JsonIgnore
    @Field(type = FieldType.Keyword)
    private String locationKeyword; // Exact match for filtering or aggregations

    @Field(type = FieldType.Text)
    private List<String> requirements; // Exact match for individual requirements

    @JsonIgnore
    @Field(type = FieldType.Keyword)
    private List<String> requirementsKeyword;

    @Field(type = FieldType.Double)
    private Double salary; // Numeric field for range queries

    @Field(type = FieldType.Text)
    private String status; // Enum-like field (ACTIVE, PENDING, CLOSED) for exact match

    @Field(type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime postedAt;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String companyName; // Employer's company name

    @Field(type = FieldType.Text, analyzer = "standard")
    private String industry; // Employer's industry

    @Field(type = FieldType.Text)
    private String logoUrl; // Employer's logo URL

    @Field(type = FieldType.Keyword)
    private JobType jobType; // Added JobType enum for job type (e.g., FULL_TIME, PART_TIME, etc.)
}
