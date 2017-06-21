package com.saran.api.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
public class ErrorResponseDTO implements Serializable
{

    private static final long serialVersionUID = -1859521571777133637L;

    private final String errorCode;
    private final String errorMessage;
    private List<String> errors;

}
