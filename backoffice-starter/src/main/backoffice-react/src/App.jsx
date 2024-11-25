import { BrowserRouter, Route, Routes } from "react-router-dom";
import AuthBackofficeWrapper from "./AuthBackofficeWrapper";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/backoffice/*" element={<AuthBackofficeWrapper />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
