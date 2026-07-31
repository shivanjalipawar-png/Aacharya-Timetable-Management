import { useState } from "react";
import {
  Search,
  Plus,
  BookOpen,
  Users,
  GraduationCap,
  X,
} from "lucide-react";

import Sidebar from "../components/Sidebar";
import Navbar from "../components/Navbar";

import "../styles/Subjects.css";

function Subjects() {
  const [searchTerm, setSearchTerm] = useState("");
  const [showModal, setShowModal] = useState(false);

  const [subjects, setSubjects] = useState([
    {
      id: "SUB001",
      name: "Physics",
      code: "PHY",
      faculty: ["Faculty 1", "Faculty 5"],
      courses: ["JEE", "NEET", "CET", "10th"],
      batches: 8,
    },
    {
      id: "SUB002",
      name: "Chemistry",
      code: "CHEM",
      faculty: ["Faculty 2", "Faculty 6"],
      courses: ["JEE", "NEET", "CET", "10th"],
      batches: 8,
    },
    {
      id: "SUB003",
      name: "Mathematics",
      code: "MATH",
      faculty: ["Faculty 3"],
      courses: ["JEE", "CET", "10th"],
      batches: 6,
    },
    {
      id: "SUB004",
      name: "Biology",
      code: "BIO",
      faculty: ["Faculty 4"],
      courses: ["NEET", "10th"],
      batches: 4,
    },
  ]);

  const [newSubject, setNewSubject] = useState({
    name: "",
    code: "",
  });

  const filteredSubjects = subjects.filter((subject) => {
    const search = searchTerm.toLowerCase();

    return (
      subject.name.toLowerCase().includes(search) ||
      subject.code.toLowerCase().includes(search) ||
      subject.id.toLowerCase().includes(search)
    );
  });

  const handleAddSubject = (event) => {
    event.preventDefault();

    if (!newSubject.name.trim() || !newSubject.code.trim()) {
      return;
    }

    const subject = {
      id: `SUB${String(subjects.length + 1).padStart(3, "0")}`,
      name: newSubject.name.trim(),
      code: newSubject.code.trim().toUpperCase(),
      faculty: [],
      courses: [],
      batches: 0,
    };

    setSubjects((previous) => [...previous, subject]);

    setNewSubject({
      name: "",
      code: "",
    });

    setShowModal(false);
  };

  return (
    <div className="subjects-layout">
      <Sidebar />

      <div className="subjects-content">
        <Navbar />

        <main className="subjects-main">

          {/* PAGE HEADER */}

          <div className="subjects-header">

            <div>
              <h1>Subject Management</h1>

              <p>
                Manage subjects and view their faculty, course and batch
                associations.
              </p>
            </div>

            <button
              className="add-subject-btn"
              onClick={() => setShowModal(true)}
            >
              <Plus size={18} />
              Add Subject
            </button>

          </div>


          {/* SUMMARY CARDS */}

          <div className="subject-summary">

            <div className="subject-summary-card">
              <BookOpen size={22} />

              <div>
                <span>Total Subjects</span>
                <strong>{subjects.length}</strong>
              </div>
            </div>

            <div className="subject-summary-card">
              <GraduationCap size={22} />

              <div>
                <span>Academic Programs</span>
                <strong>4</strong>
              </div>
            </div>

            <div className="subject-summary-card">
              <Users size={22} />

              <div>
                <span>Assigned Faculty</span>

                <strong>
                  {
                    new Set(
                      subjects.flatMap((subject) => subject.faculty)
                    ).size
                  }
                </strong>
              </div>

            </div>

          </div>


          {/* SEARCH */}

          <div className="subject-controls">

            <div className="subject-search">

              <Search size={18} />

              <input
                type="text"
                placeholder="Search subjects..."
                value={searchTerm}
                onChange={(event) =>
                  setSearchTerm(event.target.value)
                }
              />

            </div>

          </div>


          {/* SUBJECT TABLE */}

          <div className="subject-table-container">

            <table className="subject-table">

              <thead>
                <tr>
                  <th>Subject</th>
                  <th>Code</th>
                  <th>Programs</th>
                  <th>Faculty</th>
                  <th>Batches</th>
                </tr>
              </thead>

              <tbody>

                {filteredSubjects.map((subject) => (

                  <tr key={subject.id}>

                    <td>

                      <div className="subject-name-cell">

                        <div className="subject-avatar">
                          {subject.code.substring(0, 2)}
                        </div>

                        <div>
                          <strong>{subject.name}</strong>
                          <span>{subject.id}</span>
                        </div>

                      </div>

                    </td>

                    <td>
                      <span className="subject-code-badge">
                        {subject.code}
                      </span>
                    </td>

                    <td>

                      <div className="subject-course-list">

                        {subject.courses.length > 0 ? (
                          subject.courses.map((course) => (
                            <span key={course}>
                              {course}
                            </span>
                          ))
                        ) : (
                          <span className="not-assigned">
                            Not Assigned
                          </span>
                        )}

                      </div>

                    </td>

                    <td>

                      <div className="subject-faculty-list">

                        {subject.faculty.length > 0 ? (
                          subject.faculty.map((faculty) => (
                            <span key={faculty}>
                              {faculty}
                            </span>
                          ))
                        ) : (
                          <span className="not-assigned">
                            Not Assigned
                          </span>
                        )}

                      </div>

                    </td>

                    <td>

                      <div className="subject-batch-count">
                        <Users size={15} />
                        {subject.batches}
                      </div>

                    </td>

                  </tr>

                ))}

              </tbody>

            </table>


            {filteredSubjects.length === 0 && (

              <div className="no-subjects">
                No subjects found matching your search.
              </div>

            )}

          </div>

        </main>
      </div>


      {/* ADD SUBJECT MODAL */}

      {showModal && (

        <div
          className="subject-modal-overlay"
          onClick={() => setShowModal(false)}
        >

          <div
            className="subject-modal"
            onClick={(event) => event.stopPropagation()}
          >

            <div className="subject-modal-header">

              <div>
                <h2>Add Subject</h2>

                <p>
                  Add a new subject to the academic system.
                </p>
              </div>

              <button
                type="button"
                className="subject-modal-close"
                onClick={() => setShowModal(false)}
              >
                <X size={20} />
              </button>

            </div>


            <form onSubmit={handleAddSubject}>

              <div className="subject-form-group">

                <label>Subject Name</label>

                <input
                  type="text"
                  placeholder="Example: Computer Science"
                  value={newSubject.name}
                  onChange={(event) =>
                    setNewSubject({
                      ...newSubject,
                      name: event.target.value,
                    })
                  }
                  required
                />

              </div>


              <div className="subject-form-group">

                <label>Subject Code</label>

                <input
                  type="text"
                  placeholder="Example: CS"
                  value={newSubject.code}
                  onChange={(event) =>
                    setNewSubject({
                      ...newSubject,
                      code: event.target.value,
                    })
                  }
                  required
                />

              </div>


              <div className="subject-info-box">

                <BookOpen size={18} />

                <p>
                  Faculty and batch assignments can be configured after
                  creating the subject.
                </p>

              </div>


              <div className="subject-modal-actions">

                <button
                  type="button"
                  className="subject-cancel-btn"
                  onClick={() => setShowModal(false)}
                >
                  Cancel
                </button>

                <button
                  type="submit"
                  className="save-subject-btn"
                >
                  Add Subject
                </button>

              </div>

            </form>

          </div>

        </div>

      )}

    </div>
  );
}

export default Subjects;