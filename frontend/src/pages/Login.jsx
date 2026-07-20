import "../styles/Login.css";
import { useState } from "react";

function Login() {
  const [showPassword, setShowPassword] = useState(false);

  return (
    <div className="login-container">
      <div className="login-card">

        <div className="logo-circle">
          AA
        </div>

        <h1>1729 ACHARYA ACADEMY</h1>

        <h2>Timetable Management System</h2>

        <p className="welcome">
          Sign in to access your timetable dashboard
        </p>

        <form>

          <label>Email Address</label>

          <input
            type="email"
            placeholder="Enter your email"
          />

          <label>Password</label>

          <div className="password-container">

            <input
              type={showPassword ? "text" : "password"}
              placeholder="Enter your password"
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

          <button type="submit" className="login-btn">
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