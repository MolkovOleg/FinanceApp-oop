package com.olmolkov.oopfm.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Category {
    private String username;
    private String name;
    private double budget;

    @JsonCreator
    public Category(
            @JsonProperty("username") String username,
            @JsonProperty("name") String name,
            @JsonProperty("budget") double budget) {
        this.username = username;
        this.name = name;
        this.budget = budget;
    }
}
