package com.hacnation.common.exception;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String resource, String id) {
        super(resource + " non trouve avec l'ID: " + id, 404);
    }
}
