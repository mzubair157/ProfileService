import React from 'react';
import { act, renderHook, waitFor } from '@testing-library/react';
import { useOrders } from './useOrders';
import { installFetchHandlers } from '../mocks/handlers';

describe('useOrders', () => {
  beforeEach(() => {
    installFetchHandlers();
  });

  afterEach(() => {
    jest.resetAllMocks();
  });

  test('loads the first page and appends the next page on demand', async () => {
    const { result } = renderHook(() => useOrders(1));

    await waitFor(() => {
      expect(result.current.orders).toHaveLength(25);
    });

    await act(async () => {
      await result.current.loadNextPage();
    });

    expect(result.current.orders).toHaveLength(35);
    expect(result.current.hasMore).toBe(false);
  });
});
