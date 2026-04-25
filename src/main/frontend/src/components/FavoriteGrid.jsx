import React, { useState } from 'react';
import FavoriteCard from './FavoriteCard';

const FavoriteGrid = ({ favorites, onAdd, onRemove, onUpdate }) => {
  const [productIdInput, setProductIdInput] = useState('');
  const [busyProductIds, setBusyProductIds] = useState([]);
  const [adding, setAdding] = useState(false);
  const [error, setError] = useState('');

  const markBusy = (productId, isBusy) => {
    setBusyProductIds((current) => {
      if (isBusy) {
        return current.includes(productId) ? current : [...current, productId];
      }
      return current.filter((value) => value !== productId);
    });
  };

  const handleAdd = async (event) => {
    event.preventDefault();
    if (!onAdd) {
      return;
    }

    const nextProductId = Number(productIdInput);
    if (!Number.isFinite(nextProductId) || nextProductId <= 0) {
      setError('Enter a valid product id.');
      return;
    }

    try {
      setAdding(true);
      setError('');
      await onAdd(nextProductId);
      setProductIdInput('');
    } catch (addError) {
      setError(addError.message || 'Unable to add favorite');
    } finally {
      setAdding(false);
    }
  };

  const handleUpdate = async (productId, payload) => {
    if (!onUpdate) {
      return;
    }
    try {
      markBusy(productId, true);
      setError('');
      await onUpdate(productId, payload);
    } catch (updateError) {
      setError(updateError.message || 'Unable to update favorite');
    } finally {
      markBusy(productId, false);
    }
  };

  const handleRemove = async (productId) => {
    if (!onRemove) {
      return;
    }
    try {
      markBusy(productId, true);
      setError('');
      await onRemove(productId);
    } catch (removeError) {
      setError(removeError.message || 'Unable to remove favorite');
      markBusy(productId, false);
    }
  };

  return (
    <section className="panel">
      <div className="eyebrow">Favorites</div>
      <h3>Saved products</h3>
      <form className="favorite-add-row" onSubmit={handleAdd}>
        <input
          className="profile-input favorite-quick-add"
          type="number"
          min="1"
          value={productIdInput}
          onChange={(event) => setProductIdInput(event.target.value)}
          placeholder="Quick add product id"
        />
        <button type="submit" className="toolbar-button toolbar-button-accent" disabled={adding}>
          {adding ? 'Adding…' : 'Add product'}
        </button>
      </form>
      {error && <p className="empty-state">{error}</p>}
      <div className="favorite-grid">
        {favorites.items.map((item) => (
          <FavoriteCard
            key={item.productId}
            item={item}
            isBusy={busyProductIds.includes(item.productId)}
            isRemoving={!favorites.productIds.includes(item.productId)}
            onRemove={handleRemove}
            onUpdate={handleUpdate}
          />
        ))}
        {favorites.items.length === 0 && <p className="empty-state">No favorites have been saved yet.</p>}
      </div>
    </section>
  );
};

export default FavoriteGrid;
