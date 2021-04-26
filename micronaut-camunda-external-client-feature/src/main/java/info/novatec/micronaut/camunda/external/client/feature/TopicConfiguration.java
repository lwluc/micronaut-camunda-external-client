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

import java.util.Optional;

public class TopicConfiguration {
    private String topicName;
    private Long lockDuration;
    private String[] variables;
    private Boolean localVariables;
    private String businessKey;
    private String processDefinitionId;
    private String[] processDefinitionIdIn;
    private String processDefinitionKey;
    private String[] processDefinitionKeyIn;
    private String processDefinitionVersionTag;
    private Boolean withoutTenantId;
    private String[] tenantIdIn;
    private Boolean includeExtensionProperties;

    public TopicConfiguration() {
    }

    public void overrideIfExists(TopicConfiguration overridingConfig) {
        this.topicName = overridingConfig.getTopicName() == null ? this.topicName : overridingConfig.getTopicName();
        this.lockDuration = overridingConfig.getLockDuration() == null ? this.lockDuration : overridingConfig.getLockDuration();
        this.variables = overridingConfig.getVariables() == null ? this.variables : overridingConfig.getVariables();
        this.localVariables = !overridingConfig.isLocalVariables() ? this.localVariables : overridingConfig.isLocalVariables();
        this.businessKey = overridingConfig.getBusinessKey() == null ? this.businessKey : overridingConfig.getBusinessKey();
        this.processDefinitionId = overridingConfig.getProcessDefinitionId() == null ? this.processDefinitionId : overridingConfig.getProcessDefinitionId();
        this.processDefinitionIdIn = overridingConfig.getProcessDefinitionIdIn() == null ? this.processDefinitionIdIn : overridingConfig.getProcessDefinitionIdIn();
        this.processDefinitionKey = overridingConfig.getProcessDefinitionId() == null ? this.processDefinitionId : overridingConfig.getProcessDefinitionId();
        this.processDefinitionKeyIn = overridingConfig.getProcessDefinitionKeyIn() == null ? this.processDefinitionKeyIn : overridingConfig.getProcessDefinitionKeyIn();
        this.processDefinitionVersionTag = overridingConfig.getProcessDefinitionVersionTag() == null ? this.processDefinitionVersionTag : overridingConfig.getProcessDefinitionVersionTag();
        this.withoutTenantId = !overridingConfig.isWithoutTenantId() ? this.withoutTenantId : overridingConfig.isWithoutTenantId();
        this.tenantIdIn = overridingConfig.getTenantIdIn() == null ? this.tenantIdIn : overridingConfig.getTenantIdIn();
        this.includeExtensionProperties = !overridingConfig.hasIncludeExtensionProperties() ? this.includeExtensionProperties : overridingConfig.hasIncludeExtensionProperties();
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Long getLockDuration() {
        return lockDuration;
    }

    public void setLockDuration(Long lockDuration) {
        this.lockDuration = lockDuration;
    }

    public String[] getVariables() {
        return variables;
    }

    public void setVariables(String[] variables) {
        this.variables = variables;
    }

    public boolean isLocalVariables() {
        return Optional.ofNullable(localVariables).orElse(false);
    }

    public void setLocalVariables(boolean localVariables) {
        this.localVariables = localVariables;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String[] getProcessDefinitionIdIn() {
        return processDefinitionIdIn;
    }

    public void setProcessDefinitionIdIn(String[] processDefinitionIdIn) {
        this.processDefinitionIdIn = processDefinitionIdIn;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String[] getProcessDefinitionKeyIn() {
        return processDefinitionKeyIn;
    }

    public void setProcessDefinitionKeyIn(String[] processDefinitionKeyIn) {
        this.processDefinitionKeyIn = processDefinitionKeyIn;
    }

    public String getProcessDefinitionVersionTag() {
        return processDefinitionVersionTag;
    }

    public void setProcessDefinitionVersionTag(String processDefinitionVersionTag) {
        this.processDefinitionVersionTag = processDefinitionVersionTag;
    }

    public boolean isWithoutTenantId() {
        return Optional.ofNullable(withoutTenantId).orElse(false);
    }

    public void setWithoutTenantId(boolean withoutTenantId) {
        this.withoutTenantId = withoutTenantId;
    }

    public String[] getTenantIdIn() {
        return tenantIdIn;
    }

    public void setTenantIdIn(String[] tenantIdIn) {
        this.tenantIdIn = tenantIdIn;
    }

    public boolean hasIncludeExtensionProperties() {
        return Optional.ofNullable(includeExtensionProperties).orElse(false);
    }

    public void setIncludeExtensionProperties(boolean includeExtensionProperties) {
        this.includeExtensionProperties = includeExtensionProperties;
    }
}
