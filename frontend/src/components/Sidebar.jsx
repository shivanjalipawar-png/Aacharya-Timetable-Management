import { Link, useLocation } from "react-router-dom";
import "../styles/Sidebar.css";

function Sidebar() {

  const location = useLocation();

  const menuItems = [
    { name: "Dashboard", path: "/dashboard" },
    { name: "Teachers", path: "/teachers" },
    { name: "Students", path: "/students" },
    { name: "Batches", path: "/batches" },
    { name: "Subjects", path: "/subjects" },
    { name: "Timetable", path: "/timetable" },
    { name: "Notices", path: "/notifications" },
    { name: "Settings", path: "/settings" },
  ];

  return (
    <aside className="sidebar">

      <div className="sidebar-logo">

        <div className="logo-box">
          1729
        </div>

        <div>

          <h2>Acharya Academy</h2>

          <p>Academic Portal</p>

        </div>

      </div>

      <nav>

        {menuItems.map((item) => (

          <Link
            key={item.path}
            to={item.path}
            className={
              location.pathname === item.path
                ? "menu-item active"
                : "menu-item"
            }
          >
            {item.name}
          </Link>

        ))}

      </nav>

      <div className="sidebar-footer">

        <p>Administrator</p>

        <small>1729 Acharya Academy</small>

      </div>

    </aside>
  );
}

export default Sidebar;