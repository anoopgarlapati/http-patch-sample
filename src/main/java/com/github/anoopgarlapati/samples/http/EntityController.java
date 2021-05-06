/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.anoopgarlapati.samples.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Anoop Garlapati
 */
@RestController
public class EntityController {

    private final InMemoryEntityRepository entityRepository;
    private final ObjectMapper objectMapper;

    public EntityController(InMemoryEntityRepository entityRepository) {
        this.entityRepository = entityRepository;
        this.objectMapper = new ObjectMapper();
    }

    @PostMapping("/entities")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Entity create(@RequestBody Entity entity) {
        boolean created = this.entityRepository.create(entity);
        if (!created) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
        return entity;
    }

    @GetMapping("/entities/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Entity getById(@PathVariable("id") String id) {
        Entity entity = this.entityRepository.findById(id);
        if (entity == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        return entity;
    }

    @PutMapping("/entities/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Entity put(@PathVariable("id") String id, @RequestBody Entity entity) {
        Entity existingEntity = this.entityRepository.findById(id);
        if (existingEntity == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        entity.setId(id);
        return this.entityRepository.put(entity);
    }

    @PatchMapping("/entities/copy-nonnull/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Entity patchCopyNonNull(@PathVariable("id") String id, @RequestBody Entity patchEntity) {
        patchEntity.setId(id);
        Entity entity = this.entityRepository.patchUsingNonNull(patchEntity);
        if (entity == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        return entity;
    }

    @PatchMapping("/entities/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Entity patch(@PathVariable("id") String id, HttpServletRequest request,
                        @io.swagger.v3.oas.annotations.parameters.RequestBody Entity requestEntity) {
        // requestEntity in the above method parameters is for documentation purpose only
        Entity entity = this.entityRepository.patch(id, this.convertRequestToPatch(request));
        if (entity == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        return entity;
    }

    private JsonMergePatch convertRequestToPatch(HttpServletRequest request) {
        try {
            String requestBody = IOUtils.toString(request.getReader());
            return objectMapper.readValue(requestBody, JsonMergePatch.class);
        } catch (IOException ex) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler({HttpClientErrorException.class, HttpServerErrorException.class})
    public void handleHttpErrorException(HttpStatusCodeException ex, HttpServletResponse response) throws IOException {
        response.sendError(ex.getRawStatusCode(), ex.getStatusText());
    }

}
