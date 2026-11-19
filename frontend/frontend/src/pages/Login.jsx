import academyLogo from "../assets/acharya-logo.jpg";
import "../styles/Login.css";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

function Login() {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);

  const handleLogin = (e) => {
    e.preventDefault(); // Prevent page refresh
    navigate("/dashboard");
  };

  return (
    <div className="login-container">
      <div className="login-card">

       <div className="logo-circle">
  <img src="/images/acharya-logo.png" alt="1729 Acharya Academy Logo" />
</div>

        <h1>1729 ACHARYA ACADEMY</h1>

        <h2>Timetable Management System</h2>

        <p className="welcome">
          Sign in to access your timetable dashboard
        </p>

        <form onSubmit={handleLogin}>

          <label>Username</label>

        <input
        type="text"
        placeholder="Enter your username"
       required
          />

          <label>Password</label>

          <div className="password-container">

            <input
              type={showPassword ? "text" : "password"}
              placeholder="Enter your password"
             // required
            />

            <button
  type="button"
  className="show-password"
  onClick={() => setShowPassword(!showPassword)}
  aria-label={showPassword ? "Hide password" : "Show password"}
  title={showPassword ? "Hide password" : "Show password"}
>
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="20"
    height="20"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
  >
    {showPassword ? (
      <>
        <path d="M3 3l18 18" />
        <path d="M10.6 10.6a2 2 0 002.8 2.8" />
        <path d="M9.9 5.2A11 11 0 0121 12a11 11 0 01-3.2 4.2" />
        <path d="M6.2 6.2A11 11 0 003 12a11 11 0 0018 0" />
      </>
    ) : (
      <>
        <path d="M2 12s3.6-7 10-7 10 7 10 7-3.6 7-10 7-10-7-10-7z" />
        <circle cx="12" cy="12" r="3" />
      </>
    )}
  </svg>
</button>

          </div>

          <div className="extra">

            <label className="remember">
              <input type="checkbox" />
              Remember Me
            </label>

            <a href="#">Forgot Password?</a>

          </div>

          <button
            type="submit"
            className="login-btn"
          >
            LOGIN
          </button>

        </form>

        <p className="footer">
          © 1729 Acharya Academy
        </p>

      </div>
    </div>
  );
}

export default Login;