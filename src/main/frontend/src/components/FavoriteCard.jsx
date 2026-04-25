import React, { useState } from 'react';

const FavoriteCard = ({ item, isBusy, isRemoving, onRemove, onUpdate }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [draftNote, setDraftNote] = useState(item.note ?? '');
  const [draftPriority, setDraftPriority] = useState(item.priorityLevel ?? 3);

  const handleSave = async () => {
    await onUpdate(item.productId, {
      note: draftNote,
      priorityLevel: Number(draftPriority)
    });
    setIsEditing(false);
  };

  return (
    <article className={`favorite-tile${isRemoving ? ' favorite-tile-removing' : ''}`}>
      <div className="favorite-content">
        <span className="tile-label">Product</span>
        <strong>{item.productId}</strong>
        {isEditing ? (
          <div className="favorite-edit-stack">
            <input
              className="profile-input favorite-note-input"
              value={draftNote}
              onChange={(event) => setDraftNote(event.target.value)}
              placeholder="Add a note"
            />
            <select
              className="favorite-priority-select"
              value={draftPriority}
              onChange={(event) => setDraftPriority(event.target.value)}
            >
              {[1, 2, 3, 4, 5].map((level) => (
                <option key={level} value={level}>Priority {level}</option>
              ))}
            </select>
          </div>
        ) : (
          <>
            <p className="favorite-note">{item.note || 'No note yet.'}</p>
            <p className="favorite-priority">Priority {item.priorityLevel ?? 3}</p>
          </>
        )}
      </div>
      <div className="favorite-actions">
        {isBusy ? (
          <span className="favorite-spinner" aria-label="Updating favorite">…</span>
        ) : (
          <>
            {isEditing ? (
              <>
                <button type="button" className="icon-button" onClick={() => setIsEditing(false)} aria-label={`Cancel edit for product ${item.productId}`}>
                  ↺
                </button>
                <button type="button" className="icon-button" onClick={handleSave} aria-label={`Save product ${item.productId}`}>
                  ✓
                </button>
              </>
            ) : (
              <>
                <button type="button" className="icon-button" onClick={() => setIsEditing(true)} aria-label={`Edit product ${item.productId}`}>
                  ✎
                </button>
                <button type="button" className="icon-button" onClick={() => onRemove(item.productId)} aria-label={`Remove product ${item.productId}`}>
                  ×
                </button>
              </>
            )}
          </>
        )}
      </div>
    </article>
  );
};

export default FavoriteCard;
