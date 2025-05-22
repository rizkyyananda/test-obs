package com.obs.test.service.impl;

import com.obs.test.dto.ResponseObject;
import com.obs.test.dto.request.ItemRequestDTO;
import com.obs.test.dto.response.ItemResponseDTO;
import com.obs.test.dto.response.PaginationResponse;
import com.obs.test.entity.Item;
import com.obs.test.exception.BadRequestException;
import com.obs.test.exception.EntityNotFound;
import com.obs.test.repository.ItemRepository;
import com.obs.test.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public ResponseObject<PaginationResponse<ItemResponseDTO>> getAll(int page, int rows, String sortBy, String sortDirection, String search) {
        // Minimal halaman = 1
        page = Math.max(page, 1);

        // Sorting
        Sort sort = sortDirection.equalsIgnoreCase("DESC") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page - 1, rows, sort);

        // Ambil data
        Page<Item> itemPage = (search != null && !search.isEmpty())
                ? itemRepository.findByNameContainingIgnoreCase(search, pageable)
                : itemRepository.findAll(pageable);

        List<ItemResponseDTO> itemDTOs = mappingToResponseList(itemPage.getContent());

        PaginationResponse<ItemResponseDTO> pagination = new PaginationResponse<ItemResponseDTO>(
                itemPage.getNumber() + 1,
                itemPage.getSize(),
                itemPage.getTotalElements(),
                itemPage.getTotalPages(),
                itemDTOs                             // content
        );

        return new ResponseObject<>(HttpStatus.OK, "Success", "get data success", pagination);
    }

    @Override
    public ResponseObject<ItemResponseDTO> saveAndUpdate(ItemRequestDTO dto) {
        try {
            if (dto.getName().equals("")){
                return new ResponseObject(HttpStatus.BAD_REQUEST, "Failed", "failed save or update", "name can't be empty");
            }

            if (dto.getPrice() <=0){
                return new ResponseObject(HttpStatus.BAD_REQUEST, "Failed", "failed save or update", "price can't be empty");
            }
            Item item = this.mappingDTOItemToDomain(dto);
            item = itemRepository.save(item);
            ItemResponseDTO itemResponseDTO = this.mappingToResponse(item);
            return new ResponseObject(HttpStatus.OK, "Success", "save or update data success", itemResponseDTO);
        }catch (Exception e){
            return new ResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "Failed", e.getMessage());
        }

    }

    @Override
    public ResponseObject<ItemResponseDTO> getDetailById(Long id) {
        try {
            Optional<Item> item = itemRepository.findById(id);
            if (item.isPresent()){
                ItemResponseDTO itemResponseDTO = mappingToResponse(item.get());
                return new ResponseObject(HttpStatus.OK, "Success", "get data success", itemResponseDTO);
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
            Optional<Item> item = itemRepository.findById(id);
            if (item.isPresent()){
                itemRepository.delete(item.get());
                return new ResponseObject(HttpStatus.OK, "Success", "delete success");
            }else{
                return new ResponseObject(HttpStatus.NOT_FOUND, "Failed", "data not found");
            }
        }catch (Exception e){
            return new ResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "Failed", e.getMessage());
        }

    }

    private Item mappingDTOItemToDomain(ItemRequestDTO dto){
        Item item = new Item();
        if (dto.getId() != null) item.setId(dto.getId());
        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        return item;

    }

    private ItemResponseDTO mappingToResponse(Item item){
        ItemResponseDTO dto = new ItemResponseDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setPrice(item.getPrice());
        return dto;
    }

    private List<ItemResponseDTO>mappingToResponseList(List<Item> items){
        List<ItemResponseDTO> itemResponseDTOList = items
                .stream()
                .map(item -> {
                    ItemResponseDTO dto = new ItemResponseDTO();
                    dto.setId(item.getId());
                    dto.setName(item.getName());
                    dto.setPrice(item.getPrice());
                    return dto;
                })
                .collect(Collectors.toList());

        return itemResponseDTOList;

    }



}
