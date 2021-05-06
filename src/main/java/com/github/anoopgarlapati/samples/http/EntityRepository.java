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

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.data.rest.webmvc.json.patch.Patch;

/**
 * @author Anoop Garlapati
 */
public interface EntityRepository {

    boolean create(Entity entity);

    Entity findById(String id);

    Entity put(Entity entity);

    Entity patchUsingNonNull(Entity entity);

    Entity patch(String id, JsonMergePatch entityPatch);
}
