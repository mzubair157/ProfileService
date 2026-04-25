import React from 'react';
import LoyaltySummary from './LoyaltySummary';

const ProfileView = ({ profile }) => {
  if (!profile) {
    return null;
  }

  return (
    <section className="hero-card">
      <div className="hero-copy">
        <div className="eyebrow">Profile</div>
        <h1>{profile.username}</h1>
        <p>{profile.email}</p>
        <div className="profile-meta">
          <span>{profile.region}</span>
          <span>User #{profile.id}</span>
        </div>
      </div>
      <LoyaltySummary
        accountId={profile.loyaltyAccountId}
        balance={profile.loyaltyBalance}
      />
    </section>
  );
};

export default ProfileView;
