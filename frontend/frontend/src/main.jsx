import { StrictMode } from "react";
import { createRoot } from "react-dom/client";

import "./index.css";

import App from "./App.jsx";

import {
  AuthProvider,
} from "./context/AuthContext.jsx";

import {
  TimetableProvider,
} from "./context/TimetableContext.jsx";

import {
  DoubtSessionProvider,
} from "./context/DoubtSessionContext.jsx";

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <AuthProvider>
      <TimetableProvider>
        <DoubtSessionProvider>
          <App />
        </DoubtSessionProvider>
      </TimetableProvider>
    </AuthProvider>
  </StrictMode>
);