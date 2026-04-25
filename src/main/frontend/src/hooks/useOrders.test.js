import React from 'react';
import { act } from 'react';
import { createRoot } from 'react-dom/client';
import { useOrders } from './useOrders';
import { installFetchHandlers } from '../mocks/handlers';

describe('useOrders', () => {
  let container;
  let root;
  let latestHookValue;

  beforeEach(() => {
    installFetchHandlers();
    container = document.createElement('div');
    document.body.appendChild(container);
    root = createRoot(container);
  });

  afterEach(() => {
    jest.resetAllMocks();
    act(() => {
      root.unmount();
    });
    container.remove();
  });

  test('loads the first page and appends the next page on demand', async () => {
    function Harness() {
      latestHookValue = useOrders(1);
      return <div data-testid="count">{latestHookValue.orders.length}</div>;
    }

    await act(async () => {
      root.render(<Harness />);
      await Promise.resolve();
      await Promise.resolve();
    });

    expect(latestHookValue.orders).toHaveLength(25);

    await act(async () => {
      await latestHookValue.loadNextPage();
      await Promise.resolve();
    });

    expect(latestHookValue.orders).toHaveLength(35);
    expect(latestHookValue.hasMore).toBe(false);
  });
});
