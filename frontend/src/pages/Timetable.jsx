import { useState } from "react";
import {
  CalendarDays,
  Plus,
  Clock,
  MapPin,
  UserRound,
  Trash2,
  AlertTriangle,
  X,
  Pencil,
} from "lucide-react";

import Sidebar from "../components/Sidebar";
import Navbar from "../components/Navbar";
import { useTimetable } from "../context/TimetableContext";

import "../styles/Timetable.css";

function Timetable() {
  // ============================
  // SHARED TIMETABLE DATA
  // ============================

  const {
    timetable,
    addLecture,
    updateLecture,
    deleteLecture,
  } = useTimetable();

  // ============================
  // BASIC DATA
  // ============================

  const batches = [
    "JEE Batch 1",
    "JEE Batch 2",
    "NEET Batch 1",
    "NEET Batch 2",
    "CET Batch 1",
    "CET Batch 2",
    "10th CBSE",
    "10th SSC",
  ];

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

  const facultyBySubject = {
    Physics: ["Faculty 1", "Faculty 5"],
    Chemistry: ["Faculty 2", "Faculty 6"],
    Mathematics: ["Faculty 3"],
    Biology: ["Faculty 4"],
  };

  // ============================
  // STATE
  // ============================

  const [selectedBatch, setSelectedBatch] =
    useState("JEE Batch 1");

  const [showModal, setShowModal] = useState(false);

  const [editingLectureId, setEditingLectureId] =
    useState(null);

  const [errorMessage, setErrorMessage] =
    useState("");

  const [lectureForm, setLectureForm] = useState({
    day: "Monday",
    time: "08:00 AM - 09:00 AM",
    subject: "Physics",
    faculty: "Faculty 1",
    room: "",
  });

  // ============================
  // SUBJECTS ACCORDING TO BATCH
  // ============================

  const getSubjectsForBatch = (batch) => {
    if (batch.startsWith("NEET")) {
      return ["Physics", "Chemistry", "Biology"];
    }

    if (batch.startsWith("10th")) {
      return [
        "Physics",
        "Chemistry",
        "Mathematics",
        "Biology",
      ];
    }

    return [
      "Physics",
      "Chemistry",
      "Mathematics",
    ];
  };

  const subjectsForSelectedBatch =
    getSubjectsForBatch(selectedBatch);

  // ============================
  // CLOSE MODAL
  // ============================

  const closeModal = () => {
    setShowModal(false);
    setEditingLectureId(null);
    setErrorMessage("");
  };

  // ============================
  // OPEN ADD LECTURE
  // ============================

  const openAddLecture = () => {
    const firstSubject =
      subjectsForSelectedBatch[0];

    setEditingLectureId(null);

    setLectureForm({
      day: "Monday",
      time: timeSlots[0],
      subject: firstSubject,
      faculty:
        facultyBySubject[firstSubject][0],
      room: "",
    });

    setErrorMessage("");
    setShowModal(true);
  };

  // ============================
  // CLICK EMPTY CELL
  // ============================

  const openLectureForSlot = (day, time) => {
    const firstSubject =
      subjectsForSelectedBatch[0];

    setEditingLectureId(null);

    setLectureForm({
      day,
      time,
      subject: firstSubject,
      faculty:
        facultyBySubject[firstSubject][0],
      room: "",
    });

    setErrorMessage("");
    setShowModal(true);
  };

  // ============================
  // EDIT LECTURE
  // ============================

  const openEditLecture = (lecture) => {
    setEditingLectureId(lecture.id);

    setLectureForm({
      day: lecture.day,
      time: lecture.time,
      subject: lecture.subject,
      faculty: lecture.faculty,
      room: lecture.room,
    });

    setErrorMessage("");
    setShowModal(true);
  };

  // ============================
  // SUBJECT CHANGE
  // ============================

  const handleSubjectChange = (subject) => {
    const facultyList =
      facultyBySubject[subject] || [];

    setLectureForm((previous) => ({
      ...previous,
      subject,
      faculty: facultyList[0] || "",
    }));
  };

  // ============================
  // SAVE LECTURE
  // ============================

  const handleSaveLecture = (event) => {
    event.preventDefault();

    setErrorMessage("");

    const room = lectureForm.room.trim();

    if (!room) {
      setErrorMessage(
        "Please enter a classroom."
      );
      return;
    }

    /*
      Ignore the current lecture when editing.
      Otherwise it would conflict with itself.
    */

    const otherLectures = timetable.filter(
      (lecture) =>
        lecture.id !== editingLectureId
    );

    // ============================
    // BATCH CONFLICT
    // ============================

    const batchConflict =
      otherLectures.some(
        (lecture) =>
          lecture.batch === selectedBatch &&
          lecture.day === lectureForm.day &&
          lecture.time === lectureForm.time
      );

    if (batchConflict) {
      setErrorMessage(
        "This batch already has a lecture during the selected time slot."
      );
      return;
    }

    // ============================
    // FACULTY CONFLICT
    // ============================

    const facultyConflict =
      otherLectures.some(
        (lecture) =>
          lecture.faculty ===
            lectureForm.faculty &&
          lecture.day === lectureForm.day &&
          lecture.time === lectureForm.time
      );

    if (facultyConflict) {
      setErrorMessage(
        `${lectureForm.faculty} is already assigned to another batch during this time.`
      );
      return;
    }

    // ============================
    // CLASSROOM CONFLICT
    // ============================

    const roomConflict =
      otherLectures.some(
        (lecture) =>
          lecture.room
            .trim()
            .toLowerCase() ===
            room.toLowerCase() &&
          lecture.day === lectureForm.day &&
          lecture.time === lectureForm.time
      );

    if (roomConflict) {
      setErrorMessage(
        "This classroom is already occupied during the selected time."
      );
      return;
    }

    // ============================
    // UPDATE EXISTING
    // ============================

    if (editingLectureId !== null) {
      updateLecture(editingLectureId, {
        batch: selectedBatch,
        ...lectureForm,
        room,
      });
    }

    // ============================
    // ADD NEW
    // ============================

    else {
      addLecture({
        batch: selectedBatch,
        ...lectureForm,
        room,
      });
    }

    closeModal();
  };

  // ============================
  // FIND LECTURE FOR CELL
  // ============================

  const getLecture = (day, time) => {
    return timetable.find(
      (lecture) =>
        lecture.batch === selectedBatch &&
        lecture.day === day &&
        lecture.time === time
    );
  };

  return (
    <div className="timetable-layout">
      <Sidebar />

      <div className="timetable-content">
        <Navbar />

        <main className="timetable-main">

          {/* ============================
              HEADER
          ============================ */}

          <div className="timetable-header">

            <div>
              <h1>
                Timetable Management
              </h1>

              <p>
                Create, edit and manage
                weekly academic schedules
                for all batches.
              </p>
            </div>

            <button
              type="button"
              className="add-lecture-btn"
              onClick={openAddLecture}
            >
              <Plus size={18} />

              Add Lecture
            </button>

          </div>

          {/* ============================
              BATCH TOOLBAR
          ============================ */}

          <div className="timetable-toolbar">

            <div className="batch-selector">

              <label>
                Select Batch
              </label>

              <select
                value={selectedBatch}
                onChange={(event) => {
                  setSelectedBatch(
                    event.target.value
                  );

                  closeModal();
                }}
              >

                {batches.map((batch) => (
                  <option
                    key={batch}
                    value={batch}
                  >
                    {batch}
                  </option>
                ))}

              </select>

            </div>

            <div className="selected-batch-info">

              <CalendarDays size={20} />

              <div>

                <span>
                  Viewing timetable for
                </span>

                <strong>
                  {selectedBatch}
                </strong>

              </div>

            </div>

          </div>

          {/* ============================
              WEEKLY TIMETABLE
          ============================ */}

          <div className="weekly-table-wrapper">

            <table className="weekly-table">

              <thead>

                <tr>

                  <th>Time</th>

                  {days.map((day) => (
                    <th key={day}>
                      {day}
                    </th>
                  ))}

                </tr>

              </thead>

              <tbody>

                {timeSlots.map((time) => (

                  <tr key={time}>

                    {/* TIME */}

                    <td className="time-column">

                      <Clock size={14} />

                      <span>
                        {time}
                      </span>

                    </td>

                    {/* DAYS */}

                    {days.map((day) => {

                      const lecture =
                        getLecture(
                          day,
                          time
                        );

                      return (

                        <td
                          key={`${day}-${time}`}
                          className="lecture-cell"
                        >

                          {lecture ? (

                            <div className="lecture-card">

                              <div className="lecture-top">

                                <strong>
                                  {
                                    lecture.subject
                                  }
                                </strong>

                                <div className="lecture-actions">

                                  {/* EDIT */}

                                  <button
                                    type="button"
                                    title="Edit lecture"
                                    onClick={() =>
                                      openEditLecture(
                                        lecture
                                      )
                                    }
                                  >
                                    <Pencil
                                      size={13}
                                    />
                                  </button>

                                  {/* DELETE */}

                                  <button
                                    type="button"
                                    title="Delete lecture"
                                    onClick={() =>
                                      deleteLecture(
                                        lecture.id
                                      )
                                    }
                                  >
                                    <Trash2
                                      size={13}
                                    />
                                  </button>

                                </div>

                              </div>

                              {/* FACULTY */}

                              <span>

                                <UserRound
                                  size={13}
                                />

                                {
                                  lecture.faculty
                                }

                              </span>

                              {/* ROOM */}

                              <span>

                                <MapPin
                                  size={13}
                                />

                                {
                                  lecture.room
                                }

                              </span>

                            </div>

                          ) : (

                            <button
                              type="button"
                              className="free-period"
                              onClick={() =>
                                openLectureForSlot(
                                  day,
                                  time
                                )
                              }
                            >

                              <Plus size={14} />

                              <span>
                                Add Lecture
                              </span>

                            </button>

                          )}

                        </td>

                      );

                    })}

                  </tr>

                ))}

              </tbody>

            </table>

          </div>

        </main>

      </div>

      {/* ================================
          ADD / EDIT MODAL
      ================================= */}

      {showModal && (

        <div
          className="timetable-modal-overlay"
          onClick={closeModal}
        >

          <div
            className="timetable-modal"
            onClick={(event) =>
              event.stopPropagation()
            }
          >

            {/* MODAL HEADER */}

            <div className="timetable-modal-header">

              <div>

                <h2>
                  {editingLectureId !== null
                    ? "Edit Lecture"
                    : "Add Lecture"}
                </h2>

                <p>
                  {editingLectureId !== null
                    ? `Update lecture for ${selectedBatch}.`
                    : `Add a lecture to ${selectedBatch}.`}
                </p>

              </div>

              <button
                type="button"
                className="timetable-modal-close"
                onClick={closeModal}
              >
                <X size={20} />
              </button>

            </div>

            {/* FORM */}

            <form
              onSubmit={
                handleSaveLecture
              }
            >

              {/* ERROR */}

              {errorMessage && (

                <div className="timetable-error">

                  <AlertTriangle
                    size={18}
                  />

                  <span>
                    {errorMessage}
                  </span>

                </div>

              )}

              {/* DAY */}

              <div className="timetable-form-group">

                <label>
                  Day
                </label>

                <select
                  value={
                    lectureForm.day
                  }
                  onChange={(event) =>
                    setLectureForm(
                      (previous) => ({
                        ...previous,
                        day:
                          event.target
                            .value,
                      })
                    )
                  }
                >

                  {days.map((day) => (
                    <option
                      key={day}
                      value={day}
                    >
                      {day}
                    </option>
                  ))}

                </select>

              </div>

              {/* TIME */}

              <div className="timetable-form-group">

                <label>
                  Time Slot
                </label>

                <select
                  value={
                    lectureForm.time
                  }
                  onChange={(event) =>
                    setLectureForm(
                      (previous) => ({
                        ...previous,
                        time:
                          event.target
                            .value,
                      })
                    )
                  }
                >

                  {timeSlots.map(
                    (time) => (
                      <option
                        key={time}
                        value={time}
                      >
                        {time}
                      </option>
                    )
                  )}

                </select>

              </div>

              {/* SUBJECT */}

              <div className="timetable-form-group">

                <label>
                  Subject
                </label>

                <select
                  value={
                    lectureForm.subject
                  }
                  onChange={(event) =>
                    handleSubjectChange(
                      event.target.value
                    )
                  }
                >

                  {subjectsForSelectedBatch.map(
                    (subject) => (
                      <option
                        key={subject}
                        value={subject}
                      >
                        {subject}
                      </option>
                    )
                  )}

                </select>

              </div>

              {/* FACULTY */}

              <div className="timetable-form-group">

                <label>
                  Faculty
                </label>

                <select
                  value={
                    lectureForm.faculty
                  }
                  onChange={(event) =>
                    setLectureForm(
                      (previous) => ({
                        ...previous,
                        faculty:
                          event.target
                            .value,
                      })
                    )
                  }
                >

                  {(
                    facultyBySubject[
                      lectureForm.subject
                    ] || []
                  ).map((faculty) => (
                    <option
                      key={faculty}
                      value={faculty}
                    >
                      {faculty}
                    </option>
                  ))}

                </select>

              </div>

              {/* CLASSROOM */}

              <div className="timetable-form-group">

                <label>
                  Classroom
                </label>

                <input
                  type="text"
                  placeholder="Example: Room 101"
                  value={
                    lectureForm.room
                  }
                  onChange={(event) =>
                    setLectureForm(
                      (previous) => ({
                        ...previous,
                        room:
                          event.target
                            .value,
                      })
                    )
                  }
                  required
                />

              </div>

              {/* BUTTONS */}

              <div className="timetable-modal-actions">

                <button
                  type="button"
                  className="timetable-cancel-btn"
                  onClick={closeModal}
                >
                  Cancel
                </button>

                <button
                  type="submit"
                  className="save-lecture-btn"
                >
                  {editingLectureId !== null
                    ? "Save Changes"
                    : "Add Lecture"}
                </button>

              </div>

            </form>

          </div>

        </div>

      )}

    </div>
  );
}

export default Timetable;