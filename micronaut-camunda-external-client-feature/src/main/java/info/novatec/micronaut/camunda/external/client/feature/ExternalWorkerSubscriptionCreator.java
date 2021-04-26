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
    protected final TopicConfigurationMapper topicConfigurationMapper;

    public ExternalWorkerSubscriptionCreator(BeanContext beanContext,
                                             ExternalTaskClient externalTaskClient,
                                             TopicConfigurationMapper topicConfigurationMapper) {
        this.beanContext = beanContext;
        this.externalTaskClient = externalTaskClient;
        this.topicConfigurationMapper = topicConfigurationMapper;

        beanContext.getBeanDefinitions(ExternalTaskHandler.class).forEach(this::registerExternalTaskHandler);
    }

    protected void registerExternalTaskHandler(BeanDefinition<ExternalTaskHandler> beanDefinition) {
        ExternalTaskHandler externalTaskHandler = beanContext.getBean(beanDefinition);
        AnnotationValue<ExternalTaskSubscription> annotationValue = beanDefinition.getAnnotation(ExternalTaskSubscription.class);

        if (annotationValue == null) {
            log.warn("Skipping subscription. Could not find Annotation ExternalTaskSubscription on class {}", beanDefinition.getName());
        } else {
            TopicConfiguration topicConfiguration = this.topicConfigurationMapper.mapToTopicConfiguration(annotationValue);

            String topicName = topicConfiguration.getTopicName();

            if (beanContext.containsBean(TopicConfigurationProperty.class, Qualifiers.byName(topicName))) {
                TopicConfigurationProperty topicConfigurationProperties = beanContext.getBean(TopicConfigurationProperty.class, Qualifiers.byName(topicName));
                topicConfiguration.overrideIfExists(this.topicConfigurationMapper.mapToTopicConfiguration(topicConfigurationProperties));
            }

            buildTopicSubscription(externalTaskHandler, topicConfiguration);
        }
    }

    protected void buildTopicSubscription(ExternalTaskHandler externalTaskHandler, TopicConfiguration topicConfiguration) {
        //noinspection OptionalGetWithoutIsPresent
        TopicSubscriptionBuilder builder = externalTaskClient.subscribe(topicConfiguration.getTopicName());

        builder.handler(externalTaskHandler);

        builder.lockDuration(topicConfiguration.getLockDuration());

        builder.variables(topicConfiguration.getVariables());

        builder.localVariables(topicConfiguration.isLocalVariables());

        builder.businessKey(topicConfiguration.getBusinessKey());

        builder.processDefinitionId((topicConfiguration.getProcessDefinitionId()));

        builder.processDefinitionIdIn(topicConfiguration.getProcessDefinitionIdIn());

        builder.processDefinitionKey(topicConfiguration.getProcessDefinitionKey());

        builder.processDefinitionKeyIn(topicConfiguration.getProcessDefinitionKeyIn());

        builder.processDefinitionVersionTag(topicConfiguration.getProcessDefinitionVersionTag());

        if (topicConfiguration.isWithoutTenantId()) {
            builder.withoutTenantId();
        }

        builder.tenantIdIn(topicConfiguration.getTenantIdIn());

        builder.includeExtensionProperties(topicConfiguration.hasIncludeExtensionProperties());

        builder.open();
        log.info("External task client subscribed to topic '{}'", topicConfiguration.getTopicName());
    }
}
