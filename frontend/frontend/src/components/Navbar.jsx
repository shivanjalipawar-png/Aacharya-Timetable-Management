
import { useLocation } from "react-router-dom";
import "../styles/Navbar.css";

function Navbar() {

  const location = useLocation();

  const pageTitles = {
    "/dashboard": "Dashboard",
    "/teachers": "Teachers",
    "/students": "Students",
    "/batches": "Batches",
    "/subjects": "Subjects",
    "/timetable": "Timetable",
    "/notifications": "Notices",
    "/settings": "Settings",
  };

  const currentTitle = pageTitles[location.pathname] || "Dashboard";

  return (
    <div className="navbar">

      <h2>{currentTitle}</h2>

      <div className="profile">
        <span>Admin</span>
      </div>

    </div>
  );
}

export default Navbar;
