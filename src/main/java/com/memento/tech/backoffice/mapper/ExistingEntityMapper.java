package com.memento.tech.backoffice.mapper;

import com.memento.tech.backoffice.entity.BaseEntity;
import com.memento.tech.backoffice.exception.BackofficeException;
import com.memento.tech.backoffice.service.EntitySettingsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;

import static com.memento.tech.backoffice.util.ObjectsUtil.getFieldValue;
import static com.memento.tech.backoffice.util.ObjectsUtil.setFieldValue;

@Service
@RequiredArgsConstructor
public class ExistingEntityMapper {

    private final EntitySettingsService entitySettingsService;

    @SuppressWarnings("unchecked")
    public BaseEntity map(BaseEntity source, BaseEntity destination) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(destination);

        if (!source.getId().equals(destination.getId())) {
            throw new BackofficeException("", "");
        }

        if (!source.getClass().equals(destination.getClass())) {
            throw new BackofficeException("", "");
        }

        var entitySettings = entitySettingsService.getEntitySettingsForEntity(source)
                .orElseThrow();

        entitySettings.getFieldSettings()
                .forEach(fieldSettings -> {
                    var sourceFieldValue = getFieldValue(source, fieldSettings);
                    var destinationFieldValue = getFieldValue(destination, fieldSettings);

                    if (Objects.nonNull(sourceFieldValue) && !sourceFieldValue.equals(destinationFieldValue)) {
                        if (sourceFieldValue instanceof Collection && destinationFieldValue instanceof Collection) {
                            var destinationCollection = mapCollectionValue((Collection<Object>) sourceFieldValue, (Collection<Object>) destinationFieldValue);
                            setFieldValue(destination, destinationCollection, fieldSettings);
                        } else {
                            setFieldValue(destination, sourceFieldValue, fieldSettings);
                        }
                    }
                });

        return destination;
    }

    private Object mapCollectionValue(Collection<Object> sourceCollection, Collection<Object> destinationCollection) {
        if (CollectionUtils.isEmpty(sourceCollection)) {
            destinationCollection.clear();
        } else if (CollectionUtils.isNotEmpty(sourceCollection)) {
            destinationCollection.clear();
            destinationCollection.addAll(sourceCollection);
        }

        return destinationCollection;
    }

}
