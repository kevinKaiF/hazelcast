/*
 * Copyright (c) 2008-2017, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.internal.nearcache.impl.invalidation;

import com.hazelcast.internal.nearcache.NearCacheRecord;

/**
 * Used to detect staleness of near-cached data.
 *
 * During near-cache get, if one or more invalidations are lost for a key, we will make near-cached-data
 * unreachable with the help of {@link StaleReadDetector} and next near-cache-get will return null to force fresh
 * data fetching from underlying imap/icache.
 *
 * @see com.hazelcast.internal.nearcache.impl.store.AbstractNearCacheRecordStore#get
 */
public interface StaleReadDetector {

    /**
     * This instance will be used when near-cache invalidations are disabled.
     * It behaves as if there is no stale data and everything is fresh.
     */
    StaleReadDetector ALWAYS_FRESH = new StaleReadDetector() {
        @Override
        public boolean isStaleRead(Object key, NearCacheRecord record) {
            return false;
        }

        @Override
        public MetaDataContainer getMetaDataContainer(Object key) {
            return null;
        }
    };

    /**
     * @param key    the key
     * @param record the near-cache record
     * @return {@code true} if reading with the supplied invalidation metadata is stale,
     * otherwise returns {@code false}
     */
    boolean isStaleRead(Object key, NearCacheRecord record);

    /**
     * @param key supplied key to get value
     * @return {@link MetaDataContainer} for this key
     */
    MetaDataContainer getMetaDataContainer(Object key);
}