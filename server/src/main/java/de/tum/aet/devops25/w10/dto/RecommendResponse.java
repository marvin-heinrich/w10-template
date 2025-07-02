package de.tum.aet.devops25.w10.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RecommendResponse(
    @JsonProperty("recommendation") String recommendation
) {}
