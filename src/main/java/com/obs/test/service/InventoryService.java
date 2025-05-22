package com.obs.test.service;

import com.obs.test.dto.ResponseObject;
import com.obs.test.dto.request.InventoryRequestDTO;
import com.obs.test.dto.request.ItemRequestDTO;
import com.obs.test.dto.response.InventoryResponseDTO;
import com.obs.test.dto.response.ItemResponseDTO;
import com.obs.test.dto.response.PaginationResponse;
import com.obs.test.exception.EntityNotFoundException;

public interface InventoryService {

    ResponseObject<PaginationResponse<InventoryResponseDTO>> getAll(int page, int rows, String sortBy, String sortDirection, String search);
    ResponseObject<InventoryResponseDTO> saveAndUpdate(InventoryRequestDTO dto);
    ResponseObject<InventoryResponseDTO> getDetailById(Long id);
    ResponseObject delete(Long id);
}
