package com.ifpe.userApi.controller;

import com.ifpe.userApi.DTO.tag.TagCreateDTO;
import com.ifpe.userApi.DTO.tag.TagResponseDTO;
import com.ifpe.userApi.service.TagService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/tags")
@Slf4j
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    ResponseEntity<Void> createTag(@Valid TagCreateDTO tagCreateDTO) {
        log.info("TagController :: createTag :: Received request to create tag.");
        URI location = tagService.create(tagCreateDTO);
        log.info("TagController :: createTag :: Tag created successfully. Location: {}", location);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    ResponseEntity<TagResponseDTO> getTagById(Long id) {
        log.info("TagController :: getTagById :: Received request to fetch tag by ID: {}", id);
        return ResponseEntity.ok(tagService.findById(id));
    }

    @GetMapping
    ResponseEntity<Page<TagResponseDTO>> getAllTags(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        log.info("TagController :: getAllTags :: Received request to fetch all tags.");
        return ResponseEntity.ok(tagService.findAllTags(page, size));
    }
}
