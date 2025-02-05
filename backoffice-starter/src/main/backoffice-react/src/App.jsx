import { BrowserRouter, Route, Routes } from "react-router-dom";
import AuthBackofficeWrapper from "./AuthBackofficeWrapper";
import LoginPage from "./pages/LoginPage";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/backoffice/console/*"
          element={<AuthBackofficeWrapper />}
        />
        <Route path="/backoffice/login" element={<LoginPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
