import React from 'react';

const RecommendationCard = ({ items }) => (
  <section className="panel recommendation-panel">
    <div className="eyebrow">AI Offers</div>
    <h3>Suggested for this profile</h3>
    <div className="stack-list">
      {items.map((item) => (
        <article key={item.id} className="list-item">
          <div>
            <strong>{item.name}</strong>
            <p>{item.rationale}</p>
          </div>
          <span className="pill score-pill">{Math.round(item.score * 100)}%</span>
        </article>
      ))}
      {items.length === 0 && <p className="empty-state">Recommendations are loading from the fallback catalog.</p>}
    </div>
  </section>
);

export default RecommendationCard;
