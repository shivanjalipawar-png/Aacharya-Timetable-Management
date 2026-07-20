import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Teachers from "./pages/Teachers";
import Batches from "./pages/Batches";
import Subjects from "./pages/Subjects";
import Timetable from "./pages/Timetable";
import Notifications from "./pages/Notifications";
import NotFound from "./pages/NotFound";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/teachers" element={<Teachers />} />
        <Route path="/batches" element={<Batches />} />
        <Route path="/subjects" element={<Subjects />} />
        <Route path="/timetable" element={<Timetable />} />
        <Route path="/notifications" element={<Notifications />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;