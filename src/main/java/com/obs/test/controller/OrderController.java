package com.obs.test.controller;


import com.obs.test.dto.ResponseObject;
import com.obs.test.dto.request.OrderRequestDTO;
import com.obs.test.dto.response.OrderResponseDTO;
import com.obs.test.dto.response.PaginationResponse;
import com.obs.test.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private ResponseEntity<Object> newErrorResponse(HttpStatus httpStatus, String status, String errorMsg, String[] errorCode){
        return new ResponseEntity<>(new ResponseObject<Object>(httpStatus, status, errorMsg, errorCode), httpStatus);
    }

    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public ResponseEntity<Object> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int rows,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "") String search) {

        ResponseObject<PaginationResponse<OrderResponseDTO>> itemResponseDTOList = orderService.getAll(page, rows, sortBy, sortDirection, search);
        return new ResponseEntity(itemResponseDTOList, itemResponseDTOList.getStatusCode());
    }

    @PostMapping("/save")
    public ResponseEntity<Object>save(@RequestBody OrderRequestDTO dto){
        ResponseObject<OrderResponseDTO> orderRes = orderService.saveAndUpdate(dto);
        return new ResponseEntity(orderRes, orderRes.getStatusCode());
    }

    @PutMapping("/update")
    public ResponseEntity<Object>update(@RequestBody OrderRequestDTO dto){
        ResponseObject<OrderResponseDTO> orderRes = orderService.saveAndUpdate(dto);
        return new ResponseEntity(orderRes, orderRes.getStatusCode());
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable ("id") Long id) {
        ResponseObject<OrderResponseDTO> itemRes = orderService.getDetailById(id);
        return new ResponseEntity(itemRes, itemRes.getStatusCode());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try {
            ResponseObject res = orderService.delete(id);
            return new ResponseEntity(res, res.getStatusCode());
        }catch (Exception e){
            return newErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED", e.getMessage(), null);
        }
    }
}
