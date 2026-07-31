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
          <img src={academyLogo} alt="1729 Acharya Academy Logo" />
        </div>

        <h1>1729 ACHARYA ACADEMY</h1>

        <h2>Timetable Management System</h2>

        <p className="welcome">
          Sign in to access your timetable dashboard
        </p>

        <form onSubmit={handleLogin}>

          <label>Email Address</label>

          <input
            type="email"
            placeholder="Enter your email"
           // required
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
            >
              {showPassword ? "Hide" : "Show"}
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