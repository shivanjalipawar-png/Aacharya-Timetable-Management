
import "../styles/Settings.css";

function Settings() {
  return (
    <div className="settings-page">

      <div className="settings-header">
        <h1>Portal Settings</h1>
        <p>Manage and view your academic portal information.</p>
      </div>

      <div className="settings-grid">

        {/* Administrator Profile */}
        <div className="settings-card">
          <div className="settings-card-header">
            <h2>Administrator Profile</h2>
          </div>

          <div className="settings-info">
            <div className="settings-avatar">A</div>

            <div>
              <h3>Administrator</h3>
              <p>Admin Account</p>
            </div>
          </div>

          <div className="settings-detail">
            <span>Role</span>
            <strong>ADMIN</strong>
          </div>

          <div className="settings-detail">
            <span>Access Level</span>
            <strong>Full Access</strong>
          </div>
        </div>

        {/* Academy Information */}
        <div className="settings-card">
          <div className="settings-card-header">
            <h2>Academy Information</h2>
          </div>

          <div className="settings-detail">
            <span>Institution Name</span>
            <strong>Acharya Academy</strong>
          </div>

          <div className="settings-detail">
            <span>Portal Name</span>
            <strong>Academic Portal</strong>
          </div>

          <div className="settings-detail">
            <span>Institution Type</span>
            <strong>Educational Institution</strong>
          </div>
        </div>

        {/* Portal Information */}
        <div className="settings-card">
          <div className="settings-card-header">
            <h2>Portal Information</h2>
          </div>

          <div className="settings-detail">
            <span>Application</span>
            <strong>Timetable Management System</strong>
          </div>

          <div className="settings-detail">
            <span>Portal Module</span>
            <strong>Administration</strong>
          </div>

          <div className="settings-detail">
            <span>Account Management</span>
            <strong>Admin Controlled</strong>
          </div>
        </div>

      </div>
    </div>
  );
}

export default Settings;
