/**
 * Licensed to the Camel Labs under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.camellabs.iot.gateway.heartbeat

import com.github.camellabs.iot.gateway.Gateway
import org.junit.Assert
import org.junit.Test

import java.util.concurrent.CountDownLatch

class HeartbeatVerticleTest extends Assert {

    def gateway = new Gateway().start()

    @Test
    void shouldReceiveHeartbeatFromEventBus() {
        // Given
        def assertHeartbeatReceived = new CountDownLatch(1)

        gateway.vertx.eventBus().consumer('heartbeat') {
            // When
            assertHeartbeatReceived.countDown()
        }

        // Then
        assertHeartbeatReceived.await()
    }

}
