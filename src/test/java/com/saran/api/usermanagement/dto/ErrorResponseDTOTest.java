package com.saran.api.usermanagement.dto;

import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class ErrorResponseDTOTest
{

    @Test
    public void shouldBuildErrorResponseDTO()
    {
        ErrorResponseDTO dto = ErrorResponseDTO.builder()
                .errorCode("Test")
                .errorMessage("Test error message")
                .errors(Collections.singletonList("Errors"))
                .build();
        assertThat(dto.getErrorMessage()).isEqualTo("Test error message");
        assertThat(dto.getErrorCode()).isEqualTo("Test");
        assertThat(dto.getErrors().get(0)).isEqualTo("Errors");
    }
}