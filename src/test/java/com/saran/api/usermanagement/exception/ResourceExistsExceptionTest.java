package com.saran.api.usermanagement.exception;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceExistsExceptionTest
{

    @Test
    public void shouldReturnErrorMessage()
    {
        assertThat(new ResourceExistsException("Email already exists").getMessage()).isEqualTo("Email already exists");
    }
}