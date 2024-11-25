package com.memento.tech.backoffice.exception;

public final class ExceptionCodeConstants {

    /*************************** EXCEPTION MESSAGES ***************************/
    public static final String INTERNAL_BACKOFFICE_ERROR = "Something went wrong, please try later.";

    public static final String UN_SUFFICIENT_DATA_ERROR = "Some data is missing.";

    public static final String ENTITY_SAVE_NON_OPTIONAL_FIELD_MISSING = "Property %s can not be empty.";

    public static final String ENTITY_SAVE_PROPERTY_NOT_UNIQUE = "Duplicate value for property %s.";

    public static final String ENTITY_SAVE_PROPERTY_CAN_NOT_BE_UPDATED = "Property %s can not be updated.";

    public static final String ENTITY_SAVE_PROPERTY_PASSWORD_IS_REQUIRED = "Please enter password.";

    public static final String ENTITY_SAVE_PROPERTY_PASSWORD_IS_SAME = "Please use different password.";

    private ExceptionCodeConstants() {
        // This class is constants class, no need for constructor
    }
}