import { useState } from "react";

import {
  Search,
  Plus,
  Clock,
  BookOpen,
  Users,
  X,
  CalendarClock,
} from "lucide-react";

import { useTimetable } from "../context/TimetableContext";
import { useDoubtSessions } from "../context/DoubtSessionContext";

import Sidebar from "../components/Sidebar";
import Navbar from "../components/Navbar";

import "../styles/Teachers.css";

function Teachers() {
  // ==========================================
  // SHARED CONTEXT DATA
  // ==========================================

  const { timetable } = useTimetable();

  const {
    doubtSessions,
    addDoubtSession,
    removeDoubtSession,
    isDoubtSession,
  } = useDoubtSessions();

  // ==========================================
  // STATE
  // ==========================================

  const [selectedFaculty, setSelectedFaculty] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [subjectFilter, setSubjectFilter] = useState("All");
  const [showModal, setShowModal] = useState(false);

  // ==========================================
  // DAYS AND TIME SLOTS
  // Must match Timetable.jsx
  // ==========================================

  const days = [
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
  ];

  const timeSlots = [
    "08:00 AM - 09:00 AM",
    "09:00 AM - 10:00 AM",
    "10:00 AM - 11:00 AM",
    "11:00 AM - 12:00 PM",
    "12:00 PM - 01:00 PM",
    "02:00 PM - 03:00 PM",
    "03:00 PM - 04:00 PM",
  ];

  // ==========================================
  // TEMPORARY FACULTY DATA
  // Later this will come from backend
  // ==========================================

  const [teachers, setTeachers] = useState([
    {
      id: "FAC001",
      name: "Faculty 1",
      subject: "Physics",
      batches: ["JEE Batch 1", "JEE Batch 2", "CET Batch 1"],
      status: "Available",
      freeSlot: "11:00 AM - 12:00 PM",
    },

    {
      id: "FAC002",
      name: "Faculty 2",
      subject: "Chemistry",
      batches: ["JEE Batch 1", "NEET Batch 1"],
      status: "In Class",
      freeSlot: "02:00 PM - 03:00 PM",
    },

    {
      id: "FAC003",
      name: "Faculty 3",
      subject: "Mathematics",
      batches: ["JEE Batch 2", "CET Batch 1", "10th CBSE"],
      status: "Available",
      freeSlot: "01:00 PM - 02:00 PM",
    },

    {
      id: "FAC004",
      name: "Faculty 4",
      subject: "Biology",
      batches: ["NEET Batch 1", "NEET Batch 2", "10th SSC"],
      status: "In Class",
      freeSlot: "03:00 PM - 04:00 PM",
    },

    {
      id: "FAC005",
      name: "Faculty 5",
      subject: "Physics",
      batches: ["NEET Batch 2", "CET Batch 2"],
      status: "Available",
      freeSlot: "04:00 PM - 05:00 PM",
    },

    {
      id: "FAC006",
      name: "Faculty 6",
      subject: "Chemistry",
      batches: ["CET Batch 1", "CET Batch 2", "10th CBSE"],
      status: "On Leave",
      freeSlot: "Not Available",
    },
  ]);

  // ==========================================
  // NEW FACULTY FORM
  // ==========================================

  const [newFaculty, setNewFaculty] = useState({
    name: "",
    subject: "Physics",
    batches: [],
    status: "Available",
    freeSlot: "",
  });

  // ==========================================
  // BATCH OPTIONS
  // ==========================================

  const batchOptions = [
    "JEE Batch 1",
    "JEE Batch 2",
    "NEET Batch 1",
    "NEET Batch 2",
    "CET Batch 1",
    "CET Batch 2",
    "10th CBSE",
    "10th SSC",
  ];

  // ==========================================
  // GET FACULTY WEEKLY SCHEDULE
  // ==========================================

  const getFacultySchedule = (facultyName) => {
    return days.map((day) => {
      const slots = timeSlots.map((time) => {
        const lecture = timetable.find(
          (item) =>
            item.faculty === facultyName &&
            item.day === day &&
            item.time === time
        );

        return {
          time,
          lecture,
          isFree: !lecture,
        };
      });

      return {
        day,
        slots,
      };
    });
  };

  // ==========================================
  // COUNT FACULTY LECTURES
  // ==========================================

  const getFacultyLectureCount = (facultyName) => {
    return timetable.filter(
      (lecture) => lecture.faculty === facultyName
    ).length;
  };

  // ==========================================
  // FIND DOUBT SESSION ID
  // ==========================================

  const getDoubtSessionId = (faculty, day, time) => {
    const session = doubtSessions.find(
      (item) =>
        item.faculty === faculty &&
        item.day === day &&
        item.time === time
    );

    return session?.id;
  };

  // ==========================================
  // FILTER FACULTY
  // ==========================================

  const filteredTeachers = teachers.filter((teacher) => {
    const matchesSearch =
      teacher.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      teacher.id.toLowerCase().includes(searchTerm.toLowerCase());

    const matchesSubject =
      subjectFilter === "All" ||
      teacher.subject === subjectFilter;

    return matchesSearch && matchesSubject;
  });

  // ==========================================
  // BATCH CHECKBOX HANDLING
  // ==========================================

  const handleBatchChange = (batch) => {
    setNewFaculty((previous) => {
      const alreadySelected = previous.batches.includes(batch);

      return {
        ...previous,

        batches: alreadySelected
          ? previous.batches.filter((item) => item !== batch)
          : [...previous.batches, batch],
      };
    });
  };

  // ==========================================
  // ADD FACULTY
  // ==========================================

  const handleAddFaculty = (event) => {
    event.preventDefault();

    if (!newFaculty.name.trim()) {
      return;
    }

    const faculty = {
      ...newFaculty,

      id: `FAC${String(teachers.length + 1).padStart(3, "0")}`,

      freeSlot:
        newFaculty.status === "On Leave"
          ? "Not Available"
          : newFaculty.freeSlot || "Not Assigned",
    };

    setTeachers((previous) => [...previous, faculty]);

    setNewFaculty({
      name: "",
      subject: "Physics",
      batches: [],
      status: "Available",
      freeSlot: "",
    });

    setShowModal(false);
  };

  // ==========================================
  // MAIN UI
  // ==========================================

  return (
    <div className="teachers-layout">
      <Sidebar />

      <div className="teachers-content">
        <Navbar />

        <main className="teachers-main">

          {/* ==================================
              HEADER
          ================================== */}

          <div className="teachers-header">
            <div>
              <h1>Faculty Management</h1>

              <p>
                Manage faculty assignments, subjects and doubt-session
                availability.
              </p>
            </div>

            <button
              type="button"
              className="add-teacher-btn"
              onClick={() => setShowModal(true)}
            >
              <Plus size={18} />
              Add Faculty
            </button>
          </div>

          {/* ==================================
              SUMMARY CARDS
          ================================== */}

          <div className="teacher-summary">

            <div className="teacher-summary-card">
              <Users size={22} />

              <div>
                <span>Total Faculty</span>
                <strong>{teachers.length}</strong>
              </div>
            </div>

            <div className="teacher-summary-card">
              <BookOpen size={22} />

              <div>
                <span>Subjects</span>
                <strong>4</strong>
              </div>
            </div>

            <div className="teacher-summary-card">
              <Clock size={22} />

              <div>
                <span>Available Now</span>

                <strong>
                  {
                    teachers.filter(
                      (teacher) => teacher.status === "Available"
                    ).length
                  }
                </strong>
              </div>
            </div>

          </div>

          {/* ==================================
              SEARCH AND FILTER
          ================================== */}

          <div className="teacher-controls">

            <div className="teacher-search">
              <Search size={18} />

              <input
                type="text"
                placeholder="Search faculty..."
                value={searchTerm}
                onChange={(event) =>
                  setSearchTerm(event.target.value)
                }
              />
            </div>

            <select
              value={subjectFilter}
              onChange={(event) =>
                setSubjectFilter(event.target.value)
              }
            >
              <option value="All">
                All Subjects
              </option>

              <option value="Physics">
                Physics
              </option>

              <option value="Chemistry">
                Chemistry
              </option>

              <option value="Mathematics">
                Mathematics
              </option>

              <option value="Biology">
                Biology
              </option>
            </select>

          </div>

          {/* ==================================
              FACULTY TABLE
          ================================== */}

          <div className="teacher-table-container">

            <table className="teacher-table">

              <thead>
                <tr>
                  <th>Faculty</th>
                  <th>Subject</th>
                  <th>Assigned Batches</th>
                  <th>Status</th>
                  <th>Doubt Session</th>
                  <th>Schedule</th>
                </tr>
              </thead>

              <tbody>

                {filteredTeachers.map((teacher) => (
                  <tr key={teacher.id}>

                    {/* FACULTY */}

                    <td>
                      <div className="faculty-name-cell">

                        <div className="faculty-avatar">
                          {teacher.name
                            .split(" ")
                            .map((word) => word[0])
                            .join("")}
                        </div>

                        <div>
                          <strong>{teacher.name}</strong>
                          <span>{teacher.id}</span>
                        </div>

                      </div>
                    </td>

                    {/* SUBJECT */}

                    <td>
                      <span className="subject-badge">
                        {teacher.subject}
                      </span>
                    </td>

                    {/* BATCHES */}

                    <td>
                      <div className="batch-list">

                        {teacher.batches.length > 0 ? (
                          teacher.batches.map((batch) => (
                            <span key={batch}>
                              {batch}
                            </span>
                          ))
                        ) : (
                          <span>Not Assigned</span>
                        )}

                      </div>
                    </td>

                    {/* STATUS */}

                    <td>
                      <span
                        className={`teacher-status ${teacher.status
                          .toLowerCase()
                          .replaceAll(" ", "-")}`}
                      >
                        {teacher.status}
                      </span>
                    </td>

                    {/* TEMPORARY DOUBT SESSION */}

                    <td>
                      <div className="free-slot">
                        <Clock size={15} />
                        <span>{teacher.freeSlot}</span>
                      </div>
                    </td>

                    {/* AVAILABILITY */}

                    <td>
                      <button
                        type="button"
                        className="availability-btn"
                        onClick={() =>
                          setSelectedFaculty(teacher)
                        }
                      >
                        <CalendarClock size={16} />
                        Availability
                      </button>
                    </td>

                  </tr>
                ))}

              </tbody>

            </table>

            {filteredTeachers.length === 0 && (
              <div className="no-teachers">
                No faculty found matching your search.
              </div>
            )}

          </div>

        </main>
      </div>

      {/* ======================================
          ADD FACULTY MODAL
      ====================================== */}

      {showModal && (
        <div
          className="modal-overlay"
          onClick={() => setShowModal(false)}
        >
          <div
            className="faculty-modal"
            onClick={(event) => event.stopPropagation()}
          >

            {/* HEADER */}

            <div className="modal-header">

              <div>
                <h2>Add Faculty</h2>

                <p>
                  Create a new faculty profile.
                </p>
              </div>

              <button
                type="button"
                className="modal-close"
                onClick={() => setShowModal(false)}
              >
                <X size={20} />
              </button>

            </div>

            {/* FORM */}

            <form onSubmit={handleAddFaculty}>

              {/* NAME */}

              <div className="form-group">
                <label>Faculty Name</label>

                <input
                  type="text"
                  placeholder="Example: Faculty 7"
                  value={newFaculty.name}
                  onChange={(event) =>
                    setNewFaculty({
                      ...newFaculty,
                      name: event.target.value,
                    })
                  }
                  required
                />
              </div>

              {/* SUBJECT */}

              <div className="form-group">
                <label>Subject</label>

                <select
                  value={newFaculty.subject}
                  onChange={(event) =>
                    setNewFaculty({
                      ...newFaculty,
                      subject: event.target.value,
                    })
                  }
                >
                  <option>Physics</option>
                  <option>Chemistry</option>
                  <option>Mathematics</option>
                  <option>Biology</option>
                </select>
              </div>

              {/* BATCHES */}

              <div className="form-group">
                <label>Assigned Batches</label>

                <div className="batch-checkbox-grid">

                  {batchOptions.map((batch) => (
                    <label
                      className="batch-checkbox"
                      key={batch}
                    >
                      <input
                        type="checkbox"
                        checked={newFaculty.batches.includes(batch)}
                        onChange={() =>
                          handleBatchChange(batch)
                        }
                      />

                      <span>{batch}</span>
                    </label>
                  ))}

                </div>
              </div>

              {/* STATUS */}

              <div className="form-group">
                <label>Status</label>

                <select
                  value={newFaculty.status}
                  onChange={(event) =>
                    setNewFaculty({
                      ...newFaculty,
                      status: event.target.value,
                    })
                  }
                >
                  <option>Available</option>
                  <option>In Class</option>
                  <option>On Leave</option>
                </select>
              </div>

              {/* TEMPORARY MANUAL SLOT */}

              {newFaculty.status !== "On Leave" && (
                <div className="form-group">
                  <label>
                    Doubt Session / Free Slot
                  </label>

                  <input
                    type="text"
                    placeholder="Example: 11:00 AM - 12:00 PM"
                    value={newFaculty.freeSlot}
                    onChange={(event) =>
                      setNewFaculty({
                        ...newFaculty,
                        freeSlot: event.target.value,
                      })
                    }
                  />
                </div>
              )}

              {/* ACTIONS */}

              <div className="modal-actions">

                <button
                  type="button"
                  className="cancel-btn"
                  onClick={() => setShowModal(false)}
                >
                  Cancel
                </button>

                <button
                  type="submit"
                  className="save-faculty-btn"
                >
                  Add Faculty
                </button>

              </div>

            </form>

          </div>
        </div>
      )}

      {/* ======================================
          FACULTY AVAILABILITY MODAL
      ====================================== */}

      {selectedFaculty && (
        <div
          className="availability-modal-overlay"
          onClick={() => setSelectedFaculty(null)}
        >
          <div
            className="availability-modal"
            onClick={(event) => event.stopPropagation()}
          >

            {/* HEADER */}

            <div className="availability-modal-header">

              <div>
                <h2>
                  Faculty Availability
                </h2>

                <p>
                  {selectedFaculty.name}
                  {" — "}
                  {selectedFaculty.subject}
                </p>

                <small>
                  {getFacultyLectureCount(
                    selectedFaculty.name
                  )}{" "}
                  scheduled lecture(s)
                </small>
              </div>

              <button
                type="button"
                onClick={() =>
                  setSelectedFaculty(null)
                }
              >
                <X size={20} />
              </button>

            </div>

            {/* ==================================
                LEGEND
            ================================== */}

            <div className="availability-legend">

              <span className="legend-free">
                Free Slot
              </span>

              <span className="legend-doubt">
                Doubt Session
              </span>

              <span className="legend-busy">
                Teaching
              </span>

            </div>

            {/* ==================================
                WEEKLY AVAILABILITY
            ================================== */}

            <div className="availability-days">

              {getFacultySchedule(
                selectedFaculty.name
              ).map((daySchedule) => (

                <div
                  className="availability-day"
                  key={daySchedule.day}
                >

                  <h3>
                    {daySchedule.day}
                  </h3>

                  <div className="availability-slots">

                    {daySchedule.slots.map((slot) => {
                      const markedForDoubts =
                        isDoubtSession(
                          selectedFaculty.name,
                          daySchedule.day,
                          slot.time
                        );

                      return (
                        <div
                          key={slot.time}
                          className={
                            !slot.isFree
                              ? "availability-slot busy-slot"
                              : markedForDoubts
                              ? "availability-slot doubt-slot"
                              : "availability-slot free-slot"
                          }
                        >

                          {/* TIME */}

                          <strong>
                            {slot.time}
                          </strong>

                          {/* =========================
                              BUSY / TEACHING
                          ========================= */}

                          {!slot.isFree ? (
                            <>
                              <span>
                                {slot.lecture.subject}
                              </span>

                              <small>
                                {slot.lecture.batch}
                              </small>
                            </>
                          ) : markedForDoubts ? (

                            /* =========================
                               DOUBT SESSION
                            ========================= */

                            <>
                              <span className="doubt-session-label">
                                Available for Doubts
                              </span>

                              <button
                                type="button"
                                className="remove-doubt-btn"
                                onClick={() => {
                                  const id =
                                    getDoubtSessionId(
                                      selectedFaculty.name,
                                      daySchedule.day,
                                      slot.time
                                    );

                                  if (id) {
                                    removeDoubtSession(id);
                                  }
                                }}
                              >
                                Remove
                              </button>
                            </>

                          ) : (

                            /* =========================
                               FREE SLOT
                            ========================= */

                            <>
                              <span>
                                Free
                              </span>

                              <button
                                type="button"
                                className="mark-doubt-btn"
                                onClick={() =>
                                  addDoubtSession({
                                    faculty:
                                      selectedFaculty.name,

                                    subject:
                                      selectedFaculty.subject,

                                    day:
                                      daySchedule.day,

                                    time:
                                      slot.time,
                                  })
                                }
                              >
                                Mark for Doubts
                              </button>
                            </>

                          )}

                        </div>
                      );
                    })}

                  </div>

                </div>

              ))}

            </div>

          </div>
        </div>
      )}

    </div>
  );
}

export default Teachers;