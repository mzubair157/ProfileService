import React, { createContext, useContext, useMemo, useReducer } from 'react';

const LiveOrdersStateContext = createContext([]);
const LiveOrdersActionsContext = createContext(null);
const CurrentUserStateContext = createContext(null);
const CurrentUserActionsContext = createContext(null);

const MAX_LIVE_ORDERS = 50;

const liveOrdersReducer = (state, action) => {
  switch (action.type) {
    case 'PUSH_LIVE_ORDER':
      return [action.payload, ...state].slice(0, MAX_LIVE_ORDERS);
    case 'RESET_LIVE_ORDERS':
      return [];
    default:
      return state;
  }
};

const currentUserReducer = (state, action) => {
  switch (action.type) {
    case 'SET_CURRENT_USER':
      return action.payload;
    default:
      return state;
  }
};

export const GlobalProvider = ({ children }) => {
  const [liveOrders, dispatchLiveOrders] = useReducer(liveOrdersReducer, []);
  const [currentUser, dispatchCurrentUser] = useReducer(currentUserReducer, null);

  const liveOrderActions = useMemo(() => ({
    appendLiveOrder: (order) => dispatchLiveOrders({ type: 'PUSH_LIVE_ORDER', payload: order }),
    resetLiveOrders: () => dispatchLiveOrders({ type: 'RESET_LIVE_ORDERS' })
  }), []);

  const currentUserActions = useMemo(() => ({
    setCurrentUser: (user) => dispatchCurrentUser({ type: 'SET_CURRENT_USER', payload: user })
  }), []);

  return (
    <CurrentUserActionsContext.Provider value={currentUserActions}>
      <CurrentUserStateContext.Provider value={currentUser}>
        <LiveOrdersActionsContext.Provider value={liveOrderActions}>
          <LiveOrdersStateContext.Provider value={liveOrders}>
            {children}
          </LiveOrdersStateContext.Provider>
        </LiveOrdersActionsContext.Provider>
      </CurrentUserStateContext.Provider>
    </CurrentUserActionsContext.Provider>
  );
};

export const useLiveOrdersState = () => useContext(LiveOrdersStateContext);
export const useLiveOrdersActions = () => useContext(LiveOrdersActionsContext);
export const useCurrentUserState = () => useContext(CurrentUserStateContext);
export const useCurrentUserActions = () => useContext(CurrentUserActionsContext);
