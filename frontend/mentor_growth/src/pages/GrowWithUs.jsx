import "../styles/GrowWithUs.css";

export default function GrowWithUs() {

  return (
    <section className="grow">
      <div className="grow-container">
        {/* Left Content */}
        <div className="grow-text">
          <h2>Grow with us</h2>
          <p>
            Discover how Woman to Woman Mentoring helps women move beyond
            limits by connecting them with mentors, community and possibilities.
          </p>
          <button className="grow-btn">Get Started</button>
        </div>

        {/* Right Mentors */}
        <div className="grow-mentors">
          <div className="mentor-card">
            <img src="../assets/images/peer-mentoring.jpg" alt="Jane Doe" />
            <h4>Jane Doe</h4>
            <span>Position held</span>
          </div>

          <div className="mentor-card">
            <img src="/mentor-2.jpg" alt="Joy Doe" />
            <h4>Joy Doe</h4>
            <span>Position held</span>
          </div>
        </div>
      </div>

      {/* Stats */}
      <div className="grow-stats">
        <div className="stat">
          <h3>100</h3>
          <p>Mentors</p>
        </div>
        <div className="stat">
          <h3>2347</h3>
          <p>Mentees</p>
        </div>
        <div className="stat">
          <h3>200</h3>
          <p>Chapters</p>
        </div>
      </div>
    </section>
  );
}
