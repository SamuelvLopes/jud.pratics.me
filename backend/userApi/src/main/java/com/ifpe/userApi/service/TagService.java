package com.ifpe.userApi.service;

import com.ifpe.userApi.DTO.tag.TagCreateDTO;
import com.ifpe.userApi.DTO.tag.TagResponseDTO;
import com.ifpe.userApi.entities.Tag;
import com.ifpe.userApi.exceptions.ResourceNotFoundException;
import com.ifpe.userApi.repository.TagRepository;
import com.ifpe.userApi.util.dto.DTOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Service
@Slf4j
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional
    public URI create(TagCreateDTO data) {
        Tag tag;
        try{
            log.info("TagService :: create :: Starting tag creation process...");
            tag = Tag.builder().name(data.name()).build();
            log.info("TagService :: create :: Tag creation process completed.");
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tag.getId())
                .toUri();

        log.info("TagService :: create :: Tag creation process completed. Location: {}", location);
        return location;
    }

    @Transactional(readOnly = true)
    public TagResponseDTO findById(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Tag not found"));
        return new TagResponseDTO(tag.getName());
    }

    @Transactional(readOnly = true)
    public Page<TagResponseDTO> findAllTags(int page, int size) {
        Page<Tag> tags = tagRepository.findAll(PageRequest.of(page, size));
        return tags.map(tag -> new TagResponseDTO(tag.getName()));
    }
}
