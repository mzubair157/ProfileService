const parseJson = async (response) => {
  if (!response.ok) {
    throw new Error(`Request failed with status ${response.status}`);
  }
  return response.json();
};

export const getProfile = async (userId, signal) => {
  const response = await fetch(`/profiles/${userId}`, { signal });
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
