import React, { startTransition, useEffect, useState } from 'react';
import { addFavorite, getDiscounts, getFavorites, getRecommendations, removeFavorite, updateFavorite } from './api/apiClient';
import ErrorBoundaries from './components/ErrorBoundaries';
import LoadingSpinner from './components/LoadingSpinner';
import OrderTable from './components/OrderTable';
import DiscountList from './components/DiscountList';
import FavoriteGrid from './components/FavoriteGrid';
import ProfileView from './components/ProfileView';
import RecommendationCard from './components/RecommendationCard';
import { useCurrentUserState, useLiveOrdersState } from './context/GlobalStateContext';
import { useProfile } from './hooks/useProfile';
import { dashboardCss } from './styles/StyledComponents';

const userId = 1;
const normalizeFavorites = (favorites) => ({
  userId: favorites?.userId ?? userId,
  items: favorites?.items ?? (favorites?.productIds ?? []).map((productId) => ({
    productId,
    note: '',
    priorityLevel: 3
  })),
  productIds: favorites?.productIds ?? (favorites?.items ?? []).map((item) => item.productId)
});

function App() {
  const [dashboard, setDashboard] = useState({
    discounts: [],
    favorites: { userId, productIds: [], items: [] },
    recommendations: []
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { profile, loading: profileLoading, error: profileError, saveProfile, patchProfile } = useProfile(userId);
  const currentUser = useCurrentUserState();
  const liveOrders = useLiveOrdersState();

  useEffect(() => {
    const controller = new AbortController();
    setLoading(true);
    setError('');

    Promise.all([
      getDiscounts(controller.signal),
      getFavorites(userId, controller.signal),
      getRecommendations(userId, controller.signal)
    ])
      .then((payload) => {
        startTransition(() => {
          const [discounts, favorites, recommendations] = payload;
          setDashboard({ discounts, favorites: normalizeFavorites(favorites), recommendations });
          setLoading(false);
        });
      })
      .catch((fetchError) => {
        if (fetchError.name !== 'AbortError') {
          setError(fetchError.message || 'Unable to load dashboard');
          setLoading(false);
        }
      });

    return () => controller.abort();
  }, []);

  const setFavorites = (updater) => {
    setDashboard((current) => {
      const nextFavorites = typeof updater === 'function'
        ? updater(current.favorites)
        : updater;
      return {
        ...current,
        favorites: normalizeFavorites(nextFavorites)
      };
    });
  };

  const handleFavoriteAdd = async (productId) => {
    const created = await addFavorite(userId, productId);
    setFavorites((current) => {
      if (current.productIds.includes(created.productId)) {
        return current;
      }

      return {
        ...current,
        productIds: [...current.productIds, created.productId].sort((left, right) => left - right),
        items: [...current.items, created].sort((left, right) => left.productId - right.productId)
      };
    });
  };

  const handleFavoriteRemove = async (productId) => {
    const previousFavorites = dashboard.favorites;
    setFavorites((current) => ({
      ...current,
      productIds: current.productIds.filter((id) => id !== productId),
      items: current.items.filter((item) => item.productId !== productId)
    }));

    try {
      await removeFavorite(userId, productId);
    } catch (removeError) {
      setFavorites(previousFavorites);
      throw removeError;
    }
  };

  const handleFavoriteUpdate = async (productId, payload) => {
    const updated = await updateFavorite(userId, productId, payload);
    setFavorites((current) => ({
      ...current,
      items: current.items.map((item) => item.productId === productId ? updated : item)
    }));
  };

  const handleOrderQueued = (payload) => {
    if (payload.loyaltyBalance != null) {
      patchProfile((current) => ({
        ...current,
        loyaltyBalance: payload.loyaltyBalance
      }));
    }
  };

  return (
    <>
      <style>{dashboardCss}</style>
      <main className="dashboard-shell">
        <div className="dashboard-frame">
          {(loading || profileLoading) && <LoadingSpinner label="Building the customer snapshot" />}

          {!(loading || profileLoading) && (error || profileError) && (
            <section className="panel status-card">
              <div className="eyebrow">Error</div>
              <h2>Dashboard unavailable</h2>
              <p>{error || profileError}</p>
            </section>
          )}

          {!(loading || profileLoading) && !error && !profileError && profile && (
            <ErrorBoundaries>
              <>
                <ProfileView profile={profile} onSave={saveProfile} />
                <section className="panel">
                  <div className="eyebrow">Live Feed</div>
                  <h3>Buffered activity</h3>
                  <p className="panel-copy">
                    Current user: {currentUser?.username || 'Unknown'}.
                    Tracking {liveOrders.length} buffered live order updates without rerendering the full app tree.
                  </p>
                  <div className="live-strip">
                    {liveOrders.slice(0, 3).map((order) => (
                      <article key={order.id} className="live-card">
                        <strong>{order.id}</strong>
                        <p>${Number(order.amount).toFixed(2)} at {new Date(order.date).toLocaleTimeString()}</p>
                      </article>
                    ))}
                    {liveOrders.length === 0 && (
                      <p className="empty-state">No live order updates have been queued yet.</p>
                    )}
                  </div>
                </section>
                <section className="grid-two">
                  <DiscountList discounts={dashboard.discounts} />
                  <RecommendationCard items={dashboard.recommendations} />
                </section>
                <FavoriteGrid
                  favorites={dashboard.favorites}
                  onAdd={handleFavoriteAdd}
                  onRemove={handleFavoriteRemove}
                  onUpdate={handleFavoriteUpdate}
                />
                <OrderTable userId={userId} onOrderQueued={handleOrderQueued} />
              </>
            </ErrorBoundaries>
          )}
        </div>
      </main>
    </>
  );
}

export default App;
