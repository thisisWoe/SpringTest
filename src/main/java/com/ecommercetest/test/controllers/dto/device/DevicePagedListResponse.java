package com.ecommercetest.test.controllers.dto.device;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class DevicePagedListResponse {
    private final List<DeviceAssignedResponse> content;

    private final int pageSize;

    private final int page;

    private final int totalPages;

    private final Long totalItems;

    private final boolean isLastPage;


    public DevicePagedListResponse(Page<DeviceAssignedResponse> response) {
        this.content = response.getContent();
        this.pageSize = response.getSize();
        this.page = response.getNumber();
        this.totalPages = response.getTotalPages();
        this.totalItems = response.getTotalElements();
        this.isLastPage = response.isLast();
    }
}
