package com.obs.test;

import com.obs.test.dto.ResponseObject;
import com.obs.test.dto.request.InventoryRequestDTO;
import com.obs.test.dto.request.ItemRequestDTO;
import com.obs.test.dto.response.InventoryResponseDTO;
import com.obs.test.dto.response.ItemResponseDTO;
import com.obs.test.dto.response.PaginationResponse;
import com.obs.test.entity.Inventory;
import com.obs.test.entity.Item;
import com.obs.test.repository.ItemRepository;
import com.obs.test.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.rmi.NotBoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @InjectMocks
    @Spy
    private ItemServiceImpl itemService;
    @Mock
    private ItemRepository itemRepository;

    @Test
    void testGetAllItemSuccess() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Pensil");
        item1.setPrice(1000);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Pena");
        item2.setPrice(2000);

        List<Item> itemList = List.of(item1, item2);
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending());
        Page<Item> itemPage = new PageImpl<>(itemList, pageable, itemList.size());

        when(itemRepository.findAll(any(Pageable.class))).thenReturn(itemPage);

        ResponseObject<PaginationResponse<ItemResponseDTO>> response = itemService.getAll(1, 2, "id", "ASC", "");

        assertEquals("Success", response.getStatus());
        assertEquals("get data success", response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(2, response.getContent().getContent().size());
        assertEquals(1, response.getContent().getTotalPages());
        assertEquals(2, response.getContent().getTotalItems());

    }

    @Test
    void testSaveAndUpdate_Success() {
        // Arrange
        ItemRequestDTO itemRequestDTO = new ItemRequestDTO(9L, "Pensil", 1000);

        Item itemToSave = new Item();
        itemToSave.setId(9L);
        itemToSave.setName("Pensil");
        itemToSave.setPrice(1000);

        // Simulasi hasil save
        Item savedItem = new Item();
        savedItem.setId(9L);
        savedItem.setName("Pensil");
        savedItem.setPrice(1000);

        ItemResponseDTO expectedResponse = new ItemResponseDTO(9L, "Pensil", 1000);

        // Mock behavior
        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);

        // Act
        ResponseObject<ItemResponseDTO> response = itemService.saveAndUpdate(itemRequestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getStatus());
        assertEquals("save or update data success", response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(expectedResponse.getId(), response.getContent().getId());
        assertEquals(expectedResponse.getName(), response.getContent().getName());
        assertEquals(expectedResponse.getPrice(), response.getContent().getPrice());
    }

    @Test
    void testGetItemByid_Success(){
        //mock data
        Item item = new Item();
        item.setId(11L);
        item.setName("Pensil");
        item.setPrice(2000);

        //mock response
        ItemResponseDTO expectedResponse = new ItemResponseDTO();
        expectedResponse.setId(11L);
        expectedResponse.setName("Pensil");
        expectedResponse.setPrice(2000);

        when(itemRepository.findById(11L)).thenReturn(Optional.of(item));
        ResponseObject<ItemResponseDTO> response = itemService.getDetailById(item.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getStatus());
        assertEquals("get data success", response.getMessage());
        assertNotNull(response.getContent());

    }


}
