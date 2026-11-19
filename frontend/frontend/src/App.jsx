import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Teachers from "./pages/Teachers";
import Batches from "./pages/Batches";
import Subjects from "./pages/Subjects";
import Timetable from "./pages/Timetable";
import Notifications from "./pages/Notifications";
import NotFound from "./pages/NotFound";
import Students from "./pages/Students";
//import Settings from "./pages/Settings";

import AdminLayout from "./layouts/AdminLayout";

function App() {
  return (
    <BrowserRouter>
      <Routes>

        {/* Login */}
        <Route path="/" element={<Login />} />

        {/* Admin Pages */}
        <Route
          path="/dashboard"
          element={
            <AdminLayout>
              <Dashboard />
            </AdminLayout>
          }
        />

        <Route
          path="/teachers"
          element={
            <AdminLayout>
              <Teachers />
            </AdminLayout>
          }
        />
        <Route
       path="/students"
       element={
       <AdminLayout>
       <Students />
       </AdminLayout>
           }
         />

        <Route
          path="/batches"
          element={
            <AdminLayout>
              <Batches />
            </AdminLayout>
          }
        />

        <Route
          path="/subjects"
          element={
            <AdminLayout>
              <Subjects />
            </AdminLayout>
          }
        />

        <Route
          path="/timetable"
          element={
            <AdminLayout>
              <Timetable />
            </AdminLayout>
          }
        />

        <Route
          path="/notifications"
          element={
            <AdminLayout>
              <Notifications />
            </AdminLayout>
          }
        />

        {/* 404 */}
        <Route path="*" element={<NotFound />} />

      </Routes>
    </BrowserRouter>
  );
}

export default App;