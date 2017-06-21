package com.saran.api.usermanagement.exception;

public class ResourceNotFoundException extends RuntimeException
{

    private static final long serialVersionUID = 3126444514129510784L;

    public ResourceNotFoundException(final String message)
    {
        super(message);
    }

}
