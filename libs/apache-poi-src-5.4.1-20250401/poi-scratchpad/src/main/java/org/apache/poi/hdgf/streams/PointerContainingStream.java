/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.hdgf.streams;

import org.apache.logging.log4j.Logger;
import org.apache.poi.logging.PoiLogManager;
import org.apache.poi.hdgf.chunks.ChunkFactory;
import org.apache.poi.hdgf.pointers.Pointer;
import org.apache.poi.hdgf.pointers.PointerFactory;

/**
 * A stream that holds pointers, possibly in addition to some
 *  other data too.
 */
public class PointerContainingStream extends Stream { // TODO - instantiable superclass
    private static final Logger LOG = PoiLogManager.getLogger(PointerContainingStream.class);

    private static int MAX_CHILDREN_NESTING = 500;

    private final Pointer[] childPointers;
    private Stream[] childStreams;

    private final ChunkFactory chunkFactory;
    private final PointerFactory pointerFactory;

    protected PointerContainingStream(Pointer pointer, StreamStore store, ChunkFactory chunkFactory, PointerFactory pointerFactory) {
        super(pointer, store);
        this.chunkFactory = chunkFactory;
        this.pointerFactory = pointerFactory;

        // Have the child pointers identified and created
        childPointers = pointerFactory.createContainerPointers(pointer, store.getContents());
    }

    /**
     * Returns all the pointers that we contain
     */
    protected Pointer[] getChildPointers() { return childPointers; }
    /**
     * Returns all the "child" streams.
     * These are all the streams pointed to by the pointers
     *  that we contain.
     */
    public Stream[] getPointedToStreams() { return childStreams; }

    /**
     * Performs a recursive search, identifying the pointers we contain,
     *  creating the Streams for where they point to, then searching
     *  those if appropriate.
     */
    public void findChildren(byte[] documentData) {
        findChildren(documentData, 0);
    }

    private void findChildren(byte[] documentData, int nesting) {
        if (nesting > MAX_CHILDREN_NESTING) {
            throw new IllegalArgumentException("Encountered too deep nesting, cannot process stream " +
                    "with more than " + MAX_CHILDREN_NESTING + " nested children. " +
                    "Some data could not be parsed. You can call setMaxChildrenNesting() to adjust " +
                    "this limit.");
        }

        // For each pointer, generate the Stream it points to
        childStreams = new Stream[childPointers.length];

        for(int i=0; i<childPointers.length; i++) {
            Pointer ptr = childPointers[i];
            childStreams[i] = Stream.createStream(ptr, documentData, chunkFactory, pointerFactory);

            // Process chunk streams into their chunks
            if(childStreams[i] instanceof ChunkStream) {
                ChunkStream child = (ChunkStream)childStreams[i];
                child.findChunks();
            }

            // Recurse into pointer containing streams
            if(childStreams[i] instanceof PointerContainingStream) {
                PointerContainingStream child =
                    (PointerContainingStream)childStreams[i];
                child.findChildren(documentData, nesting + 1);
            }
        }
    }

    public static int getMaxChildrenNesting() {
        return MAX_CHILDREN_NESTING;
    }

    public static void setMaxChildrenNesting(int maxChildrenNesting) {
        MAX_CHILDREN_NESTING = maxChildrenNesting;
    }
}
