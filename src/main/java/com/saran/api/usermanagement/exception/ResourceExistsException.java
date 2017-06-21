package com.saran.api.usermanagement.exception;

public class ResourceExistsException extends RuntimeException
{

    private static final long serialVersionUID = -9138784941662172530L;

    public ResourceExistsException(final String message)
    {
        super(message);
    }

}
