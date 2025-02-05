package com.memento.tech.backoffice.service.impl;

import com.memento.tech.backoffice.dao.EntityDao;
import com.memento.tech.backoffice.dto.EntityMetadata;
import com.memento.tech.backoffice.dto.GroupMetadata;
import com.memento.tech.backoffice.dto.MetadataWrapper;
import com.memento.tech.backoffice.entity.BackofficeConfigurationMarker;
import com.memento.tech.backoffice.entity.EntitySettings;
import com.memento.tech.backoffice.populator.EntityMetadataPopuplator;
import com.memento.tech.backoffice.service.EntityMetadataService;
import com.memento.tech.backoffice.service.EntitySettingsService;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class DefaultEntityMetadataService implements EntityMetadataService {

    private final EntitySettingsService entitySettingsService;

    private final EntityDao entityDao;

    private final EntityMetadataPopuplator entityMetadataPopuplator;

    @Override
    public List<MetadataWrapper> getAllEntityMetadata() {
        return getMetadataInternal(entitySettingsService.getEntitySettings());
    }

    @Override
    public List<MetadataWrapper> getRefreshedEntityMetadata() {
        return getMetadataInternal(entitySettingsService.createEntitySettings());
    }

    private List<MetadataWrapper> getMetadataInternal(List<EntitySettings> entitySettings) {
        if (CollectionUtils.isEmpty(entitySettings)) {
            return List.of();
        }

        var result = new LinkedList<MetadataWrapper>();
        var allEntitiesGroup = new LinkedList<EntityMetadata>();

        for (EntitySettings settings : entitySettings) {
            var numberOfRecords = entityDao.getEntityRecordCount(settings.getTableName());
            var entityMetadata = entityMetadataPopuplator.populateEntityMetadata(settings, numberOfRecords);
            allEntitiesGroup.add(entityMetadata);


            if (StringUtils.isBlank(settings.getEntityGroup())) {
                if (!(settings.getEntityClass().isAssignableFrom(BackofficeConfigurationMarker.class))
                        && !settings.isExcludeEntity()) {
                    result.add(entityMetadata);
                }

                continue;
            }

            result.stream()
                    .filter(GroupMetadata.class::isInstance)
                    .map(GroupMetadata.class::cast)
                    .filter(group -> group.getGroupTitle().equals(settings.getEntityGroup()))
                    .findAny()
                    .ifPresentOrElse(groupMetadata -> {
                                groupMetadata.getGroupMetadata().add(entityMetadata);
                                if (groupMetadata.getOrder() == 0) {
                                    groupMetadata.setOrder(settings.getGroupOrder());
                                } else if (settings.getGroupOrder() != 0 && groupMetadata.getOrder() < settings.getGroupOrder()) {
                                    groupMetadata.setOrder(settings.getGroupOrder());
                                }
                            },
                            () -> {
                                var groupMetadata = new LinkedList<EntityMetadata>();
                                groupMetadata.add(entityMetadata);
                                var newGroup = GroupMetadata.builder()
                                        .isGroup(true)
                                        .groupTitle(settings.getEntityGroup())
                                        .groupMetadata(groupMetadata)
                                        .order(settings.getGroupOrder())
                                        .build();

                                result.add(newGroup);
                            });
        }

        result.add(GroupMetadata.builder()
                .isGroup(true)
                .groupTitle("configurationalOnly")
                .configurational(true)
                .groupMetadata(allEntitiesGroup)
                .order(1000)
                .build());

        //Sorting upper list that contains both entity metadata and group metadata
        result.sort(
                Comparator
                        .comparing(wrapper -> wrapper instanceof EntityMetadata entityMetadata
                                ? entityMetadata.getOrder()
                                : ((GroupMetadata) wrapper).getOrder(), Comparator.reverseOrder())
                        .thenComparing(wrapper -> wrapper instanceof EntityMetadata entityMetadata
                                ? entityMetadata.getEntityName()
                                : ((GroupMetadata) wrapper).getGroupTitle()));

        //Sorting entity metadata inside groups
        result.stream()
                .filter(GroupMetadata.class::isInstance)
                .map(GroupMetadata.class::cast)
                .forEach(group -> group.getGroupMetadata()
                        .sort(Comparator
                                .comparing(EntityMetadata::getOrder, Comparator.reverseOrder())
                                .thenComparing(EntityMetadata::getEntityName)));

        return result;
    }
}