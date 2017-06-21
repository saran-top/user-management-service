package com.saran.api.usermanagement.exception;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class ResourceNotFoundExceptionTest
{

    @Test
    public void shouldReturnErrorMessage()
    {
        assertThat(new ResourceNotFoundException("User not found").getMessage()).isEqualTo("User not found");
    }
}