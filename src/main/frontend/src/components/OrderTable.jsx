import React, { useDeferredValue, useMemo } from 'react';
import { useOrders } from '../hooks/useOrders';
import { useLiveOrdersActions } from '../context/GlobalStateContext';
import { createOrder } from '../services/orderService';
import { formatOrderDate } from '../utils/dateFormatter';

const VISIBLE_ROWS = 20;

const OrderTable = ({ userId, onOrderQueued }) => {
  const { orders, loading, error, hasMore, loadNextPage, prependOrder } = useOrders(userId);
  const deferredOrders = useDeferredValue(orders);
  const { appendLiveOrder } = useLiveOrdersActions();

  const visibleOrders = useMemo(
    () => deferredOrders.slice(Math.max(0, deferredOrders.length - VISIBLE_ROWS)),
    [deferredOrders]
  );

  const queueSampleLiveOrder = async () => {
    const payload = await createOrder(userId, 19.99);
    prependOrder(payload.order);
    appendLiveOrder(payload.order);
    if (onOrderQueued) {
      onOrderQueued(payload);
    }
  };

  return (
    <section className="panel">
      <div className="eyebrow">Orders</div>
      <h3>Recent order history</h3>
      <div className="toolbar-row">
        <p className="table-cell-muted">
          Rendering {visibleOrders.length} rows from {orders.length} loaded orders.
        </p>
        <button
          type="button"
          className="toolbar-button"
          onClick={queueSampleLiveOrder}
        >
          Queue sample live order
        </button>
      </div>

      {error && <p className="empty-state">{error}</p>}

      <table className="orders-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Date</th>
            <th>Amount</th>
          </tr>
        </thead>
        <tbody>
          {visibleOrders.map((order) => (
            <tr key={order.id}>
              <td>{order.id}</td>
              <td>{formatOrderDate(order.date)}</td>
              <td>${Number(order.amount).toFixed(2)}</td>
            </tr>
          ))}
          {visibleOrders.length === 0 && (
            <tr>
              <td colSpan="3" className="table-cell-muted">No order history loaded yet.</td>
            </tr>
          )}
        </tbody>
      </table>

      <div className="toolbar-row">
        <p className="table-cell-muted">{loading ? 'Fetching another page…' : 'Paged history keeps memory usage bounded.'}</p>
        <button
          type="button"
          className="toolbar-button"
          disabled={!hasMore || loading}
          onClick={loadNextPage}
        >
          {hasMore ? 'Load more orders' : 'All pages loaded'}
        </button>
      </div>
    </section>
  );
};

export default OrderTable;
