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

import com.github.javacliparser.Configurable;
import com.github.javacliparser.IntOption;
import com.github.javacliparser.StringOption;
import org.apache.samoa.evaluation.ClusteringEvaluatorProcessor;
import org.apache.samoa.learners.Learner;
import org.apache.samoa.tasks.Task;
import org.apache.samoa.topology.ComponentFactory;
import org.apache.samoa.topology.Stream;
import org.apache.samoa.topology.Topology;
import org.apache.samoa.topology.TopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mahesh on 7/14/16.
 */

public class GsocTask implements Task,Configurable {

    private static final long serialVersionUID = -5134935141154021352L;
    private static Logger logger = LoggerFactory.getLogger(GsocTask.class);

    /** The topology builder for the task. */
    private TopologyBuilder builder;
    /** The topology that will be created for the task */
    private Topology gsocTopology;


    public IntOption instanceLimitOption = new IntOption("instanceLimit", 'i',
            "Maximum number of instances to generate (-1 = no limit).", 1000000, -1, Integer.MAX_VALUE);

    public IntOption gsocParallelismOption = new IntOption("parallelismOption", 'p',
            "Number of destination Processors", 1, 1, Integer.MAX_VALUE);

    public StringOption evaluationNameOption = new StringOption("evaluationName", 'n',
            "Identifier of the evaluation", "GsocTask" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

    private Learner learner;
    private ClusteringEvaluatorProcessor evaluator;

    @Override
    public void init(){
        // create source EntranceProcessor
    /* The event source for the topology. Implements EntranceProcessor */
        GsocSourceProcessor sourceProcessor = new GsocSourceProcessor(instanceLimitOption.getValue());
        builder.addEntranceProcessor(sourceProcessor);

        // create Stream
        Stream stream = builder.createStream(sourceProcessor);

        // create destination Processor
    /* The event sink for the topology. Implements Processor */
        GsocDestinationProcessor destProcessor = new GsocDestinationProcessor();
        builder.addProcessor(destProcessor, gsocParallelismOption.getValue());
        builder.connectInputShuffleStream(stream, destProcessor);

        // build the topology
        gsocTopology = builder.build();
        System.out.println("Successfully built the topology");
        logger.debug("Successfully built the topology");
    }

    @Override
    public Topology getTopology()
    {
        return gsocTopology;
    }

    @Override
    public void setFactory(ComponentFactory factory) {
        // will be removed when dynamic binding is implemented
        builder = new TopologyBuilder(factory);
        logger.debug("Successfully instantiating TopologyBuilder");
        builder.initTopology(evaluationNameOption.getValue());
        logger.debug("Successfully initializing SAMOA topology with name {}", evaluationNameOption.getValue());
    }

}
