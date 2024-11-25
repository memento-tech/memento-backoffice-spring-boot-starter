import { WidgetProvider } from "./providers/WidgetContext";
import { BackofficeAuthProvider } from "./BackofficeAuthContext";
import BackofficeRoutes from "./BackofficeRoutes";

const AuthBackofficeWrapper = () => (
  <BackofficeAuthProvider>
    <WidgetProvider>
      <BackofficeRoutes />
    </WidgetProvider>
  </BackofficeAuthProvider>
);

export default AuthBackofficeWrapper;
