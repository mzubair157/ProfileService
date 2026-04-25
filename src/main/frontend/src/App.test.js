import React from 'react';
import { act } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';
import { GlobalProvider } from './context/GlobalStateContext';
import { installFetchHandlers } from './mocks/handlers';

describe('App', () => {
  let container;
  let root;

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

  test('renders dashboard sections after the parallel fetch completes', async () => {
    await act(async () => {
      root.render(
        <GlobalProvider>
          <App />
        </GlobalProvider>
      );
    });

    await act(async () => {
      await Promise.resolve();
      await Promise.resolve();
    });

    expect(container.textContent).toContain('Ada');
    expect(container.textContent).toContain('Active campaigns');
    expect(container.textContent).toContain('Saved products');
    expect(container.textContent).toContain('Recent order history');
  });
});
