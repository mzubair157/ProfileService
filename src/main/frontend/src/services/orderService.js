const parseJson = async (response) => {
  if (!response.ok) {
    throw new Error(`Request failed with status ${response.status}`);
  }
  return response.json();
};

export const fetchOrdersPage = async (userId, page = 0, pageSize = 25, signal) => {
  const response = await fetch(
    `/api/v1/orders/history?user=${userId}&page=${page}&size=${pageSize}`,
    { signal }
  );
  const payload = await parseJson(response);

  return {
    items: payload.items ?? [],
    page: payload.page ?? page,
    hasMore: Boolean(payload.hasMore)
  };
};

export const createOrder = async (userId, amount) => {
  const response = await fetch('/api/v1/orders', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ userId, amount })
  });

  return parseJson(response);
};
