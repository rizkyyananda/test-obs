package com.obs.test.service.impl;

import com.obs.test.dto.ResponseObject;
import com.obs.test.dto.request.InventoryRequestDTO;
import com.obs.test.dto.response.InventoryResponseDTO;
import com.obs.test.dto.response.ItemResponseDTO;
import com.obs.test.dto.response.PaginationResponse;
import com.obs.test.entity.Inventory;
import com.obs.test.entity.Item;
import com.obs.test.exception.BadRequestException;
import com.obs.test.repository.InventoryRepository;
import com.obs.test.repository.ItemRepository;
import com.obs.test.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.rmi.NotBoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventorySeviceImpl implements InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public ResponseObject<PaginationResponse<InventoryResponseDTO>> getAll(int page, int rows, String sortBy, String sortDirection, String search) {
        page = Math.max(page, 1);

        // Sorting
        Sort sort = sortDirection.equalsIgnoreCase("DESC") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page - 1, rows, sort);

        // Ambil data
        Page<Inventory> itemPage = inventoryRepository.findAll(pageable);

        List<InventoryResponseDTO> itemDTOs = mappingToResponseList(itemPage.getContent());

        PaginationResponse<InventoryResponseDTO> pagination = new PaginationResponse<InventoryResponseDTO>(
                itemPage.getNumber() + 1,
                itemPage.getSize(),
                itemPage.getTotalElements(),
                itemPage.getTotalPages(),
                itemDTOs                             // content
        );

        return new ResponseObject<>(HttpStatus.OK, "Success", "get data success", pagination);
    }

    @Override
    public ResponseObject<InventoryResponseDTO> saveAndUpdate(InventoryRequestDTO dto) {
        try {
            if (dto.getQty()<= 0){
                return new ResponseObject(HttpStatus.BAD_REQUEST, "Failed", "failed save or update", "name can't be empty");
            }

            if (dto.getItemId() <=0){
                return new ResponseObject(HttpStatus.BAD_REQUEST, "Failed", "failed save or update", "price can't be empty");
            }
            Inventory inventory = this.mappingDTOItemToDomain(dto);
            inventory = inventoryRepository.save(inventory);
            InventoryResponseDTO inventoryResponseDTO = this.mappingToResponse(inventory);
            return new ResponseObject(HttpStatus.OK, "Success", "save or update data success", inventoryResponseDTO);
        }catch (Exception e){
            return new ResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "Failed", e.getMessage());
        }
    }

    @Override
    public ResponseObject<InventoryResponseDTO> getDetailById(Long id) {
        try {
            Optional<Inventory> inventory = inventoryRepository.findById(id);
            if (inventory.isPresent()){
                InventoryResponseDTO inventoryResponseDTO = mappingToResponse(inventory.get());
                return new ResponseObject(HttpStatus.OK, "Success", "get data success", inventoryResponseDTO);
            }else {
                return new ResponseObject(HttpStatus.NOT_FOUND, "Failed", "data not found");
            }
        }catch (Exception e){
            return new ResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "Failed", e.getMessage());
        }
    }

    @Override
    public ResponseObject delete(Long id) {
        try {
            Optional<Inventory> inventory = inventoryRepository.findById(id);
            if (inventory.isPresent()){
                inventoryRepository.delete(inventory.get());
                return new ResponseObject(HttpStatus.OK, "Success", "delete success");
            }else{
                return new ResponseObject(HttpStatus.NOT_FOUND, "Failed", "data not found");
            }
        }catch (Exception e){
            return new ResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "Failed", e.getMessage());
        }
    }


    private List<InventoryResponseDTO>mappingToResponseList(List<Inventory> inventories){
        List<InventoryResponseDTO> inItemResponseDTOList = inventories
                .stream()
                .map(inventorie -> {
                    InventoryResponseDTO dto = new InventoryResponseDTO();
                    dto.setId(inventorie.getId());
                    dto.setItemId(inventorie.getItem().getId());
                    dto.setQty(inventorie.getQty());
                    dto.setType(inventorie.getType());
                    return dto;
                })
                .collect(Collectors.toList());

        return inItemResponseDTOList;

    }

    public Inventory mappingDTOItemToDomain(InventoryRequestDTO dto) throws NotBoundException {
        Optional<Item> item = itemRepository.findById(dto.getItemId());
        if (item.isPresent()){
            Item itemData = item.get();
            Inventory inventory = new Inventory();
            if (dto.getId() != null) inventory.setId(dto.getId());
            inventory.setItem(itemData);
            inventory.setQty(dto.getQty());
            inventory.setType(dto.getType());
            return inventory;
        }else{
            throw new NotBoundException("data not found");
        }
    }

    public InventoryResponseDTO mappingToResponse(Inventory inventory){
        InventoryResponseDTO dto = new InventoryResponseDTO();
        dto.setId(inventory.getId());
        dto.setQty(inventory.getQty());
        dto.setQty(inventory.getQty());
        dto.setItemId(inventory.getItem().getId());
        dto.setType(inventory.getType());
        return dto;
    }
}
