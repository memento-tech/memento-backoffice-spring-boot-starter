import { Suspense } from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import { backofficeStore } from "./redux/store/backofficeStore";
import { Provider } from "react-redux";
import AlertsProvider from "./alert/AlertsContext";
import LoadingOverlay from "./components/LoadingOverlay";
import { ThemeProvider } from "styled-components";
import { reserveMeTheme } from "./theme/ThemeUtil";
import App from "./App";

const root = ReactDOM.createRoot(document.getElementById("root"));

root.render(
  <Provider store={backofficeStore}>
    <ThemeProvider theme={reserveMeTheme}>
      <AlertsProvider>
        <Suspense
          fallback={
            <div style={{ height: "100vh", width: "100vw" }}>
              <LoadingOverlay/>
            </div>
          }
        >
          <App />
        </Suspense>
      </AlertsProvider>
    </ThemeProvider>
  </Provider>
);
