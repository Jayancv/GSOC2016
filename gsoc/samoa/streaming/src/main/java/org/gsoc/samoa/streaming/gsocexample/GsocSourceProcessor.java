package org.gsoc.samoa.streaming.gsocexample;

/*
 * #%L
 * SAMOA
 * %%
 * Copyright (C) 2014 - 2016 Apache Software Foundation
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.apache.samoa.core.ContentEvent;
import org.apache.samoa.core.EntranceProcessor;
import org.apache.samoa.core.Processor;

import java.util.Random;

/**
 * Created by mahesh on 7/14/16.
 */

public class GsocSourceProcessor implements EntranceProcessor {

    private static final long serialVersionUID = 6212296305865604747L;
    private Random rnd;
    private final long maxInst;
    private long count;

    public GsocSourceProcessor(long maxInst){
        this.maxInst = maxInst;
    }

    @Override
    public boolean process(ContentEvent event) {
        // do nothing, API will be refined further
        System.out.println("Source Processor: process");
        return false;
    }

    @Override
    public void onCreate(int id) {
        System.out.println("Source Processor: onCreate");
        rnd = new Random(id);
    }

    @Override
    public Processor newProcessor(Processor p) {
        System.out.println("Source Processor: newProcessor");
        GsocSourceProcessor hwsp = (GsocSourceProcessor) p;
        return new GsocSourceProcessor(hwsp.maxInst);
    }

    @Override
    public boolean isFinished()
    {
        return count >= maxInst;
    }

    @Override
    public boolean hasNext() {
        return count < maxInst;
    }

    @Override
    public ContentEvent nextEvent() {
        count++;
        System.out.println("Source Processor: nextEvent");
        return new GsocContentEvent(rnd.nextInt(), false);
    }
}
