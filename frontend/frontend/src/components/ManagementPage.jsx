
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { managementConfig } from "../config/managementConfig";
import "../styles/ManagementPage.css";

function ManagementPage({ section }) {
  const config = managementConfig[section];
  const navigate = useNavigate();

  const [records, setRecords] = useState(() => {
    try {
      const saved = localStorage.getItem(`acharya-${section}`);
      return saved
        ? JSON.parse(saved)
        : config?.initialData || [];
    } catch {
      return config?.initialData || [];
    }
  });

  const [search, setSearch] = useState("");
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({});
  const [message, setMessage] = useState("");

  useEffect(() => {
    if (config) {
      localStorage.setItem(
        `acharya-${section}`,
        JSON.stringify(records)
      );
    }
  }, [records, section, config]);

  if (!config) {
    return <p>Section configuration not found.</p>;
  }

  const updateField = (name, value) => {
    setFormData((previous) => ({
      ...previous,
      [name]: value,
    }));
  };

  const addRecord = (event) => {
    event.preventDefault();

    const record = {
      ...formData,
      id: Date.now(),
    };

    setRecords((previous) => [record, ...previous]);
    setFormData({});
    setShowForm(false);
    setMessage("Record added successfully.");

    window.setTimeout(() => setMessage(""), 2500);
  };

  const deleteRecord = (id) => {
    if (window.confirm("Are you sure you want to delete this record?")) {
      setRecords((previous) =>
        previous.filter((record) => record.id !== id)
      );

      setMessage("Record deleted.");
      window.setTimeout(() => setMessage(""), 2500);
    }
  };

  const filteredRecords = records.filter((record) =>
    Object.values(record).some((value) =>
      String(value ?? "")
        .toLowerCase()
        .includes(search.toLowerCase())
    )
  );

  const displayValue = (value) => value || "—";

  return (
    <main className="management-main">

      {/* Page Heading */}
      <div className="management-heading">
        <div>
          <p className="management-eyebrow">
            1729 ACHARYA ACADEMY
          </p>

          <h1>{config.title}</h1>
          <p>{config.description}</p>
        </div>

        <button
          className="management-primary-btn"
          onClick={() => {
            setFormData({});
            setShowForm(!showForm);
          }}
        >
          {showForm
            ? "Close Form"
            : `+ Add ${
                section === "notices"
                  ? "Notice"
                  : section === "timetable"
                  ? "Class"
                  : section === "subjects"
                  ? "Subject"
                  : section === "batches"
                  ? "Batch"
                  : "Student"
              }`}
        </button>
      </div>

      {/* Success Message */}
      {message && (
        <div className="management-message">
          {message}
        </div>
      )}

      {/* Add Record Form */}
      {showForm && (
        <section className="management-panel">
          <h2>
            Add{" "}
            {section === "notices"
              ? "Notice"
              : section === "timetable"
              ? "Class"
              : section.slice(0, -1)}
          </h2>

          <form
            className="management-form"
            onSubmit={addRecord}
          >
            {config.fields.map((field) => (
              <label
                key={field.name}
                className="management-field"
              >
                <span>{field.label}</span>

                {field.type === "select" ? (
                  <select
                    value={formData[field.name] || ""}
                    onChange={(event) =>
                      updateField(field.name, event.target.value)
                    }
                    required={field.required !== false}
                  >
                    <option value="">
                      Select {field.label}
                    </option>

                    {field.options.map((option) => (
                      <option key={option} value={option}>
                        {option}
                      </option>
                    ))}
                  </select>
                ) : field.type === "textarea" ? (
                  <textarea
                    value={formData[field.name] || ""}
                    onChange={(event) =>
                      updateField(field.name, event.target.value)
                    }
                    required={field.required !== false}
                    rows={4}
                  />
                ) : (
                  <input
                    type={field.type || "text"}
                    value={formData[field.name] || ""}
                    onChange={(event) =>
                      updateField(field.name, event.target.value)
                    }
                    required={field.required !== false}
                    min={
                      field.type === "number"
                        ? "0"
                        : undefined
                    }
                  />
                )}
              </label>
            ))}

            <div className="management-form-actions">
              <button
                type="submit"
                className="management-primary-btn"
              >
                Save Record
              </button>

              <button
                type="button"
                className="management-secondary-btn"
                onClick={() => setShowForm(false)}
              >
                Cancel
              </button>
            </div>
          </form>
        </section>
      )}

      {/* Records Table */}
      <section className="management-panel">
        <div className="management-toolbar">
          <div>
            <h2>All {config.title}</h2>
            <span>
              {filteredRecords.length} record(s)
            </span>
          </div>

          <input
            className="management-search"
            type="search"
            placeholder={`Search ${config.title.toLowerCase()}...`}
            value={search}
            onChange={(event) =>
              setSearch(event.target.value)
            }
          />
        </div>

        <div className="management-table-wrap">
          <table className="management-table">
            <thead>
              <tr>
                {config.columns.map((column) => (
                  <th key={column}>
                    {column === "students"
                      ? "Capacity"
                      : column.charAt(0).toUpperCase() +
                        column.slice(1)}
                  </th>
                ))}

                <th>Action</th>
              </tr>
            </thead>

            <tbody>
              {filteredRecords.map((record) => (
                <tr key={record.id}>
                  {config.columns.map((column) => (
                    <td key={column}>
                      {column === "message" ? (
                        <span className="management-message-cell">
                          {displayValue(record[column])}
                        </span>
                      ) : (
                        displayValue(record[column])
                      )}
                    </td>
                  ))}

                  <td>
                    <button
                      className="management-delete-btn"
                      onClick={() => deleteRecord(record.id)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}

              {filteredRecords.length === 0 && (
                <tr>
                  <td
                    colSpan={config.columns.length + 1}
                    className="management-empty"
                  >
                    No records found. Try another search or add a record.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </section>

      {/* Back to Dashboard */}
      <button
        className="management-back-btn"
        onClick={() => navigate("/dashboard")}
      >
        ← Back to Dashboard
      </button>

    </main>
  );
}

export default ManagementPage;
