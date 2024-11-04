import { createContext, useContext } from "react";
import API from "../config/api";
import { HttpStatusCode } from "axios";

const WidgetContext = createContext(null);

const SHOW_WIDGET_URL = "/api/backoffice/widget";

export const WidgetProvider = ({ children }) => {
  const showRecordWidget = async (widgetData, entityName, recordId) => {
    try {
      const response = await API.get(
        getWidgetHandleMapping(
          SHOW_WIDGET_URL,
          widgetData.widgetId,
          entityName,
          recordId
        )
      );

      return {
        show: response.status === HttpStatusCode.Ok,
        errorCode: response.data?.errorCode || "Something went wrong :(",
      };
    } catch (error) {
      return {
        show: false,
        errorCode: error.data?.errorCode || "Something went wrong :(",
      };
    }
  };

  const handleRecordWidget = async (widgetData, entityName, recordId) => {
    try {
      const response = await API.get(
        getWidgetHandleMapping(
          widgetData.handleMapping,
          widgetData.widgetId,
          entityName,
          recordId
        )
      );

      return {
        success: response.status === HttpStatusCode.Ok,
        errorCode: response.data?.errorCode || "Something went wrong :(",
      };
    } catch (error) {
      return {
        success: false,
        errorCode: error.data?.errorCode || "Something went wrong :(",
      };
    }
  };

  const getWidgetHandleMapping = (mapping, widgetId, entityName, recordId) => {
    return (
      mapping +
      "?widgetId=" +
      widgetId +
      "&entityName=" +
      entityName +
      "&recordId=" +
      recordId
    );
  };

  const handleEntityWidget = async () => {
    return;
  };

  return (
    <WidgetContext.Provider
      value={{ showRecordWidget, handleRecordWidget, handleEntityWidget }}
    >
      {children}
    </WidgetContext.Provider>
  );
};

export const useWidgets = () => useContext(WidgetContext);
