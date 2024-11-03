import { useNavigate } from "react-router-dom";
import { useWidgets } from "../providers/WidgetContext";

const { OutlinedButton } = require("../components/OutlinedButton");

const WidgetButton = ({ widget, entityName, recordId }) => {
  const { showRecordWidget, handleRecordWidget, handleEntityWidget } =
    useWidgets();

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
            handleEntityWidget();
          } else if (widget.recordLevel) {
            handleRecordWidget(widget, entityName, recordId);
          } else {
            return;
          }

          // navigate(0);
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
