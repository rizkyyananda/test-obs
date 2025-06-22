package com.obs.test;

import com.obs.test.dto.ResponseObject;
import com.obs.test.dto.request.OrderRequestDTO;
import com.obs.test.dto.response.OrderResponseDTO;
import com.obs.test.entity.Inventory;
import com.obs.test.entity.Item;
import com.obs.test.entity.Order;
import com.obs.test.repository.InventoryRepository;
import com.obs.test.repository.OrderRepository;
import com.obs.test.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    @Spy
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        Order order = new Order();
        order.setId(1L);
        order.setQty(5);
        order.setPrice(100);
        order.setOrderNo("ORD001");
        Item item = new Item();
        item.setId(1L);
        Inventory inventory = new Inventory();
        inventory.setItem(item);
        order.setItem(item);

        Page<Order> page = new PageImpl<>(Arrays.asList(order));
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(page);

        ResponseObject<?> response = orderService.getAll(1, 10, "id", "ASC", "");

        assertEquals("Success", response.getStatus());
        assertEquals(1, page.getContent().size());
    }

    @Test
    void testGetDetailById_found() {
        Order order = new Order();
        order.setId(1L);
        Item item = new Item();
        item.setId(10L);
        order.setItem(item);
        order.setQty(2);
        order.setOrderNo("ORD001");
        order.setPrice(1000);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        ResponseObject<OrderResponseDTO> response = orderService.getDetailById(1L);

        assertEquals("Success", response.getStatus());
        assertEquals("ORD001", response.getContent().getOrderNo());
    }

    @Test
    void testGetDetailById_notFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseObject<OrderResponseDTO> response = orderService.getDetailById(99L);

        assertEquals("Failed", response.getStatus());
        assertEquals("data not found", response.getMessage());
    }

    @Test
    void testDelete_found() {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        ResponseObject<?> response = orderService.delete(1L);

        assertEquals("Success", response.getStatus());
        verify(orderRepository).delete(order);
    }

    @Test
    void testDelete_notFound() {
        when(orderRepository.findById(2L)).thenReturn(Optional.empty());

        ResponseObject<?> response = orderService.delete(2L);

        assertEquals("Failed", response.getStatus());
    }

    @Test
    void testSaveAndUpdate_invalidQty() {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setQty(0);
        dto.setPrice(100);
        ResponseObject<?> response = orderService.saveAndUpdate(dto);
        assertEquals("Failed", response.getStatus());
        assertEquals("qty can't be empty", response.getContent());
    }

    @Test
    void testSaveAndUpdate_invalidPrice() {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setQty(1);
        dto.setPrice(0);
        ResponseObject<?> response = orderService.saveAndUpdate(dto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed", response.getStatus());
        assertEquals("price can't be empty", response.getContent());
    }

    @Test
    void testSaveAndUpdate_success() {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setItemId(13L);
        dto.setQty(2);
        dto.setPrice(100);
        dto.setOrderNo("ORD123");

        Item item = new Item();
        item.setId(13L);

        Inventory inventory = new Inventory();
        inventory.setItem(item);
        inventory.setQty(10);

        Order savedOrder = new Order();
        savedOrder.setItem(item);
        savedOrder.setQty(2);
        savedOrder.setOrderNo("ORD123");
        savedOrder.setPrice(100);

        when(inventoryRepository.findInventoryByItemId(13L)).thenReturn(Optional.of(inventory));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        ResponseObject<OrderResponseDTO> response = orderService.saveAndUpdate(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getStatus());
        assertEquals("save or update data success", response.getMessage());
        verify(inventoryRepository).save(any(Inventory.class));
    }
}
