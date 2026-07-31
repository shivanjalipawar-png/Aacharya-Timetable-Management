import { createContext, useContext, useState } from "react";

const TimetableContext = createContext(null);

export function TimetableProvider({ children }) {
  const [timetable, setTimetable] = useState([
    {
      id: 1,
      batch: "JEE Batch 1",
      day: "Monday",
      time: "08:00 AM - 09:00 AM",
      subject: "Physics",
      faculty: "Faculty 1",
      room: "Room 101",
    },
    {
      id: 2,
      batch: "JEE Batch 1",
      day: "Monday",
      time: "09:00 AM - 10:00 AM",
      subject: "Chemistry",
      faculty: "Faculty 2",
      room: "Room 101",
    },
    {
      id: 3,
      batch: "JEE Batch 1",
      day: "Tuesday",
      time: "08:00 AM - 09:00 AM",
      subject: "Mathematics",
      faculty: "Faculty 3",
      room: "Room 101",
    },
    {
      id: 4,
      batch: "NEET Batch 1",
      day: "Monday",
      time: "08:00 AM - 09:00 AM",
      subject: "Biology",
      faculty: "Faculty 4",
      room: "Room 201",
    },
  ]);

  const addLecture = (lecture) => {
    const newLecture = {
      ...lecture,
      id: Date.now(),
    };

    setTimetable((previous) => [
      ...previous,
      newLecture,
    ]);
  };

  const updateLecture = (id, updatedLecture) => {
    setTimetable((previous) =>
      previous.map((lecture) =>
        lecture.id === id
          ? {
              ...lecture,
              ...updatedLecture,
            }
          : lecture
      )
    );
  };

  const deleteLecture = (id) => {
    setTimetable((previous) =>
      previous.filter(
        (lecture) => lecture.id !== id
      )
    );
  };

  return (
    <TimetableContext.Provider
      value={{
        timetable,
        addLecture,
        updateLecture,
        deleteLecture,
      }}
    >
      {children}
    </TimetableContext.Provider>
  );
}

export function useTimetable() {
  const context = useContext(TimetableContext);

  if (!context) {
    throw new Error(
      "useTimetable must be used inside TimetableProvider"
    );
  }

  return context;
}