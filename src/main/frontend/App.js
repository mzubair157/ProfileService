import React, { startTransition, useEffect, useState } from 'react';
import { getDiscounts, getFavorites, getRecommendations } from './api/apiClient';
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

function App() {
  const [dashboard, setDashboard] = useState({ discounts: [], favorites: { userId, productIds: [] }, recommendations: [] });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { profile, loading: profileLoading, error: profileError } = useProfile(userId);
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
          setDashboard({ discounts, favorites, recommendations });
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
                <ProfileView profile={profile} />
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
                <FavoriteGrid favorites={dashboard.favorites} />
                <OrderTable userId={userId} />
              </>
            </ErrorBoundaries>
          )}
        </div>
      </main>
    </>
  );
}

export default App;
