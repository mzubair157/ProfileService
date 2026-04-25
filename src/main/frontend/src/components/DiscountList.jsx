import React from 'react';

const DiscountList = ({ discounts }) => (
  <section className="panel">
    <div className="eyebrow">Discounts</div>
    <h3>Active campaigns</h3>
    <div className="stack-list">
      {discounts.map((discount) => (
        <article key={discount.code} className="list-item">
          <div>
            <strong>{discount.code}</strong>
            <p>{discount.eligibleUserCount} eligible users</p>
          </div>
          <span className="pill">{discount.percentage}% off</span>
        </article>
      ))}
      {discounts.length === 0 && <p className="empty-state">No active discounts are available right now.</p>}
    </div>
  </section>
);

export default DiscountList;
