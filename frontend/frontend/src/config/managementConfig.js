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

const subjects = [
  "Physics",
  "Chemistry",
  "Mathematics",
  "Biology",
];

const faculty = [
  "Faculty 1",
  "Faculty 2",
  "Faculty 3",
  "Faculty 4",
];

export const managementConfig = {
  students: {
    title: "Students",
    description: "Manage student records and batch assignments.",
    fields: [
      { name: "name", label: "Student Name", required: true },
      { name: "email", label: "Email", type: "email", required: true },
      { name: "phone", label: "Phone Number", required: true },
      { name: "batch", label: "Batch", type: "select", options: batches },
    ],
    columns: ["name", "email", "phone", "batch"],
    initialData: [
      {
        id: 1,
        name: "Student 1",
        email: "student1@example.com",
        phone: "9876543210",
        batch: "JEE Batch 1",
      },
      {
        id: 2,
        name: "Student 2",
        email: "student2@example.com",
        phone: "9876543211",
        batch: "NEET Batch 1",
      },
    ],
  },

  batches: {
    title: "Batches",
    description: "Manage academy batches and their subject groups.",
    fields: [
      { name: "name", label: "Batch Name", required: true },
      {
        name: "stream",
        label: "Stream",
        type: "select",
        options: ["JEE", "NEET", "CET", "10th CBSE", "10th SSC"],
      },
      { name: "room", label: "Room Number", required: true },
      { name: "students", label: "Student Capacity", type: "number", required: true },
    ],
    columns: ["name", "stream", "room", "students"],
    initialData: batches.map((name, index) => ({
      id: index + 1,
      name,
      stream: name.startsWith("JEE")
        ? "JEE"
        : name.startsWith("NEET")
        ? "NEET"
        : name.startsWith("CET")
        ? "CET"
        : name,
      room: `Room ${101 + index}`,
      students: "40",
    })),
  },

  subjects: {
    title: "Subjects",
    description: "Manage subjects and assign faculty members.",
    fields: [
      {
        name: "name",
        label: "Subject",
        type: "select",
        options: subjects,
      },
      {
        name: "batch",
        label: "Batch",
        type: "select",
        options: batches,
      },
      {
        name: "faculty",
        label: "Faculty",
        type: "select",
        options: faculty,
      },
    ],
    columns: ["name", "batch", "faculty"],
    initialData: [
      { id: 1, name: "Physics", batch: "JEE Batch 1", faculty: "Faculty 1" },
      { id: 2, name: "Chemistry", batch: "NEET Batch 1", faculty: "Faculty 2" },
      { id: 3, name: "Mathematics", batch: "CET Batch 1", faculty: "Faculty 3" },
      { id: 4, name: "Biology", batch: "NEET Batch 1", faculty: "Faculty 4" },
    ],
  },

  timetable: {
    title: "Timetable",
    description: "Schedule classes by batch, subject, faculty and room.",
    fields: [
      { name: "day", label: "Day", type: "select", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"] },
      { name: "batch", label: "Batch", type: "select", options: batches },
      { name: "subject", label: "Subject", type: "select", options: subjects },
      { name: "faculty", label: "Faculty", type: "select", options: faculty },
      { name: "time", label: "Time (e.g. 10:00 AM - 11:00 AM)", required: true },
      { name: "room", label: "Room", required: true },
    ],
    columns: ["day", "time", "batch", "subject", "faculty", "room"],
    initialData: [
      { id: 1, day: "Monday", time: "08:00 AM - 09:00 AM", batch: "JEE Batch 1", subject: "Physics", faculty: "Faculty 1", room: "Room 101" },
      { id: 2, day: "Monday", time: "09:00 AM - 10:00 AM", batch: "NEET Batch 1", subject: "Biology", faculty: "Faculty 2", room: "Room 203" },
      { id: 3, day: "Tuesday", time: "10:00 AM - 11:00 AM", batch: "CET Batch 2", subject: "Chemistry", faculty: "Faculty 3", room: "Room 105" },
    ],
  },

  notices: {
    title: "Notices",
    description: "Publish and manage announcements for students and faculty.",
    fields: [
      { name: "title", label: "Notice Title", required: true },
      { name: "audience", label: "Audience", type: "select", options: ["All", "Students", "Teachers", "JEE", "NEET", "CET", "10th"] },
      { name: "date", label: "Date", type: "date", required: true },
      { name: "message", label: "Notice Message", type: "textarea", required: true },
    ],
    columns: ["title", "audience", "date", "message"],
    initialData: [
      { id: 1, title: "Weekly Test", audience: "Students", date: "2026-10-30", message: "Please check with your faculty for the test schedule." },
      { id: 2, title: "Faculty Meeting", audience: "Teachers", date: "2026-10-31", message: "Faculty meeting details will be shared by the administration." },
    ],
  },
};