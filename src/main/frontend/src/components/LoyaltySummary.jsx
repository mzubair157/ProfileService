import React from 'react';

const LoyaltySummary = ({ accountId, balance }) => (
  <section className="panel loyalty-panel">
    <div className="eyebrow">Loyalty</div>
    <h3>Reward balance</h3>
    <div className="metric-row">
      <span className="metric">{balance ?? 0}</span>
      <span className="metric-label">points</span>
    </div>
    <p className="panel-copy">Account {accountId ?? 'Pending'} is ready for redemption and tier evaluation.</p>
  </section>
);

export default LoyaltySummary;
