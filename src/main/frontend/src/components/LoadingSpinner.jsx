import React from 'react';

const LoadingSpinner = ({ label = 'Loading data' }) => (
  <section className="panel status-card" aria-live="polite">
    <div className="spinner" aria-hidden="true" />
    <div className="eyebrow">Loading</div>
    <h2>{label}</h2>
    <p>Parallel requests are in progress.</p>
  </section>
);

export default LoadingSpinner;
