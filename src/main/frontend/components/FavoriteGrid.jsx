import React from 'react';

const FavoriteGrid = ({ favorites }) => (
  <section className="panel">
    <div className="eyebrow">Favorites</div>
    <h3>Saved products</h3>
    <div className="favorite-grid">
      {favorites.productIds.map((productId) => (
        <div key={productId} className="favorite-tile">
          <span className="tile-label">Product</span>
          <strong>{productId}</strong>
        </div>
      ))}
      {favorites.productIds.length === 0 && <p className="empty-state">No favorites have been saved yet.</p>}
    </div>
  </section>
);

export default FavoriteGrid;
