package org.gsoc.samoa.streaming.streams;

import org.apache.samoa.instances.Instance;
import org.apache.samoa.instances.InstancesHeader;
import org.apache.samoa.moa.core.Example;
import org.apache.samoa.moa.core.ObjectRepository;
import org.apache.samoa.moa.options.AbstractOptionHandler;
import org.apache.samoa.moa.tasks.TaskMonitor;
import org.apache.samoa.streams.InstanceStream;
import org.apache.samoa.streams.clustering.ClusteringStream;

/**
 * Created by mahesh on 7/17/16.
 */
public class MyClusteringStream  extends ClusteringStream {

    @Override
    protected void prepareForUseImpl(TaskMonitor taskMonitor, ObjectRepository objectRepository) {

    }

    @Override
    public InstancesHeader getHeader() {
        return null;
    }

    @Override
    public long estimatedRemainingInstances() {
        return 0;
    }

    @Override
    public boolean hasMoreInstances() {
        return false;
    }

    @Override
    public Example<Instance> nextInstance() {
        return null;
    }

    @Override
    public boolean isRestartable() {
        return false;
    }

    @Override
    public void restart() {

    }

    @Override
    public void getDescription(StringBuilder stringBuilder, int i) {

    }
}
