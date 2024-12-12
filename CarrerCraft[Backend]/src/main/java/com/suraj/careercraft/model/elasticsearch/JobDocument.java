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
    private String id;

    @Field(type = FieldType.Keyword)
    private String employerId;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @JsonIgnore
    @Field(type = FieldType.Keyword)
    private String titleKeyword;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String location;

    @JsonIgnore
    @Field(type = FieldType.Keyword)
    private String locationKeyword;

    @Field(type = FieldType.Text)
    private List<String> requirements;

    @JsonIgnore
    @Field(type = FieldType.Keyword)
    private List<String> requirementsKeyword;

    @Field(type = FieldType.Double)
    private Double salary;

    @Field(type = FieldType.Text)
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime postedAt;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String companyName;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String industry;

    @Field(type = FieldType.Text)
    private String logoUrl;

    @Field(type = FieldType.Keyword)
    private List<JobType> jobTypes; // Store as a list of strings in Elasticsearch
}
