import { createContext, useContext, useRef, useState } from "react";
import Alert from "./Alert";

const AlertsContext = createContext(null);

const AlertsProvider = ({ children }) => {
  const [alerts, setAlerts] = useState([]);

  const addAlert = (alert) => {
    const id =
      Math.random().toString(36).slice(2, 9) +
      new Date().getTime().toString(36);
    setAlerts((prev) => [{ ...alert, id: id }, ...prev]);
    return id;
  };

  const dismissAlert = (id) => {
    setAlerts((prev) => prev.filter((alert) => alert.id !== id));
  };

  return (
    <AlertsContext.Provider value={{ alerts, addAlert, dismissAlert }}>
      {alerts.map((alert, index) => (
        <Alert
          key={alert.id}
          order={alerts.length - index}
          {...alert}
          handleDismiss={() => {
            dismissAlert(alert.id);
          }}
        />
      ))}
      {children}
    </AlertsContext.Provider>
  );
};

export const useAlerts = () => {
  const [alertIds, setAlertIds] = useState([]);
  const alertIdsRef = useRef(alertIds);
  const { addAlert, dismissAlert } = useContext(AlertsContext);

  const addAlertWithId = (alert) => {
    const id = addAlert(alert);
    alertIdsRef.current.push(id);
    setAlertIds(alertIdsRef.current);
  };

  const clearAlerts = () => {
    alertIdsRef.current.forEach((id) => dismissAlert(id));
    alertIdsRef.current = [];
    setAlertIds([]);
  };
  return { addAlert: addAlertWithId, clearAlerts };
};

export default AlertsProvider;
