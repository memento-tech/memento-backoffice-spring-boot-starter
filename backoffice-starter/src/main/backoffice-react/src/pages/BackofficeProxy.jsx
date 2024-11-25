import { Provider } from "react-redux";
import PopupProvider from "../popup/PopupContext";
import BackofficePage from "./BackofficePage";
import AlertsProvider from "../alert/AlertsContext";
import { backofficeStore } from "../redux/store/backofficeStore";

const BackofficeProxy = () => {
  return (
    <Provider store={backofficeStore}>
      <PopupProvider>
        <AlertsProvider>
          <BackofficePage />
        </AlertsProvider>
      </PopupProvider>
    </Provider>
  );
};

export default BackofficeProxy;
