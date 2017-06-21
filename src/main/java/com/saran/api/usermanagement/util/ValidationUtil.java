package com.saran.api.usermanagement.util;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.stream.Collectors;

public final class ValidationUtil
{
    public ValidationUtil()
    {
        throw new IllegalStateException("Utility classes cannot be instantiated");
    }

    public static List<String> convert(final Errors errors)
    {
        return errors.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
    }
}
