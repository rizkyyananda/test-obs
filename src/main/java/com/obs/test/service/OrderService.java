package com.obs.test.service;

import com.obs.test.dto.ResponseObject;
import com.obs.test.dto.request.ItemRequestDTO;
import com.obs.test.dto.request.OrderRequestDTO;
import com.obs.test.dto.response.ItemResponseDTO;
import com.obs.test.dto.response.OrderResponseDTO;
import com.obs.test.dto.response.PaginationResponse;

public interface OrderService {
    ResponseObject<PaginationResponse<OrderResponseDTO>> getAll(int page, int rows, String sortBy, String sortDirection, String search);
    ResponseObject<OrderResponseDTO> saveAndUpdate(OrderRequestDTO item);

    ResponseObject<OrderResponseDTO> getDetailById(Long id);
    ResponseObject delete(Long id);
}
