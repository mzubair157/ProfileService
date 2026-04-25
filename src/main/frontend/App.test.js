import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import App from './App';
import { GlobalProvider } from './context/GlobalStateContext';
import { installFetchHandlers } from './mocks/handlers';

describe('App', () => {
  beforeEach(() => {
    installFetchHandlers();
  });

  afterEach(() => {
    jest.resetAllMocks();
  });

  test('renders dashboard sections after the parallel fetch completes', async () => {
    render(
      <GlobalProvider>
        <App />
      </GlobalProvider>
    );

    expect(screen.getByText(/building the customer snapshot/i)).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.getByText('Ada')).toBeInTheDocument();
    });

    expect(screen.getByText(/active campaigns/i)).toBeInTheDocument();
    expect(screen.getByText(/saved products/i)).toBeInTheDocument();
    expect(screen.getByText(/recent order history/i)).toBeInTheDocument();
  });
});
