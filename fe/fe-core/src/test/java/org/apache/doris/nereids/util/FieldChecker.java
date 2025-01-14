// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.apache.doris.nereids.util;

import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class FieldChecker {

    public final List<String> fields;

    public FieldChecker(List<String> fields) {
        this.fields = fields;
    }

    public <T> Predicate<T> check(List<Object> valueList) {
        return (o) -> {
            Assertions.assertEquals(fields.size(), valueList.size());
            Class<?> classInfo = o.getClass();
            IntStream.range(0, valueList.size()).forEach(i -> {
                Field field;
                try {
                    field = classInfo.getDeclaredField(this.fields.get(i));
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
                field.setAccessible(true);
                try {
                    Assertions.assertEquals(valueList.get(i), field.get(o));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
            return true;
        };
    }
}
