/**
 * Licensed to the Rhiot under one or more
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
package io.rhiot.datastream.document

import io.rhiot.datastream.engine.AbstractStreamConsumer
import io.vertx.core.eventbus.Message
import io.vertx.core.json.Json

import static io.rhiot.datastream.document.Pojos.collectionName
import static io.rhiot.datastream.document.Pojos.pojoToMap

/**
 * Consumes a stream of document-related messages.
 */
class DocumentStreamConsumer extends AbstractStreamConsumer {

    private DocumentStore documentStore

    @Override
    String fromChannel() {
        'document'
    }

    @Override
    void start() {
        log().debug('Starting document stream consumer.')
        def storeFromRegistry = bootstrap.beanRegistry().bean(DocumentStore.class)
        if(!storeFromRegistry.isPresent()) {
            throw new IllegalStateException("Can't find ${DocumentStore.class.name} in a Rhiot Bootstrap bean registry.")
        }
        documentStore = storeFromRegistry.get()
    }

    @Override
    void stop() {
        documentStore = null
    }

    @Override
    void consume(Message message) {
        switch (message.headers().get('operation')) {
            case 'save':
                def collection = (String) message.headers().get('collection')
                def document = Json.decodeValue((String) message.body(), Map.class)
                def id = documentStore.save(collectionName(collection), pojoToMap(document))
                message.reply(Json.encode([id: id]))
                break
            case 'count':
                def collection = (String) message.headers().get('collection')
                def count = documentStore.count(collection)
                message.reply(Json.encode([count: count]))
                break
        }
    }

}