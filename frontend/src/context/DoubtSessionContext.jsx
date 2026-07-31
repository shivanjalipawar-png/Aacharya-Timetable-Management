import { createContext, useContext, useState } from "react";

const DoubtSessionContext = createContext(null);

export function DoubtSessionProvider({ children }) {
  const [doubtSessions, setDoubtSessions] = useState([]);

  // Mark a free faculty slot as available for doubts
  const addDoubtSession = (session) => {
    const alreadyExists = doubtSessions.some(
      (item) =>
        item.faculty === session.faculty &&
        item.day === session.day &&
        item.time === session.time
    );

    if (alreadyExists) {
      return;
    }

    setDoubtSessions((previous) => [
      ...previous,
      {
        id: Date.now(),
        ...session,
      },
    ]);
  };

  // Remove doubt-session availability
  const removeDoubtSession = (id) => {
    setDoubtSessions((previous) =>
      previous.filter((session) => session.id !== id)
    );
  };

  // Check whether a particular slot is marked
  const isDoubtSession = (faculty, day, time) => {
    return doubtSessions.some(
      (session) =>
        session.faculty === faculty &&
        session.day === day &&
        session.time === time
    );
  };

  return (
    <DoubtSessionContext.Provider
      value={{
        doubtSessions,
        addDoubtSession,
        removeDoubtSession,
        isDoubtSession,
      }}
    >
      {children}
    </DoubtSessionContext.Provider>
  );
}

export function useDoubtSessions() {
  const context = useContext(DoubtSessionContext);

  if (!context) {
    throw new Error(
      "useDoubtSessions must be used inside DoubtSessionProvider"
    );
  }

  return context;
}