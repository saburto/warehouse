package com.saburto.warehouse.applications;

public class NoProductFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    public NoProductFoundException(String name) {
        super(String.format("Product [%s] not found", name));
    }


}
