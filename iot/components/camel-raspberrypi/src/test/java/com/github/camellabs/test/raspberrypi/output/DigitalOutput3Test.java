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
package com.github.camellabs.test.raspberrypi.output;

import com.github.camellabs.component.raspberrypi.mock.RaspiGpioProviderMock;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Assert;
import org.junit.Test;

public class DigitalOutput3Test extends CamelTestSupport {

    public static final RaspiGpioProviderMock MOCK_RASPI = new RaspiGpioProviderMock();

    static {
        // Mandatory we are not inside a Real Raspberry PI
        GpioFactory.setDefaultProvider(MOCK_RASPI);
    }

    @Test
    public void providerDigitalOutput3Test() throws Exception {

        MockEndpoint mock = getMockEndpoint("mock:result");

        mock.expectedMessageCount(2);

        assertMockEndpointsSatisfied();

        Assert.assertEquals("", PinState.LOW, MOCK_RASPI.getState(RaspiPin.GPIO_05));

    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("timer://foo?repeatCount=2").id("rbpi-route").to("log:com.github.camellabs.component.raspberrypi?showAll=true&multiline=true")
                    .to("raspberrypi://pin:5?mode=DIGITAL_OUTPUT&state=LOW&action=TOGGLE").to("mock:result");

            }
        };
    }
}
