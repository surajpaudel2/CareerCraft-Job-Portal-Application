package com.suraj.careercraft.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        // Create and configure ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        // Register JavaTimeModule for date/time serialization
        mapper.registerModule(new JavaTimeModule());

        // Register the custom deserializer for LocalDateTime
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, new EpochMillisToLocalDateTimeDeserializer());
        mapper.registerModule(module);

        // Create RestClient
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200) // Adjust hostname and port if needed
        ).build();

        // Create RestClientTransport with custom ObjectMapper
        RestClientTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper(mapper) // Use the customized ObjectMapper
        );

        // Create and return ElasticsearchClient
        return new ElasticsearchClient(transport);
    }
}
