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
package org.elasticsearch.index.translog;

import org.apache.lucene.store.ByteArrayDataOutput;
import org.apache.lucene.store.DataInput;
import org.apache.lucene.store.DataOutput;
import org.apache.lucene.util.RamUsageEstimator;
import org.elasticsearch.common.io.Channels;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 */
class Checkpoint {

   static final int BUFFER_SIZE = RamUsageEstimator.NUM_BYTES_INT  // ops
            + RamUsageEstimator.NUM_BYTES_LONG // offset
            + RamUsageEstimator.NUM_BYTES_LONG;// id
    final long offset;
    final int numOps;
    final long translogId;

    Checkpoint(long offset, int numOps, long translogId) {
        this.offset = offset;
        this.numOps = numOps;
        this.translogId = translogId;
    }

    Checkpoint(FileChannel in, long position) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        in.read(buffer, position);
        buffer.flip();
        offset = buffer.getLong();
        numOps = buffer.getInt();
        translogId = buffer.getLong();
    }

    Checkpoint(DataInput in) throws IOException {
        offset = in.readLong();
        numOps = in.readInt();
        translogId = in.readLong();
    }

    void write(FileChannel channel) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        final ByteArrayDataOutput out = new ByteArrayDataOutput(buffer);
        write(out);
        Channels.writeToChannel(buffer, channel);
    }

    public void write(DataOutput out) throws IOException {
        out.writeLong(offset);
        out.writeInt(numOps);
        out.writeLong(translogId);
    }

    @Override
    public String toString() {
        return "TranslogInfo{" +
                "offset=" + offset +
                ", numOps=" + numOps +
                ", translogId= " + translogId +
                '}';
    }

}