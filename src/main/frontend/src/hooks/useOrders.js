import { useEffect, useState, useTransition } from 'react';
import { fetchOrdersPage } from '../services/orderService';

const PAGE_SIZE = 25;

export const useOrders = (userId) => {
  const [orders, setOrders] = useState([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [isPending, startTransition] = useTransition();

  useEffect(() => {
    if (!userId) {
      return undefined;
    }

    const controller = new AbortController();
    setOrders([]);
    setPage(0);
    setHasMore(true);
    setLoading(true);
    setError('');

    fetchOrdersPage(userId, 0, PAGE_SIZE, controller.signal)
      .then((payload) => {
        startTransition(() => {
          setOrders(payload.items);
          setPage(payload.page);
          setHasMore(payload.hasMore);
          setLoading(false);
        });
      })
      .catch((fetchError) => {
        if (fetchError.name !== 'AbortError') {
          setError(fetchError.message || 'Unable to load order history');
          setLoading(false);
        }
      });

    return () => controller.abort();
  }, [userId]);

  const loadNextPage = () => {
    if (!hasMore || loading || !userId) {
      return Promise.resolve();
    }

    const nextPage = page + 1;
    setLoading(true);
    setError('');

    return fetchOrdersPage(userId, nextPage, PAGE_SIZE)
      .then((payload) => {
        startTransition(() => {
          setOrders((current) => [...current, ...payload.items]);
          setPage(payload.page);
          setHasMore(payload.hasMore);
          setLoading(false);
        });
      })
      .catch((fetchError) => {
        setError(fetchError.message || 'Unable to load additional orders');
        setLoading(false);
      });
  };

  const prependOrder = (order) => {
    startTransition(() => {
      setOrders((current) => [order, ...current]);
    });
  };

  return {
    orders,
    loading: loading || isPending,
    error,
    hasMore,
    loadNextPage,
    prependOrder
  };
};
