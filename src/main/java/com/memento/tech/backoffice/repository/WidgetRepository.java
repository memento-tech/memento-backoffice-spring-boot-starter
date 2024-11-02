package com.memento.tech.backoffice.repository;

import com.memento.tech.backoffice.entity.Widget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WidgetRepository extends JpaRepository<Widget, String> {

    Optional<Widget> findByWidgetId(String widgetId);
}
