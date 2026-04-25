import { useEffect, useState, useTransition } from 'react';
import { getProfile } from '../api/apiClient';
import { useCurrentUserActions } from '../context/GlobalStateContext';

export const useProfile = (userId) => {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [isPending, startTransition] = useTransition();
  const { setCurrentUser } = useCurrentUserActions();

  useEffect(() => {
    if (!userId) {
      setLoading(false);
      return undefined;
    }

    const controller = new AbortController();
    setLoading(true);
    setError('');

    getProfile(userId, controller.signal)
      .then((payload) => {
        startTransition(() => {
          setProfile(payload);
          setCurrentUser(payload);
          setLoading(false);
        });
      })
      .catch((fetchError) => {
        if (fetchError.name !== 'AbortError') {
          setError(fetchError.message || 'Unable to load profile');
          setLoading(false);
        }
      });

    return () => controller.abort();
  }, [setCurrentUser, userId]);

  return { profile, loading: loading || isPending, error };
};
