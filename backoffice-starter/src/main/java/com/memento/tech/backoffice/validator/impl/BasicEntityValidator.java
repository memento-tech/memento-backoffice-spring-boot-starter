package com.memento.tech.backoffice.validator.impl;

import com.memento.tech.backoffice.dao.EntityDao;
import com.memento.tech.backoffice.entity.BaseEntity;
import com.memento.tech.backoffice.entity.EntityFieldSettings;
import com.memento.tech.backoffice.entity.EntitySettings;
import com.memento.tech.backoffice.exception.BackofficeException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.memento.tech.backoffice.exception.ExceptionCodeConstants.*;
import static com.memento.tech.backoffice.util.ObjectsUtil.getFieldValue;
import static org.apache.commons.collections4.SetUtils.emptyIfNull;

@Service
@AllArgsConstructor
@Slf4j
public final class BasicEntityValidator {

    private final EntityDao entityDao;

    private final DefaultEntityValidators entityValidators;

    private final PasswordEncoder backofficePasswordEncoder;

    @SuppressWarnings("unchecked")
    public void validateEntity(BaseEntity entity, EntitySettings entitySettings) {
        if (Objects.isNull(entity) || Objects.isNull(entitySettings)) {
            throw new BackofficeException("BasicEntityValidator::validateEntity : Entity and entity settings can not be null.", INTERNAL_BACKOFFICE_ERROR);
        }

        if (!entity.getClass().equals(entitySettings.getEntityClass())) {
            throw new BackofficeException("BasicEntityValidator::validateEntity : Entity and entity settings are bad match.", INTERNAL_BACKOFFICE_ERROR);
        }

        validateEntityInternal(entity, null, entitySettings);

        emptyIfNull(entityValidators.getValidatorsForEntity(entitySettings.getEntityClass()))
                .forEach(validator -> validator.validate(entity));
    }

    @SuppressWarnings("unchecked")
    public void validateWithExistingEntity(BaseEntity entity, BaseEntity existingEntity, EntitySettings entitySettings) {
        if (Objects.isNull(entity) || Objects.isNull(entitySettings) || Objects.isNull(existingEntity)) {
            throw new BackofficeException("BasicEntityValidator::validateEntity : Entity data, entity settings and existing entity can not be null.", INTERNAL_BACKOFFICE_ERROR);
        }

        if (!entity.getClass().equals(entitySettings.getEntityClass())) {
            throw new BackofficeException("BasicEntityValidator::validateEntity : Entity and entity settings are bad match.", INTERNAL_BACKOFFICE_ERROR);
        }

        validateEntityInternal(entity, existingEntity, entitySettings);

        emptyIfNull(entityValidators.getValidatorsForEntity(entitySettings.getEntityClass()))
                .forEach(validator -> {
                    validator.validate(entity);
                    validator.validateWithExisting(entity, existingEntity);
                });
    }

    private void validateEntityInternal(BaseEntity entity, BaseEntity persistedEntity, EntitySettings entitySettings) {
        entitySettings.getFieldSettings()
                .forEach(fieldSettings -> {
                    validateNonOptionalField(entity, fieldSettings);
                    validateUniqueField(entity, entitySettings.getEntityName(), fieldSettings);

                    if (Objects.nonNull(persistedEntity)) {
                        validateNonUpdatableField(entity, persistedEntity, fieldSettings);
                    }

                    if (fieldSettings.isPassword()) {
                        validatePasswordChange(entity, persistedEntity, fieldSettings);
                    }
                });
    }

    private void validateNonOptionalField(BaseEntity entity, EntityFieldSettings fieldSettings) {
        var value = getFieldValue(entity, fieldSettings);

        if (fieldSettings.isRequired()
                && (Objects.isNull(value)
                || !fieldSettings.isPassword() && fieldSettings.getFieldClass().equals(String.class) && StringUtils.isBlank((String) value))) {
            throw new BackofficeException(
                    "BasicEntityValidator::validateNonOptionalField : Entity value for field " + fieldSettings.getField() + " is missing.",
                    String.format(ENTITY_SAVE_NON_OPTIONAL_FIELD_MISSING, fieldSettings.getField()));
        }
    }

    private void validatePasswordChange(BaseEntity entity, BaseEntity persistentEntity, EntityFieldSettings fieldSettings) {
        var newPassword = (String) getFieldValue(entity, fieldSettings);
        var persistedPassword = (String) getFieldValue(persistentEntity, fieldSettings);
        var passwordChanged = StringUtils.isNoneBlank(newPassword);

        if (Objects.isNull(persistentEntity) && StringUtils.isBlank(newPassword)) {
            throw new BackofficeException(
                    "BasicEntityValidator::validateNonOptionalField : Password can not be empty for new entities.",
                    ENTITY_SAVE_PROPERTY_PASSWORD_IS_REQUIRED);
        }

        if (passwordChanged && Objects.nonNull(persistentEntity) && StringUtils.isNotBlank(newPassword)) {

            if (backofficePasswordEncoder.matches(newPassword, persistedPassword)) {
                throw new BackofficeException(
                        "BasicEntityValidator::validateNonOptionalField : Password can not be the same.",
                        ENTITY_SAVE_PROPERTY_PASSWORD_IS_SAME);
            }
        }

        if (!passwordChanged) {
            try {
                var passwordField = entity.getClass().getDeclaredField(fieldSettings.getField());
                passwordField.setAccessible(true);
                passwordField.set(entity, persistedPassword);
            } catch (IllegalAccessException | NoSuchFieldException exception) {
                log.error("Something went wrong, could not find password field, will not allow user update!", exception);
                throw new BackofficeException("BasicEntityValidator::validatePasswordChange : Password field not found.", INTERNAL_BACKOFFICE_ERROR);
            }
        }
    }

    private void validateUniqueField(BaseEntity entity, String entityName, EntityFieldSettings fieldSettings) {
        if (fieldSettings.isUniqueField()) {
            var fieldValue = getFieldValue(entity, fieldSettings);

            Object value;
            if (fieldSettings.getFieldClass().equals(Integer.class)) {
                value = String.valueOf(fieldValue);
            } else {
                value = fieldValue;
            }

            entityDao.getEntityIdsForProperty(entityName, fieldSettings.getField(), value)
                    .stream()
                    .filter(foundEntityId -> !foundEntityId.equals(entity.getId()))
                    .findAny()
                    .ifPresent(persistedEntity -> {
                        throw new BackofficeException(
                                "BasicEntityValidator::validateUniqueField : Entity not unique for field " + fieldSettings.getField() + " with value " + fieldValue + ".",
                                String.format(ENTITY_SAVE_PROPERTY_NOT_UNIQUE, fieldSettings.getField()));
                    });
        }
    }

    private void validateNonUpdatableField(Object entity, Object persistedEntity, EntityFieldSettings fieldSettings) {
        if (BooleanUtils.isFalse(fieldSettings.isUpdatable()) && !fieldSettings.getField().equals("updatedAt")) {
            var fieldValue = getFieldValue(entity, fieldSettings);
            var persistedFieldValue = getFieldValue(persistedEntity, fieldSettings);

            if (Objects.nonNull(fieldValue) && !fieldValue.equals(persistedFieldValue)) {
                throw new BackofficeException(
                        "BasicEntityValidator::validateNonUpdatableField : Property " + fieldSettings.getField() + " is not updatable.",
                        String.format(ENTITY_SAVE_PROPERTY_CAN_NOT_BE_UPDATED, fieldSettings.getField()));
            }

        }
    }
}