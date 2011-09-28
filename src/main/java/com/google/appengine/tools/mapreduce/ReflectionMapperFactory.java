package com.google.appengine.tools.mapreduce;
/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.ReflectionUtils;

/**
 * ReflectionMapperFactory is a {@link MapperFactory} class that is using the utility {@link ReflectionUtils} that comes
 * from <code>hadoop</hadoop> to instantiate the provided mapper classes.
 *
 * @author Miroslav Genov (mgenov@gmail.com)
 */
class ReflectionMapperFactory implements MapperFactory {

  /**
   * Creates a {@link AppEngineMapper} for this task invocation.
   *
   * @param <INKEY> the type of the input keys for this mapper
   * @param <INVALUE> the type of the input values for this mapper
   * @param <OUTKEY> the type of the output keys for this mapper
   * @param <OUTVALUE> the type of the output values for this mapper
   * @return the new mapper
   */
  @Override
  public <INKEY, INVALUE, OUTKEY, OUTVALUE> AppEngineMapper<INKEY, INVALUE, OUTKEY, OUTVALUE> createMapper(Class<? extends Mapper<?, ?, ?, ?>> mapperClass, Configuration configuration) {
    return (AppEngineMapper<INKEY, INVALUE, OUTKEY, OUTVALUE>)
            ReflectionUtils.newInstance(mapperClass,
                    configuration);
  }
}
