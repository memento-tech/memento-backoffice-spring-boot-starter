import { useNavigate } from "react-router-dom";
import { useWidgets } from "../providers/WidgetContext";
import { useAlerts } from "../alert/AlertsContext";

const { OutlinedButton } = require("../components/OutlinedButton");

const WidgetButton = ({ widget, entityName, recordId }) => {
  const { showRecordWidget, handleRecordWidget, handleEntityWidget } =
    useWidgets();

  const { addAlert } = useAlerts();

  const showWidget = async () => {
    return await showRecordWidget(widget, entityName, recordId).then(
      (result) => result.show
    );
  };
  const navigate = useNavigate();

  if (showWidget())
    return (
      <OutlinedButton
        style={{ minWidth: "1rem", padding: "0 1rem", marginRight: "1rem" }}
        onClick={(event) => {
          event.preventDefault();
          if (widget.entityLevel) {
            handleEntityWidget().then((result) => {
              if (result.success) {
                navigate(0);
              } else {
                addAlert({
                  severity: "error",
                  messages: result.errorCode,
                  timeout: 2000,
                });
              }
            });
          } else if (widget.recordLevel) {
            handleRecordWidget(widget, entityName, recordId).then((result) => {
              if (result.success) {
                navigate(0);
              } else {
                addAlert({
                  severity: "error",
                  messages: result.errorCode,
                  timeout: 2000,
                });
              }
            });
          } else {
            return;
          }
        }}
      >
        {widget.label}
      </OutlinedButton>
    );
  else {
    return <></>;
  }
};

export default WidgetButton;
