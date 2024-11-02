package com.memento.tech.backoffice.service.impl;

import com.memento.tech.backoffice.entity.EntitySettings;
import com.memento.tech.backoffice.entity.Media;
import com.memento.tech.backoffice.entity.Translation;
import com.memento.tech.backoffice.handler.EntityAnnotationHandler;
import com.memento.tech.backoffice.repository.EntitySettingsRepository;
import com.memento.tech.backoffice.repository.WidgetRepository;
import com.memento.tech.backoffice.service.EntityCreationService;
import com.memento.tech.backoffice.service.EntitySettingsService;
import com.memento.tech.backoffice.service.FieldSettingsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.EntityType;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@AllArgsConstructor
public class DefaultEntitySettingsService implements EntitySettingsService {

    private final EntityManager entityManager;

    private final EntitySettingsRepository entitySettingsRepository;

    private final FieldSettingsService fieldSettingsService;

    private final EntityCreationService entityCreationService;

    private final EntityAnnotationHandler entityAnnotationHandler;

    private final WidgetRepository widgetRepository;

    @Override
    public List<EntitySettings> getEntitySettings() {
        var result = entitySettingsRepository.findAll();

        result.forEach(entitySettings -> entitySettings.setFieldSettings(fieldSettingsService.orderFields(entitySettings.getFieldSettings())));

        return sortEntitySettings(result);
    }

    @Override
    public List<EntitySettings> createEntitySettings() {
        entitySettingsRepository.deleteAll();
        widgetRepository.deleteAll();

        final var entities = getEntities();


        var result = entities.stream()
                .map(this::saveEntitySettingsInternal)
                .toList();

        return sortEntitySettings(result);
    }

    @Override
    public Optional<EntitySettings> getEntitySettingsForName(String entityName) {
        if (isBlank(entityName)) {
            return Optional.empty();
        }

        return entitySettingsRepository.findByEntityName(entityName);
    }

    @Override
    public Optional<EntitySettings> getEntitySettingsForEntity(Object entity) {
        return CollectionUtils.emptyIfNull(this.getEntitySettings())
                .stream()
                .filter(entitySettings -> entitySettings.getEntityClass().equals(entity.getClass()))
                .findFirst();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private EntitySettings saveEntitySettingsInternal(EntityType entityType) {
        final var entitySettings = new EntitySettings();

        entitySettings.setEntityName(entityType.getName());
        entitySettings.setTableName(getEntityTableName(entityType));
        entitySettings.setEntityClass(entityType.getJavaType());
        entitySettings.setTranslation(entityType.getJavaType().equals(Translation.class));
        entitySettings.setMedia(entityType.getJavaType().equals(Media.class));

        entityAnnotationHandler.handleEntityAnnotation(entitySettings, entityType.getJavaType());

        entitySettings.setFieldSettings(fieldSettingsService.createEntityFields(entityType));
        entitySettings.setCreationSettings(entityCreationService.initCreationSettings(entitySettings, entitySettings.isDisableCreation()));

        entitySettingsRepository.save(entitySettings);

        return entitySettings;
    }

    private Set<EntityType<?>> getEntities() {
        return entityManager.getMetamodel().getEntities();
    }

    private String getEntityTableName(EntityType<?> entityType) {
        return Optional.of(entityType)
                .map(EntityType::getJavaType)
                .map(clazz -> clazz.getAnnotation(Table.class))
                .map(Table::name)
                .filter(StringUtils::isNotBlank)
                .orElseGet(() -> defineTableNameFromEntityName(entityType.getName()));
    }

    private String defineTableNameFromEntityName(String entityName) {
        return entityName
                .chars()
                .mapToObj(i -> (char) i)
                .collect(joinOnCapital())
                .toLowerCase();
    }

    private Collector<Character, StringBuilder, String> joinOnCapital() {
        return Collector.of(
                StringBuilder::new,
                (sb, c) -> {
                    if (Character.isUpperCase(c)) {
                        if (!sb.isEmpty() && !Character.isUpperCase(sb.charAt(sb.length() - 1))) {
                            sb.append('_');
                        }
                    }
                    sb.append(c);
                },
                StringBuilder::append,
                StringBuilder::toString
        );
    }

    private List<EntitySettings> sortEntitySettings(List<EntitySettings> unsortedEntitySettings) {
        return emptyIfNull(unsortedEntitySettings)
                .stream()
                .sorted()
                .sorted((first, second) -> Integer.compare(second.getEntityOrder(), first.getEntityOrder()))
                .toList();
    }
}