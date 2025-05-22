package com.obs.test.controller;

import com.obs.test.dto.ResponseObject;
import com.obs.test.dto.request.InventoryRequestDTO;
import com.obs.test.dto.response.InventoryResponseDTO;
import com.obs.test.dto.response.PaginationResponse;
import com.obs.test.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    private ResponseEntity<Object> newErrorResponse(HttpStatus httpStatus, String status, String errorMsg, String[] errorCode){
        return new ResponseEntity<>(new ResponseObject<Object>(httpStatus, status, errorMsg, errorCode), httpStatus);
    }

    @GetMapping("")
    public ResponseEntity<Object> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int rows,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "") String search) {

        ResponseObject<PaginationResponse<InventoryResponseDTO>> inventoryList = inventoryService.getAll(page, rows, sortBy, sortDirection, search);
        return new ResponseEntity(inventoryList, inventoryList.getStatusCode());
    }

    @PostMapping("/save")
    public ResponseEntity<Object>save(@RequestBody InventoryRequestDTO dto){
        ResponseObject<InventoryResponseDTO> inventoryRes = inventoryService.saveAndUpdate(dto);
        return new ResponseEntity(inventoryRes, inventoryRes.getStatusCode());
    }

    @PutMapping("/update")
    public ResponseEntity<Object>update(@RequestBody InventoryRequestDTO dto){
        ResponseObject<InventoryResponseDTO> inventoryRes = inventoryService.saveAndUpdate(dto);
        return new ResponseEntity(inventoryRes, inventoryRes.getStatusCode());
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable ("id") Long id) {
        ResponseObject<InventoryResponseDTO> inventoryRes = inventoryService.getDetailById(id);
        return new ResponseEntity(inventoryRes, inventoryRes.getStatusCode());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try {
            ResponseObject res = inventoryService.delete(id);
            return new ResponseEntity(res, res.getStatusCode());
        }catch (Exception e){
            return newErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED", e.getMessage(), null);
        }
    }
}
