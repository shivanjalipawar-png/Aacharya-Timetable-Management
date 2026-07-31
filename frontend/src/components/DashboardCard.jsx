import "../styles/DashboardCard.css";

function DashboardCard({ title, count, subtitle }) {
  return (
    <div className="dashboard-card">
      <div className="card-accent"></div>

      <p className="card-title">{title}</p>

      <h2 className="card-count">{count}</h2>

      <p className="card-subtitle">{subtitle}</p>
    </div>
  );
}

export default DashboardCard;