import { Outlet, Route, Routes } from "react-router-dom";
import BackofficeProxy from "./pages/BackofficeProxy";

const BackofficeRoutes = () => {
  return (
    <Routes>
      <Route element={<Outlet />}>
        <Route path="/" element={<BackofficeProxy />} />
      </Route>
    </Routes>
  );
};

export default BackofficeRoutes;
