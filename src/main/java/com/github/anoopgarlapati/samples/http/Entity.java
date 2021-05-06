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

import java.util.List;

/**
 * @author Anoop Garlapati
 */
public class Entity {

    private String id;
    private String alpha;
    private Boolean bravo;
    private List<String> charlie;
    private Delta delta;

    public Entity() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlpha() {
        return this.alpha;
    }

    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

    public Boolean getBravo() {
        return this.bravo;
    }

    public void setBravo(Boolean bravo) {
        this.bravo = bravo;
    }

    public List<String> getCharlie() {
        return this.charlie;
    }

    public void setCharlie(List<String> charlie) {
        this.charlie = charlie;
    }

    public Delta getDelta() {
        return this.delta;
    }

    public void setDelta(Delta delta) {
        this.delta = delta;
    }

    public static class Delta {

        private String echo;
        private String foxtrot;

        public Delta() {
        }

        public String getEcho() {
            return this.echo;
        }

        public void setEcho(String echo) {
            this.echo = echo;
        }

        public String getFoxtrot() {
            return this.foxtrot;
        }

        public void setFoxtrot(String foxtrot) {
            this.foxtrot = foxtrot;
        }
    }
}
