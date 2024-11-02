package com.memento.tech.backoffice.validator.impl;

import com.memento.tech.backoffice.entity.BaseEntity;
import com.memento.tech.backoffice.exception.BackofficeException;
import com.memento.tech.backoffice.validator.EntityValidator;
import com.memento.tech.backoffice.validator.EntityValidators;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.memento.tech.backoffice.exception.ExceptionCodeConstants.INTERNAL_BACKOFFICE_ERROR;

@Service
@SuppressWarnings("rawtypes")
public final class DefaultEntityValidators implements EntityValidators {

    private final Map<Class, Set<EntityValidator>> validators = new HashMap<>();

    @Override
    public void registerValidator(EntityValidator<? extends BaseEntity> validator, Class<? extends BaseEntity> entityClass) {
        if (Objects.isNull(validator) || Objects.isNull(entityClass)) {
            throw new BackofficeException("DefaultEntityValidators::registerValidator : Validator and entity class can not be null.", INTERNAL_BACKOFFICE_ERROR);
        }

        //check if validator is compatible with entity class

        var entityValidators = validators.get(entityClass);
        if (CollectionUtils.isEmpty(entityValidators)) {
            var newEntityValidatorsList = new HashSet<EntityValidator>();
            newEntityValidatorsList.add(validator);

            validators.put(entityClass, newEntityValidatorsList);
        } else {
            entityValidators.add(validator);
            validators.put(entityClass, entityValidators);
        }
    }

    Set<EntityValidator> getValidatorsForEntity(Class entityClass) {
        if (Objects.isNull(entityClass)) {
            throw new BackofficeException("DefaultEntityValidators::getValidatorsForEntity : Entity class can not be null.", INTERNAL_BACKOFFICE_ERROR);
        }

        return validators.getOrDefault(entityClass, SetUtils.emptySet());
    }

}