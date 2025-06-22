package com.obs.test.service.impl;

import com.obs.test.dto.ResponseObject;
import com.obs.test.dto.request.OrderRequestDTO;
import com.obs.test.dto.response.OrderResponseDTO;
import com.obs.test.dto.response.PaginationResponse;
import com.obs.test.entity.Inventory;
import com.obs.test.entity.Order;
import com.obs.test.repository.InventoryRepository;
import com.obs.test.repository.OrderRepository;
import com.obs.test.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.rmi.NotBoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public ResponseObject<PaginationResponse<OrderResponseDTO>> getAll(int page, int rows, String sortBy, String sortDirection, String search) {
        page = Math.max(page, 1);

        // Sorting
        Sort sort = sortDirection.equalsIgnoreCase("DESC") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page - 1, rows, sort);

        // Ambil data
        Page<Order> orders = orderRepository.findAll(pageable);

        List<OrderResponseDTO> itemDTOs = mappingToResponseList(orders.getContent());

        PaginationResponse<OrderResponseDTO> pagination = new PaginationResponse<OrderResponseDTO>(
                orders.getNumber() + 1,
                orders.getSize(),
                orders.getTotalElements(),
                orders.getTotalPages(),
                itemDTOs
        );

        return new ResponseObject<>(HttpStatus.OK, "Success", "get data success", pagination);
    }

    @Override
    public ResponseObject<OrderResponseDTO> saveAndUpdate(OrderRequestDTO dto) {
        try {
            if (dto.getQty()<= 0){
                return new ResponseObject(HttpStatus.BAD_REQUEST, "Failed", "failed save or update", "qty can't be empty");
            }

            if (dto.getPrice() <=0){
                return new ResponseObject(HttpStatus.BAD_REQUEST, "Failed", "failed save or update", "price can't be empty");
            }
            if (dto.getOrderNo() == null || dto.getOrderNo().isBlank()) {
                dto.setOrderNo(generateOrderNo());
            }
            Optional<Inventory> inventory = inventoryRepository.findInventoryByItemId(dto.getItemId());
            if (inventory.isPresent()){
                Inventory inventoryData = inventory.get();
                if (inventoryData.getQty() <= 0 && inventoryData.getQty() < dto.getQty()){
                    return new ResponseObject(HttpStatus.BAD_REQUEST, "Failed", "gagal membuat orderan, stock tidak tersedia");
                }else{
                    int qtyOld = 0;
                    if (dto.getId() != null){
                        Optional<Order> oldORder = orderRepository.findById(dto.getId());
                        Order orderOldData = oldORder.get();
                        qtyOld = orderOldData.getQty();
                    }
                    Order order = this.mappingDTOItemToDomain(dto);
                    order = orderRepository.save(order);
                    OrderResponseDTO itemResponseDTO = this.mappingToResponse(order);
                    boolean isEqualsQty = dto.getQty() == qtyOld;
                    if (!isEqualsQty){
                        if (dto.getQty() < qtyOld){
                            inventoryData.setQty(inventoryData.getQty() + dto.getQty());
                        }else{
                            inventoryData.setQty(inventoryData.getQty() - dto.getQty());
                        }
                        inventoryRepository.save(inventoryData);
                    }
                    return new ResponseObject(HttpStatus.OK, "Success", "save or update data success", itemResponseDTO);
                }
            }else{
                return new ResponseObject(HttpStatus.BAD_REQUEST, "Failed", "gagal membuat orderan, stock tidak tersedia");
            }

        }catch (Exception e){
            return new ResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "Failed", "internal server error");
        }
    }

    private String generateOrderNo() {
        String prefix = "ORD";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = String.format("%05d", new Random().nextInt(100000)); // 00000 - 99999
        return prefix + "-" + date + "-" + randomPart;
    }

    @Override
    public ResponseObject<OrderResponseDTO> getDetailById(Long id) {
        try {
            Optional<Order> order = orderRepository.findById(id);
            if (order.isPresent()){
                OrderResponseDTO orderResponseDTO = mappingToResponse(order.get());
                return new ResponseObject(HttpStatus.OK, "Success", "get data success", orderResponseDTO);
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
            Optional<Order> order = orderRepository.findById(id);
            if (order.isPresent()){
                orderRepository.delete(order.get());
                return new ResponseObject(HttpStatus.OK, "Success", "delete success");
            }else{
                return new ResponseObject(HttpStatus.NOT_FOUND, "Failed", "data not found");
            }
        }catch (Exception e){
            return new ResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "Failed", e.getMessage());
        }
    }


    private List<OrderResponseDTO> mappingToResponseList(List<Order> orderList){
        List<OrderResponseDTO> orderResponseDTOList = orderList
                .stream()
                .map(order -> {
                    OrderResponseDTO dto = new OrderResponseDTO();
                    dto.setId(order.getId());
                    dto.setOrderNo(order.getOrderNo());
                    dto.setItemId(order.getItem().getId());
                    dto.setQty(order.getQty());
                    dto.setPrice(order.getPrice());
                    return dto;
                })
                .collect(Collectors.toList());

        return orderResponseDTOList;

    }

    private Order mappingDTOItemToDomain(OrderRequestDTO dto) throws NotBoundException {
        Optional<Inventory> inventory = inventoryRepository.findInventoryByItemId(dto.getItemId());
        if (inventory.isPresent()){
            Inventory itemData = inventory.get();
            Order order = new Order();
            if (dto.getId() != null) order.setId(dto.getId());
            order.setItem(itemData.getItem());
            order.setQty(dto.getQty());
            order.setOrderNo(dto.getOrderNo());
            order.setPrice(dto.getPrice());
            return order;
        }else{
            throw new NotBoundException("data not found");
        }
    }

    private OrderResponseDTO mappingToResponse(Order order){
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setItemId(order.getItem().getId());
        dto.setQty(order.getQty());
        dto.setOrderNo(order.getOrderNo());
        dto.setPrice(order.getPrice());
        return dto;
    }
}
