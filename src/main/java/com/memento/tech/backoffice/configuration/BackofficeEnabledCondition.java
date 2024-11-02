package com.memento.tech.backoffice.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class BackofficeEnabledCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String propertyValue = context.getEnvironment().getProperty("memento.tech.backoffice.enabled");
        boolean isEnabled = Boolean.parseBoolean(propertyValue);

        boolean externalApiStatus = checkExternalApiStatus();

        if (isEnabled && externalApiStatus) {
            return ConditionOutcome.match();
        } else {
            return ConditionOutcome.noMatch("Backoffice is disabled: " +
                    "memento.backoffice.enabled = " + isEnabled + ", " +
                    "External API status = " + externalApiStatus);
        }
    }

    private boolean checkExternalApiStatus() {
        return true;
    }
}
