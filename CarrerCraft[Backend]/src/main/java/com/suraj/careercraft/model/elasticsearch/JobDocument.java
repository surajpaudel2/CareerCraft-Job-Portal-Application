package com.suraj.careercraft.model.elasticsearch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(indexName = "jobs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Field(type = FieldType.Keyword)
    private List<String> requirements; // Exact match for individual requirements

    @Field(type = FieldType.Double)
    private Double salary; // Numeric field for range queries

    @Field(type = FieldType.Text)
    private String status; // Enum-like field (ACTIVE, PENDING, CLOSED) for exact match

    @Field(type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime postedAt; // Date field for sorting or range queries

    @Field(type = FieldType.Text, analyzer = "standard")
    private String companyName; // Employer's company name

    @Field(type = FieldType.Text, analyzer = "standard")
    private String industry; // Employer's industry

    @Field(type = FieldType.Text)
    private String logoUrl; // Employer's logo URL
}
