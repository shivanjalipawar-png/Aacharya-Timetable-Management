
import "../styles/Notifications.css";

function Notifications() {
  const notices = [
    {
      title: "Timetable Published",
      description:
        "The updated academic timetable is now available for all batches.",
      date: "20 Nov 2026",
      category: "Academic",
      priority: "Important",
    },
    {
      title: "Faculty Meeting",
      description:
        "All faculty members are requested to attend the scheduled meeting.",
      date: "22 Nov 2026",
      category: "Meeting",
      priority: "General",
    },
    {
      title: "Assignment Submission",
      description:
        "Students are reminded to submit their pending assignments on time.",
      date: "25 Nov 2026",
      category: "Student Notice",
      priority: "General",
    },
  ];

  return (
    <div className="notices-page">

      <div className="notices-header">
        <div>
          <h1>Notices & Announcements</h1>
          <p>Stay updated with the latest academic announcements.</p>
        </div>
      </div>

      <div className="notices-summary">
        <div className="notice-summary-card">
          <h3>{notices.length}</h3>
          <p>Total Notices</p>
        </div>

        <div className="notice-summary-card">
          <h3>
            {notices.filter((notice) => notice.priority === "Important").length}
          </h3>
          <p>Important Notices</p>
        </div>

        <div className="notice-summary-card">
          <h3>{notices.filter((notice) => notice.category === "Meeting").length}</h3>
          <p>Meetings</p>
        </div>
      </div>

      <div className="notices-section">
        <h2>Recent Announcements</h2>

        <div className="notices-list">
          {notices.map((notice, index) => (
            <div className="notice-card" key={index}>

              <div className="notice-card-content">
                <div className="notice-title-row">
                  <h3>{notice.title}</h3>

                  <span
                    className={
                      notice.priority === "Important"
                        ? "notice-priority important"
                        : "notice-priority general"
                    }
                  >
                    {notice.priority}
                  </span>
                </div>

                <p>{notice.description}</p>

                <div className="notice-details">
                  <span>{notice.category}</span>
                  <span>{notice.date}</span>
                </div>
              </div>

            </div>
          ))}
        </div>
      </div>

    </div>
  );
}

export default Notifications;
