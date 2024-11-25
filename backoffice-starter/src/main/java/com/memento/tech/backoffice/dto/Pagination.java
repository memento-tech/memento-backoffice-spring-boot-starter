package com.memento.tech.backoffice.dto;

public record Pagination(int pageSize, int pageNumber) {

    public Pagination {
        if (pageSize <= 0) pageSize = 20;
        if (pageNumber < 0) pageNumber = 0;
    }
}