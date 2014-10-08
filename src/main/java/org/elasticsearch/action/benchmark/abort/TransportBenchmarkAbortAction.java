/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.action.benchmark.abort;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.benchmark.BenchmarkCoordinatorService;
import org.elasticsearch.action.support.master.TransportMasterNodeOperationAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

/**
 * Transport action for benchmark abort requests
 */
public class TransportBenchmarkAbortAction extends TransportMasterNodeOperationAction<BenchmarkAbortRequest, BenchmarkAbortResponse> {

    private final BenchmarkCoordinatorService service;

    @Inject
    public TransportBenchmarkAbortAction(Settings settings, TransportService transportService, ClusterService clusterService,
                                         ThreadPool threadPool, BenchmarkCoordinatorService service, ActionFilters actionFilters) {
        super(settings, BenchmarkAbortAction.NAME, transportService, clusterService, threadPool, actionFilters);
        this.service = service;
    }

    @Override
    protected String executor() {
        return ThreadPool.Names.GENERIC;
    }

    @Override
    protected BenchmarkAbortRequest newRequest() {
        return new BenchmarkAbortRequest();
    }

    @Override
    protected BenchmarkAbortResponse newResponse() {
        return new BenchmarkAbortResponse();
    }

    @Override
    protected void masterOperation(BenchmarkAbortRequest request, ClusterState state, final ActionListener<BenchmarkAbortResponse> listener) throws ElasticsearchException {
        service.abortBenchmark(request, listener);
    }
}