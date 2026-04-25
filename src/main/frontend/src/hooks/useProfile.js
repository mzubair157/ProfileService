import { useEffect, useState, useTransition } from 'react';
import { getProfile, updateProfile } from '../api/apiClient';
import { useCurrentUserActions } from '../context/GlobalStateContext';

export const useProfile = (userId) => {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [isPending, startTransition] = useTransition();
  const { setCurrentUser } = useCurrentUserActions();

  const syncProfile = (nextProfile) => {
    startTransition(() => {
      setProfile(nextProfile);
      setCurrentUser(nextProfile);
    });
  };

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
        syncProfile(payload);
        setLoading(false);
      })
      .catch((fetchError) => {
        if (fetchError.name !== 'AbortError') {
          setError(fetchError.message || 'Unable to load profile');
          setLoading(false);
        }
      });

    return () => controller.abort();
  }, [setCurrentUser, userId]);

  const saveProfile = async (payload) => {
    const updatedProfile = await updateProfile(userId, payload);
    syncProfile(updatedProfile);
    return updatedProfile;
  };

  const patchProfile = (updater) => {
    setProfile((current) => {
      if (!current) {
        return current;
      }

      const nextProfile = typeof updater === 'function'
        ? updater(current)
        : { ...current, ...updater };
      setCurrentUser(nextProfile);
      return nextProfile;
    });
  };

  return { profile, loading: loading || isPending, error, saveProfile, patchProfile };
};
