/*
 * Copyright 2021 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.novatec.micronaut.camunda.external.client.feature;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Context;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.inject.BeanDefinition;
import io.micronaut.inject.qualifiers.Qualifiers;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.topic.TopicSubscriptionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Sawilla
 *
 * Allows to configure an external task worker with the {@link ExternalTaskSubscription} annotation. This allows to easily build
 * external workers for multiple topics.
 */
@Context
public class ExternalWorkerSubscriptionCreator {

    private static final Logger log = LoggerFactory.getLogger(ExternalWorkerSubscriptionCreator.class);

    protected final BeanContext beanContext;
    protected final ExternalTaskClient externalTaskClient;

    public ExternalWorkerSubscriptionCreator(BeanContext beanContext,
                                             ExternalTaskClient externalTaskClient) {
        this.beanContext = beanContext;
        this.externalTaskClient = externalTaskClient;

        beanContext.getBeanDefinitions(ExternalTaskHandler.class).forEach(this::registerExternalTaskHandler);
    }

    protected void registerExternalTaskHandler(BeanDefinition<ExternalTaskHandler> beanDefinition) {
        ExternalTaskHandler externalTaskHandler = beanContext.getBean(beanDefinition);
        AnnotationValue<ExternalTaskSubscription> annotationValue = beanDefinition.getAnnotation(ExternalTaskSubscription.class);

        if (annotationValue == null) {
            log.warn("Skipping subscription. Could not find Annotation ExternalTaskSubscription on class {}", beanDefinition.getName());
        } else {
            TopicSubscriptionBuilder builder = buildTopicSubscription(externalTaskHandler, externalTaskClient, annotationValue);

            //noinspection OptionalGetWithoutIsPresent
            String topicName = annotationValue.stringValue("topicName").get();

            if (beanContext.containsBean(TopicConfigurationProperty.class, Qualifiers.byName(topicName))) {
                TopicConfigurationProperty topicConfigurationProperties = beanContext.getBean(TopicConfigurationProperty.class, Qualifiers.byName(topicName));
                overrideAnnotationWithConfigurationProperties(topicConfigurationProperties, builder);
            }

            builder.open();
            log.info("External task client subscribed to topic '{}'", topicName);
        }
    }

    protected TopicSubscriptionBuilder buildTopicSubscription(ExternalTaskHandler externalTaskHandler, ExternalTaskClient client, AnnotationValue<ExternalTaskSubscription> annotationValue) {
        //noinspection OptionalGetWithoutIsPresent
        TopicSubscriptionBuilder builder = client.subscribe(annotationValue.stringValue("topicName").get());

        builder.handler(externalTaskHandler);

        annotationValue.longValue("lockDuration").ifPresent(builder::lockDuration);

        annotationValue.get("variables", String[].class).ifPresent(it -> {
            if (!it[0].equals("")) {
                builder.variables(it);
            }
        });

        annotationValue.booleanValue("localVariables").ifPresent(builder::localVariables);

        annotationValue.stringValue("businessKey").ifPresent(builder::businessKey);

        annotationValue.stringValue("processDefinitionId").ifPresent(builder::processDefinitionId);

        annotationValue.get("processDefinitionIdIn", String[].class).ifPresent(it -> {
            if (!it[0].equals("")) {
                builder.processDefinitionIdIn(it);
            }
        });

        annotationValue.stringValue("processDefinitionKey").ifPresent(builder::processDefinitionKey);

        annotationValue.get("processDefinitionKeyIn", String[].class).ifPresent(it -> {
            if (!it[0].equals("")) {
                builder.processDefinitionKeyIn(it);
            }
        });

        annotationValue.stringValue("processDefinitionVersionTag").ifPresent(builder::processDefinitionVersionTag);

        annotationValue.booleanValue("withoutTenantId").ifPresent(it -> {
            if (it) {
                builder.withoutTenantId();
            }
        });

        annotationValue.get("tenantIdIn", String[].class).ifPresent(it -> {
            if (!it[0].equals("")) {
                builder.tenantIdIn(it);
            }
        });

        annotationValue.booleanValue("includeExtensionProperties").ifPresent(builder::includeExtensionProperties);

        return builder;
    }

    protected void overrideAnnotationWithConfigurationProperties(TopicConfigurationProperty topicConfiguration, TopicSubscriptionBuilder builder) {
        log.info("External configuration for topic {} found. Overriding annotation values", topicConfiguration.getTopicName());

        if (topicConfiguration.getLockDuration() != null) {
            builder.lockDuration(topicConfiguration.getLockDuration());
        }

        if (topicConfiguration.getVariables() != null) {
            builder.variables(topicConfiguration.getVariables());
        }

        if (topicConfiguration.getVariables() != null) {
            builder.variables(topicConfiguration.getVariables());
        }

        if (topicConfiguration.getLocalVariables() != null) {
            builder.localVariables(topicConfiguration.getLocalVariables());
        }

        if (topicConfiguration.getBusinessKey() != null) {
            builder.businessKey((topicConfiguration.getBusinessKey()));
        }

        if (topicConfiguration.getProcessDefinitionId() != null) {
            builder.processDefinitionId(topicConfiguration.getProcessDefinitionId());
        }

        if (topicConfiguration.getProcessDefinitionIdIn() != null) {
            builder.processDefinitionIdIn(topicConfiguration.getProcessDefinitionIdIn());
        }

        if (topicConfiguration.getProcessDefinitionKey() != null) {
            builder.processDefinitionKey(topicConfiguration.getProcessDefinitionKey());
        }

        if (topicConfiguration.getProcessDefinitionKeyIn() != null) {
            builder.processDefinitionKeyIn(topicConfiguration.getProcessDefinitionKeyIn());
        }

        if (topicConfiguration.getProcessDefinitionVersionTag() != null) {
            builder.processDefinitionVersionTag(topicConfiguration.getProcessDefinitionVersionTag());
        }

        if (topicConfiguration.getWithoutTenantId() != null && topicConfiguration.getWithoutTenantId()) {
            builder.withoutTenantId();
        }

        if (topicConfiguration.getTenantIdIn() != null) {
            builder.tenantIdIn(topicConfiguration.getTenantIdIn());
        }

        if (topicConfiguration.getIncludeExtensionProperties() != null && topicConfiguration.getIncludeExtensionProperties()) {
            builder.includeExtensionProperties(topicConfiguration.getIncludeExtensionProperties());
        }
    }
}
