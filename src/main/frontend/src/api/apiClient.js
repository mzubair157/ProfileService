const parseJson = async (response) => {
  if (!response.ok) {
    throw new Error(`Request failed with status ${response.status}`);
  }
  if (response.status === 204) {
    return null;
  }
  return response.json();
};

const createIdempotencyKey = (prefix) => {
  if (typeof crypto !== 'undefined' && typeof crypto.randomUUID === 'function') {
    return `${prefix}-${crypto.randomUUID()}`;
  }
  return `${prefix}-${Date.now()}-${Math.random().toString(36).slice(2)}`;
};

export const getProfile = async (userId, signal) => {
  const response = await fetch(`/profiles/${userId}`, { signal });
  return parseJson(response);
};

export const updateProfile = async (userId, payload) => {
  const response = await fetch(`/api/v1/profiles/${userId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(payload)
  });
  return parseJson(response);
};

export const getDiscounts = async (signal) => {
  const response = await fetch('/discounts/active', { signal });
  return parseJson(response);
};

export const getFavorites = async (userId, signal) => {
  const response = await fetch(`/favorites/${userId}`, { signal });
  return parseJson(response);
};

export const addFavorite = async (userId, productId) => {
  const response = await fetch(`/api/v1/favorites/${userId}/${productId}`, {
    method: 'POST',
    headers: {
      'Idempotency-Key': createIdempotencyKey(`favorite-add-${userId}-${productId}`)
    }
  });
  return parseJson(response);
};

export const updateFavorite = async (userId, productId, payload) => {
  const response = await fetch(`/api/v1/favorites/${userId}/${productId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Idempotency-Key': createIdempotencyKey(`favorite-update-${userId}-${productId}`)
    },
    body: JSON.stringify(payload)
  });
  return parseJson(response);
};

export const removeFavorite = async (userId, productId) => {
  const response = await fetch(`/api/v1/favorites/${userId}/${productId}`, {
    method: 'DELETE',
    headers: {
      'Idempotency-Key': createIdempotencyKey(`favorite-remove-${userId}-${productId}`)
    }
  });
  return parseJson(response);
};

export const getRecommendations = async (userId, signal) => {
  const response = await fetch(`/recommendations/users/${userId}`, { signal });
  return parseJson(response);
};

export const loadDashboard = async (userId, signal) => {
  const [profile, discounts, favorites, recommendations] = await Promise.all([
    getProfile(userId, signal),
    getDiscounts(signal),
    getFavorites(userId, signal),
    getRecommendations(userId, signal)
  ]);

  return { profile, discounts, favorites, recommendations };
};
