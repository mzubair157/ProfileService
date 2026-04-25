import React, { useEffect, useState } from 'react';
import LoyaltySummary from './LoyaltySummary';

const ProfileView = ({ profile, onSave }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [draft, setDraft] = useState({ username: '', email: '' });
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (profile) {
      setDraft({ username: profile.username ?? '', email: profile.email ?? '' });
    }
  }, [profile]);

  if (!profile) {
    return null;
  }

  const handleSave = async () => {
    if (!onSave) {
      setIsEditing(false);
      return;
    }

    try {
      setSaving(true);
      setError('');
      await onSave({ username: draft.username, email: draft.email });
      setIsEditing(false);
    } catch (saveError) {
      setError(saveError.message || 'Unable to save profile');
    } finally {
      setSaving(false);
    }
  };

  return (
    <section className="hero-card">
      <div className="hero-copy">
        <div className="eyebrow">Profile</div>
        {isEditing ? (
          <div className="editor-stack">
            <input
              className="profile-input profile-input-title"
              value={draft.username}
              onChange={(event) => setDraft((current) => ({ ...current, username: event.target.value }))}
              placeholder="Username"
            />
            <input
              className="profile-input"
              value={draft.email}
              onChange={(event) => setDraft((current) => ({ ...current, email: event.target.value }))}
              placeholder="Email"
              type="email"
            />
          </div>
        ) : (
          <>
            <h1>{profile.username}</h1>
            <p>{profile.email}</p>
          </>
        )}
        <div className="profile-meta">
          <span>{profile.region}</span>
          <span>User #{profile.id}</span>
          <button
            type="button"
            className="toolbar-button"
            onClick={() => {
              setDraft({ username: profile.username ?? '', email: profile.email ?? '' });
              setIsEditing((current) => !current);
              setError('');
            }}
          >
            {isEditing ? 'Cancel' : 'Edit profile'}
          </button>
          {isEditing && (
            <button
              type="button"
              className="toolbar-button toolbar-button-accent"
              disabled={saving}
              onClick={handleSave}
            >
              {saving ? 'Saving…' : 'Save profile'}
            </button>
          )}
        </div>
        {error && <p className="empty-state">{error}</p>}
      </div>
      <LoyaltySummary
        accountId={profile.loyaltyAccountId}
        balance={profile.loyaltyBalance}
      />
    </section>
  );
};

export default ProfileView;
