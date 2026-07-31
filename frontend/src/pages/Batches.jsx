import { useState } from "react";
import {
  Search,
  Plus,
  Users,
  BookOpen,
  X,
  GraduationCap,
} from "lucide-react";

import Sidebar from "../components/Sidebar";
import Navbar from "../components/Navbar";

import "../styles/Batches.css";

function Batches() {
  const [searchTerm, setSearchTerm] = useState("");
  const [courseFilter, setCourseFilter] = useState("All");
  const [showModal, setShowModal] = useState(false);

  const [batches, setBatches] = useState([
    {
      id: "BAT001",
      name: "JEE Batch 1",
      course: "JEE",
      subjects: ["Physics", "Chemistry", "Mathematics"],
      students: 40,
      room: "Room 101",
    },
    {
      id: "BAT002",
      name: "JEE Batch 2",
      course: "JEE",
      subjects: ["Physics", "Chemistry", "Mathematics"],
      students: 38,
      room: "Room 102",
    },
    {
      id: "BAT003",
      name: "NEET Batch 1",
      course: "NEET",
      subjects: ["Physics", "Chemistry", "Biology"],
      students: 42,
      room: "Room 201",
    },
    {
      id: "BAT004",
      name: "NEET Batch 2",
      course: "NEET",
      subjects: ["Physics", "Chemistry", "Biology"],
      students: 39,
      room: "Room 202",
    },
    {
      id: "BAT005",
      name: "CET Batch 1",
      course: "CET",
      subjects: ["Physics", "Chemistry", "Mathematics"],
      students: 45,
      room: "Room 103",
    },
    {
      id: "BAT006",
      name: "CET Batch 2",
      course: "CET",
      subjects: ["Physics", "Chemistry", "Mathematics"],
      students: 41,
      room: "Room 104",
    },
    {
      id: "BAT007",
      name: "10th CBSE",
      course: "10th",
      subjects: [
        "Physics",
        "Chemistry",
        "Mathematics",
        "Biology",
      ],
      students: 35,
      room: "Room 301",
    },
    {
      id: "BAT008",
      name: "10th SSC",
      course: "10th",
      subjects: [
        "Physics",
        "Chemistry",
        "Mathematics",
        "Biology",
      ],
      students: 37,
      room: "Room 302",
    },
  ]);

  const [newBatch, setNewBatch] = useState({
    name: "",
    course: "JEE",
    students: "",
    room: "",
  });

  const getSubjects = (course) => {
    if (course === "NEET") {
      return ["Physics", "Chemistry", "Biology"];
    }

    if (course === "10th") {
      return [
        "Physics",
        "Chemistry",
        "Mathematics",
        "Biology",
      ];
    }

    return ["Physics", "Chemistry", "Mathematics"];
  };

  const filteredBatches = batches.filter((batch) => {
    const matchesSearch =
      batch.name
        .toLowerCase()
        .includes(searchTerm.toLowerCase()) ||
      batch.id
        .toLowerCase()
        .includes(searchTerm.toLowerCase());

    const matchesCourse =
      courseFilter === "All" ||
      batch.course === courseFilter;

    return matchesSearch && matchesCourse;
  });

  const handleAddBatch = (event) => {
    event.preventDefault();

    if (!newBatch.name.trim()) {
      return;
    }

    const batch = {
      id: `BAT${String(batches.length + 1).padStart(3, "0")}`,
      name: newBatch.name,
      course: newBatch.course,
      subjects: getSubjects(newBatch.course),
      students: Number(newBatch.students) || 0,
      room: newBatch.room || "Not Assigned",
    };

    setBatches((previous) => [...previous, batch]);

    setNewBatch({
      name: "",
      course: "JEE",
      students: "",
      room: "",
    });

    setShowModal(false);
  };

  const totalStudents = batches.reduce(
    (total, batch) => total + batch.students,
    0
  );

  return (
    <div className="batches-layout">
      <Sidebar />

      <div className="batches-content">
        <Navbar />

        <main className="batches-main">

          <div className="batches-header">

            <div>
              <h1>Batch Management</h1>

              <p>
                Manage academic batches, subjects, students
                and classroom allocation.
              </p>
            </div>

            <button
              className="add-batch-btn"
              onClick={() => setShowModal(true)}
            >
              <Plus size={18} />
              Add Batch
            </button>

          </div>

          <div className="batch-summary">

            <div className="batch-summary-card">
              <GraduationCap size={22} />

              <div>
                <span>Total Batches</span>
                <strong>{batches.length}</strong>
              </div>
            </div>

            <div className="batch-summary-card">
              <Users size={22} />

              <div>
                <span>Total Students</span>
                <strong>{totalStudents}</strong>
              </div>
            </div>

            <div className="batch-summary-card">
              <BookOpen size={22} />

              <div>
                <span>Core Subjects</span>
                <strong>4</strong>
              </div>
            </div>

          </div>

          <div className="batch-controls">

            <div className="batch-search">

              <Search size={18} />

              <input
                type="text"
                placeholder="Search batches..."
                value={searchTerm}
                onChange={(event) =>
                  setSearchTerm(event.target.value)
                }
              />

            </div>

            <select
              value={courseFilter}
              onChange={(event) =>
                setCourseFilter(event.target.value)
              }
            >
              <option value="All">All Courses</option>
              <option value="JEE">JEE</option>
              <option value="NEET">NEET</option>
              <option value="CET">CET</option>
              <option value="10th">10th</option>
            </select>

          </div>

          <div className="batch-table-container">

            <table className="batch-table">

              <thead>
                <tr>
                  <th>Batch</th>
                  <th>Course</th>
                  <th>Subjects</th>
                  <th>Students</th>
                  <th>Classroom</th>
                </tr>
              </thead>

              <tbody>

                {filteredBatches.map((batch) => (

                  <tr key={batch.id}>

                    <td>
                      <div className="batch-name-cell">

                        <div className="batch-avatar">
                          {batch.course}
                        </div>

                        <div>
                          <strong>{batch.name}</strong>
                          <span>{batch.id}</span>
                        </div>

                      </div>
                    </td>

                    <td>
                      <span className="course-badge">
                        {batch.course}
                      </span>
                    </td>

                    <td>
                      <div className="subject-list">

                        {batch.subjects.map((subject) => (
                          <span key={subject}>
                            {subject}
                          </span>
                        ))}

                      </div>
                    </td>

                    <td>
                      <div className="student-count">
                        <Users size={15} />
                        {batch.students}
                      </div>
                    </td>

                    <td>{batch.room}</td>

                  </tr>

                ))}

              </tbody>

            </table>

            {filteredBatches.length === 0 && (
              <div className="no-batches">
                No batches found matching your search.
              </div>
            )}

          </div>

        </main>
      </div>

      {showModal && (

        <div
          className="batch-modal-overlay"
          onClick={() => setShowModal(false)}
        >

          <div
            className="batch-modal"
            onClick={(event) => event.stopPropagation()}
          >

            <div className="batch-modal-header">

              <div>
                <h2>Add Batch</h2>
                <p>Create a new academic batch.</p>
              </div>

              <button
                type="button"
                className="batch-modal-close"
                onClick={() => setShowModal(false)}
              >
                <X size={20} />
              </button>

            </div>

            <form onSubmit={handleAddBatch}>

              <div className="batch-form-group">
                <label>Batch Name</label>

                <input
                  type="text"
                  placeholder="Example: JEE Batch 3"
                  value={newBatch.name}
                  onChange={(event) =>
                    setNewBatch({
                      ...newBatch,
                      name: event.target.value,
                    })
                  }
                  required
                />
              </div>

              <div className="batch-form-group">
                <label>Course</label>

                <select
                  value={newBatch.course}
                  onChange={(event) =>
                    setNewBatch({
                      ...newBatch,
                      course: event.target.value,
                    })
                  }
                >
                  <option value="JEE">JEE</option>
                  <option value="NEET">NEET</option>
                  <option value="CET">CET</option>
                  <option value="10th">10th</option>
                </select>
              </div>

              <div className="auto-subject-box">
                <span>Subjects</span>

                <div>
                  {getSubjects(newBatch.course).map(
                    (subject) => (
                      <strong key={subject}>
                        {subject}
                      </strong>
                    )
                  )}
                </div>

                <small>
                  Subjects are automatically assigned according
                  to the selected course.
                </small>
              </div>

              <div className="batch-form-group">
                <label>Number of Students</label>

                <input
                  type="number"
                  min="0"
                  placeholder="Example: 40"
                  value={newBatch.students}
                  onChange={(event) =>
                    setNewBatch({
                      ...newBatch,
                      students: event.target.value,
                    })
                  }
                />
              </div>

              <div className="batch-form-group">
                <label>Classroom</label>

                <input
                  type="text"
                  placeholder="Example: Room 105"
                  value={newBatch.room}
                  onChange={(event) =>
                    setNewBatch({
                      ...newBatch,
                      room: event.target.value,
                    })
                  }
                />
              </div>

              <div className="batch-modal-actions">

                <button
                  type="button"
                  className="batch-cancel-btn"
                  onClick={() => setShowModal(false)}
                >
                  Cancel
                </button>

                <button
                  type="submit"
                  className="save-batch-btn"
                >
                  Add Batch
                </button>

              </div>

            </form>

          </div>

        </div>

      )}

    </div>
  );
}

export default Batches;