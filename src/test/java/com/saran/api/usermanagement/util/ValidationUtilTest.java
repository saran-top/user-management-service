package com.saran.api.usermanagement.util;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ValidationUtilTest
{
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private Errors mockErrors;

    @Mock
    private ObjectError mockObjectError;

    @Test(expected = IllegalStateException.class)
    public void shouldNotAllowToCreateInstantiate()
    {
        new ValidationUtil();
    }

    @Test
    public void shouldConvertBindingErrors()
    {
        when(mockErrors.getAllErrors()).thenReturn(Collections.singletonList(mockObjectError));
        when(mockObjectError.getDefaultMessage()).thenReturn("First name can not be null");
        assertThat(ValidationUtil.convert(mockErrors))
                .hasSize(1)
                .contains("First name can not be null");
    }

}