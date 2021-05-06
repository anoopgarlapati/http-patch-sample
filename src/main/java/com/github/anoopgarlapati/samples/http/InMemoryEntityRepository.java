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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Anoop Garlapati
 */
@Service
public class InMemoryEntityRepository implements EntityRepository {

    Map<String, Entity> entityMap = new ConcurrentHashMap<>();

    @Override
    public boolean create(Entity entity) {
        Entity existingEntity = this.entityMap.putIfAbsent(entity.getId(), entity);
        return existingEntity == null;
    }

    @Override
    public Entity findById(String id) {
        return this.entityMap.get(id);
    }

    @Override
    public Entity put(Entity entity) {
        this.entityMap.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Entity patchUsingNonNull(Entity patchEntity) {
        Entity entity = this.entityMap.get(patchEntity.getId());
        if (entity == null) {
            return null;
        }
        CopyNonNullProperties.patch(entity, patchEntity);
        return this.put(entity);
    }

    @Override
    public Entity patch(String id, JsonMergePatch patch) {
        Entity entity = this.entityMap.get(id);
        if (entity == null) {
            return null;
        }
        try {
            entity = jsonMergePatch(entity, patch, Entity.class);
        } catch (Exception ex) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return this.put(entity);
    }

    static <T> T jsonMergePatch(T entity, JsonMergePatch patch, Class<T> clazz)
            throws JsonPatchException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode entityNode = mapper.convertValue(entity, JsonNode.class);
        entityNode = patch.apply(entityNode);
        return mapper.treeToValue(entityNode, clazz);
    }

    static class CopyNonNullProperties extends BeanUtilsBean {

        public static void patch(Object dest, Object source) {
            BeanUtilsBean copyNonNullValues = new CopyNonNullProperties();
            try {
                copyNonNullValues.copyProperties(dest, source);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @Override
        public void copyProperty(Object dest, String name, Object value) 
                throws IllegalAccessException, InvocationTargetException {
            if (value == null) {
                return;
            }
            super.copyProperty(dest, name, value);
        }
    }

}
