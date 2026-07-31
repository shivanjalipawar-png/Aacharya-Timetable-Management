import Sidebar from "../components/Sidebar";
import Navbar from "../components/Navbar";
import DashboardCard from "../components/DashboardCard";
import "../styles/Dashboard.css";

function Dashboard() {

  const cards = [
    { title: "Teachers", count: "12", subtitle: "Active Faculty" },
    { title: "Students", count: "320", subtitle: "Registered Students" },
    { title: "Batches", count: "8", subtitle: "Running Batches" },
    { title: "Today's Classes", count: "26", subtitle: "Scheduled Today" },
  ];

  const schedule = [
    {
      time: "08:00 AM",
      batch: "JEE Batch 1",
      subject: "Physics",
      room: "Room 101",
    },
    {
      time: "09:00 AM",
      batch: "NEET Batch 1",
      subject: "Biology",
      room: "Room 203",
    },
    {
      time: "10:00 AM",
      batch: "CET Batch 2",
      subject: "Chemistry",
      room: "Room 105",
    },
  ];

  const faculty = [
    {
      name: "Faculty 1",
      subject: "Physics",
      slot: "11:00 AM - 12:00 PM",
      location: "Faculty Cabin 1",
    },
    {
      name: "Faculty 4",
      subject: "Chemistry",
      slot: "02:00 PM - 03:00 PM",
      location: "Faculty Cabin 2",
    },
    {
      name: "Faculty 7",
      subject: "Mathematics",
      slot: "03:00 PM - 04:00 PM",
      location: "Faculty Cabin 3",
    },
  ];

  const updates = [
    "Timetable updated for JEE Batch 2",
    "New CET Batch created",
    "Weekly JEE Test scheduled",
    "Faculty meeting on Friday",
  ];

  const actions = [
    "Add Teacher",
    "Create Batch",
    "Generate Timetable",
    "Publish Notice",
  ];

  return (
    <div className="dashboard-layout">

      <Sidebar />

      <div className="dashboard-content">

        <Navbar />

        <div className="dashboard-main">

          <div className="dashboard-header">

            <div>

              <h1>Welcome, Admin 👋</h1>

              <p>
                1729 Acharya Academy Academic Management Portal
              </p>

            </div>

          </div>

          <div className="card-container">

            {cards.map((card) => (

              <DashboardCard

                key={card.title}

                title={card.title}

                count={card.count}

                subtitle={card.subtitle}

              />

            ))}

          </div>

          <div className="dashboard-grid">

            <div className="dashboard-box">

              <h2>📅 Today's Schedule</h2>

              {schedule.map((lecture, index) => (

                <div className="schedule-item" key={index}>

                  <div>

                    <strong>{lecture.time}</strong>

                    <h4>{lecture.subject}</h4>

                    <p>{lecture.batch}</p>

                    <span>{lecture.room}</span>

                  </div>

                </div>

              ))}

            </div>

            <div className="dashboard-box">

              <h2>👨‍🏫 Faculty Available for Doubt Session</h2>

              {faculty.map((teacher, index) => (

                <div className="faculty-item" key={index}>

                  <div className="status-dot"></div>

                  <div>

                    <strong>{teacher.name}</strong>

                    <p>{teacher.subject}</p>

                    <span>{teacher.slot}</span>

                    <small>{teacher.location}</small>

                  </div>

                </div>

              ))}

            </div>

            <div className="dashboard-box">

              <h2>⚡ Quick Actions</h2>

              <div className="action-grid">

                {actions.map((action) => (

                  <button key={action} className="action-btn">

                    {action}

                  </button>

                ))}

              </div>

            </div>

            <div className="dashboard-box">

              <h2>📢 Recent Updates</h2>

              <ul>

                {updates.map((item, index) => (

                  <li key={index}>✓ {item}</li>

                ))}

              </ul>

            </div>

          </div>

        </div>

      </div>

    </div>

  );
}

export default Dashboard;