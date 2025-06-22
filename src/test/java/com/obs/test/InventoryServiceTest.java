package com.obs.test;


import com.obs.test.dto.request.InventoryRequestDTO;
import com.obs.test.dto.response.PaginationResponse;
import com.obs.test.entity.Inventory;
import com.obs.test.entity.Item;
import com.obs.test.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.rmi.NotBoundException;
import java.util.List;
import java.util.Optional;

import com.obs.test.dto.response.InventoryResponseDTO;
import com.obs.test.repository.InventoryRepository;
import com.obs.test.service.impl.InventorySeviceImpl;
import com.obs.test.dto.ResponseObject;

import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {
    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    @Spy
    private InventorySeviceImpl inventoryService;

    @Mock
    private ItemRepository itemRepository;

    @Test
    void testGetAllInventorySuccess() {
        // Dummy data Inventory entity
        Inventory inv1 = new Inventory();
        inv1.setId(2L);
        inv1.setItem(new Item());
        inv1.setQty(9);
        inv1.setType("T");

        Inventory inv2 = new Inventory();
        inv2.setId(3L);
        inv2.setItem(new Item());
        inv2.setQty(10);
        inv2.setType("W");

        List<Inventory> inventoryList = List.of(inv1, inv2);
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending());
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, pageable, inventoryList.size());

        when(inventoryRepository.findAll(any(Pageable.class))).thenReturn(inventoryPage);

        // Jika mappingToResponseList(...) adalah internal method, pastikan berjalan normal
        ResponseObject<PaginationResponse<InventoryResponseDTO>> response = inventoryService.getAll(1, 2, "id", "ASC", "");

        assertEquals("Success", response.getStatus());
        assertEquals("get data success", response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(2, response.getContent().getContent().size());
        assertEquals(1, response.getContent().getTotalPages());
        assertEquals(2, response.getContent().getTotalItems());
    }

    @Test
    void testSaveAndUpdate_Success() throws NotBoundException {
        // Arrange
        InventoryRequestDTO inventoryRequestDTO = new InventoryRequestDTO(11L, 10, "T");
        InventoryResponseDTO responseDTO = new InventoryResponseDTO( 0L,11L, 10, "T");
        // Mock behavior
        Item item = new Item();
        item.setId(1L);
        item.setName("Pensil");
        item.setPrice(10000);
        Inventory inventory = new Inventory();
        inventory.setId(1L);
        inventory.setItem(item);
        inventory.setQty(inventoryRequestDTO.getQty());
        inventory.setType(inventoryRequestDTO.getType());

        when(itemRepository.findById(inventoryRequestDTO.getItemId())).thenReturn(Optional.of(item));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);
        // Act - call method
        ResponseObject<InventoryResponseDTO> response = inventoryService.saveAndUpdate(inventoryRequestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", "Success");
        assertEquals("save or update data success", response.getMessage());
        assertEquals(responseDTO.getQty(), response.getContent().getQty());
        assertNotNull(response.getContent());
        assertEquals(1L, response.getContent().getId());
    }

    @Test
    void testGetByIdInventorySuccess() {
        // Arrange
        Item item = new Item();
        item.setId(11L);
        item.setName("Pensil");
        item.setPrice(2000);

        Inventory inventory = new Inventory();
        inventory.setId(5L);
        inventory.setQty(10000);
        inventory.setType("T");
        inventory.setItem(item);

        InventoryResponseDTO expectedResponse = new InventoryResponseDTO();
        expectedResponse.setId(5L);
        expectedResponse.setItemId(11L);
        expectedResponse.setQty(10000);
        expectedResponse.setType("T");

        // Mocking repository dan mapper
        when(inventoryRepository.findById(11L)).thenReturn(Optional.of(inventory));
        doReturn(expectedResponse).when(inventoryService).mappingToResponse(inventory);

        // Act
        ResponseObject<InventoryResponseDTO> response = inventoryService.getDetailById(11L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getStatus());
        assertEquals("get data success", response.getMessage());
        assertEquals(expectedResponse,response.getContent());
        assertEquals(expectedResponse.getId(), response.getContent().getId());
        assertEquals(expectedResponse.getItemId(), response.getContent().getItemId());
        assertEquals(expectedResponse.getQty(), response.getContent().getQty());
        assertEquals(expectedResponse.getType(), response.getContent().getType());
    }

    @Test
    void testDeleteInventorySuccess() {
        // Arrange
        Item item = new Item();
        item.setId(11L);
        item.setName("Pensil");
        item.setPrice(2000);

        Inventory inventory = new Inventory();
        inventory.setId(5L);
        inventory.setQty(10000);
        inventory.setType("T");
        inventory.setItem(item);

        // Mock behavior: findById and delete
        when(inventoryRepository.findById(5L)).thenReturn(Optional.of(inventory));
        doNothing().when(inventoryRepository).delete(any(Inventory.class));

        // Act
        ResponseObject response = inventoryService.delete(5L);


        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getStatus());
        assertEquals("delete success", response.getMessage());
        assertNull(response.getContent());
    }
}
