import React from 'react';
import { act } from 'react';
import { createRoot } from 'react-dom/client';
import ProfileView from './ProfileView';

describe('ProfileView', () => {
  let container;
  let root;

  beforeEach(() => {
    container = document.createElement('div');
    document.body.appendChild(container);
    root = createRoot(container);
  });

  afterEach(() => {
    act(() => {
      root.unmount();
    });
    container.remove();
  });

  test('renders the profile summary and loyalty details', () => {
    act(() => {
      root.render(
        <ProfileView
          profile={{
            id: 1,
            username: 'Ada',
            email: 'ada@example.com',
            region: 'EU',
            loyaltyAccountId: 8,
            loyaltyBalance: 420
          }}
        />
      );
    });

    expect(container.textContent).toContain('Ada');
    expect(container.textContent).toContain('ada@example.com');
    expect(container.textContent).toContain('420');
  });
});
