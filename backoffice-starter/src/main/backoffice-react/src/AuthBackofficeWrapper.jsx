import { WidgetProvider } from "./providers/WidgetContext";
import BackofficeRoutes from "./BackofficeRoutes";

const AuthBackofficeWrapper = () => (
  <WidgetProvider>
    <BackofficeRoutes />
  </WidgetProvider>
);

export default AuthBackofficeWrapper;
