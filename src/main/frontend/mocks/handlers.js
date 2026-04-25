export const profileFixture = {
  id: 1,
  username: 'Ada',
  email: 'ada@example.com',
  region: 'EU',
  loyaltyAccountId: 8,
  loyaltyBalance: 420
};

export const discountsFixture = [
  { code: 'FLASH20', percentage: 20, eligibleUserCount: 14 }
];

export const favoritesFixture = {
  userId: 1,
  productIds: [11, 17, 29]
};

export const recommendationsFixture = [
  { id: 7, name: 'Noise Cancelling Headphones', rationale: 'Affinity match', score: 0.92 }
];

export const ordersPageOneFixture = {
  items: Array.from({ length: 25 }, (_, index) => ({
    id: index + 1,
    date: '2026-04-20T10:15:00Z',
    amount: 10 + index
  })),
  page: 0,
  hasMore: true
};

export const ordersPageTwoFixture = {
  items: Array.from({ length: 10 }, (_, index) => ({
    id: index + 26,
    date: '2026-04-21T10:15:00Z',
    amount: 40 + index
  })),
  page: 1,
  hasMore: false
};

export const installFetchHandlers = () => {
  global.fetch = jest.fn((url) => {
    const requestUrl = String(url);

    if (requestUrl.includes('/profiles/1')) {
      return Promise.resolve({ ok: true, json: async () => profileFixture });
    }
    if (requestUrl.includes('/discounts/active')) {
      return Promise.resolve({ ok: true, json: async () => discountsFixture });
    }
    if (requestUrl.includes('/favorites/1')) {
      return Promise.resolve({ ok: true, json: async () => favoritesFixture });
    }
    if (requestUrl.includes('/recommendations/users/1')) {
      return Promise.resolve({ ok: true, json: async () => recommendationsFixture });
    }
    if (requestUrl.includes('/api/v1/orders/history?user=1&page=0&size=25')) {
      return Promise.resolve({ ok: true, json: async () => ordersPageOneFixture });
    }
    if (requestUrl.includes('/api/v1/orders/history?user=1&page=1&size=25')) {
      return Promise.resolve({ ok: true, json: async () => ordersPageTwoFixture });
    }

    return Promise.resolve({ ok: false, status: 404, json: async () => ({}) });
  });
};
