package com.obs.test.service;

import com.obs.test.dto.ResponseObject;
import com.obs.test.dto.request.ItemRequestDTO;
import com.obs.test.dto.response.ItemResponseDTO;
import com.obs.test.dto.response.PaginationResponse;
import com.obs.test.entity.Item;
import com.obs.test.exception.EntityNotFoundException;

import java.util.List;

public interface ItemService {
    ResponseObject<PaginationResponse<ItemResponseDTO>> getAll(int page, int rows, String sortBy, String sortDirection, String search);
    ResponseObject<ItemResponseDTO> saveAndUpdate(ItemRequestDTO item);

    ResponseObject<ItemResponseDTO> getDetailById(Long id);
    ResponseObject delete(Long id);
}
