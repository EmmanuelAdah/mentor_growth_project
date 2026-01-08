import React from 'react'
import "../styles/LandingPage.css";

function LandingPage() {


  return (
    <div className="landing">
      {/* Navbar */}
      <header className="navbar">
        <div className="logo">
          <span className="logo-bold">Mentor</span>
          <span className="logo-bold">GROWTH</span>
        </div>

        <nav className="nav-links">
          <a href="#home">Home</a>
          <a href="#programs">Mentors</a>
          <a href="/grow">Grow with us</a>
          <a href="#stories">Success Stories</a>
        </nav>

        <div className="nav-actions">
          <a href="#signin" className="signin">Sign up</a>
          <button className="donate-btn">Sign in</button>
        </div>
      </header>

      <section className="hero">
        <div className="hero-text">
          <span className="hero-tag">GROW WITHOUT LIMITS</span>

          <h1>
            Join a community <br />
            of learners helping <br />
            each other grow.
          </h1>

          <p>
            Make connections, take action and move forward with a community
            of women dedicated to mutual growth.
          </p>

          <button className="cta-btn">Learn More</button>
        </div>

        <div className="hero-image">
          <img src="../assets/images/mentor-landing.jpg"
            alt="landing image"
          />
        </div>
      </section>
    </div>
  );
}

export default LandingPage;