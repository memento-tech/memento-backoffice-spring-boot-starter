package com.memento.tech.backoffice.repository;

import com.memento.tech.backoffice.entity.Widget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WidgetRepository extends JpaRepository<Widget, String> {

    Optional<Widget> findByWidgetId(String widgetId);
}
