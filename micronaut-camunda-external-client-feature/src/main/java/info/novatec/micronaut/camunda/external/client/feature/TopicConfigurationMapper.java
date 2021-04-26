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

import io.micronaut.core.annotation.AnnotationValue;

public class TopicConfigurationMapper {

    protected final TopicConfigurationMapper topicConfigurationMapper;

    public TopicConfigurationMapper(TopicConfigurationMapper topicConfigurationMapper) {
        this.topicConfigurationMapper = topicConfigurationMapper;
    }

    public TopicConfiguration mapToTopicConfiguration(TopicConfigurationProperty topicConfigurationProperty) {
        TopicConfiguration topicConfiguration = new TopicConfiguration();
        topicConfiguration.setTopicName(topicConfigurationProperty.getTopicName());
        topicConfiguration.setLockDuration(topicConfigurationProperty.getLockDuration());
        topicConfiguration.setVariables(topicConfigurationProperty.getVariables());
        topicConfiguration.setBusinessKey(topicConfigurationProperty.getBusinessKey());
        topicConfiguration.setProcessDefinitionId(topicConfigurationProperty.getProcessDefinitionId());
        topicConfiguration.setProcessDefinitionIdIn(topicConfigurationProperty.getProcessDefinitionIdIn());
        topicConfiguration.setProcessDefinitionKey(topicConfigurationProperty.getProcessDefinitionKey());
        topicConfiguration.setProcessDefinitionKeyIn(topicConfigurationProperty.getProcessDefinitionKeyIn());
        topicConfiguration.setProcessDefinitionVersionTag(topicConfigurationProperty.getProcessDefinitionVersionTag());
        topicConfiguration.setWithoutTenantId(topicConfigurationProperty.isWithoutTenantId());
        topicConfiguration.setTenantIdIn(topicConfigurationProperty.getTenantIdIn());
        topicConfiguration.setIncludeExtensionProperties(topicConfigurationProperty.hasIncludeExtensionProperties());
        return topicConfiguration;
    }

    public TopicConfiguration mapToTopicConfiguration(AnnotationValue<ExternalTaskSubscription> annotationValue) {
        TopicConfiguration topicConfiguration = new TopicConfiguration();

        annotationValue.stringValue("topicName").ifPresent(topicConfiguration::setTopicName);

        annotationValue.longValue("lockDuration").ifPresent(topicConfiguration::setLockDuration);

        annotationValue.get("variables", String[].class).ifPresent(it -> {
            if(!it[0].equals("")){
                topicConfiguration.setVariables(it);
            }
        });

        annotationValue.booleanValue("localVariables").ifPresent(topicConfiguration::setLocalVariables);

        annotationValue.stringValue("businessKey").ifPresent(topicConfiguration::setBusinessKey);

        annotationValue.stringValue("processDefinitionId").ifPresent(topicConfiguration::setProcessDefinitionId);

        annotationValue.get("processDefinitionIdIn", String[].class).ifPresent(it -> {
            if(!it[0].equals("")){
                topicConfiguration.setProcessDefinitionIdIn(it);
            }
        });

        annotationValue.stringValue("processDefinitionKey").ifPresent(topicConfiguration::setProcessDefinitionKey);

        annotationValue.get("processDefinitionKeyIn", String[].class).ifPresent(it -> {
            if(!it[0].equals("")) {
                topicConfiguration.setProcessDefinitionKeyIn(it);
            }
        });

        annotationValue.stringValue("processDefinitionVersionTag").ifPresent(topicConfiguration::setProcessDefinitionVersionTag);

        annotationValue.booleanValue("withoutTenantId").ifPresent(it -> {
            if (it) {
                topicConfiguration.setWithoutTenantId(true);
            }
        });

        annotationValue.get("tenantIdIn", String[].class).ifPresent(it -> {
            if (!it[0].equals("")) {
                topicConfiguration.setTenantIdIn(it);
            }
        });

        annotationValue.booleanValue("includeExtensionProperties").ifPresent(topicConfiguration::setIncludeExtensionProperties);

        return topicConfiguration;
    }
}
