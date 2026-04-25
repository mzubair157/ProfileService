import React from 'react';
import { render, screen } from '@testing-library/react';
import ProfileView from './ProfileView';

describe('ProfileView', () => {
  test('renders the profile summary and loyalty details', () => {
    render(
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

    expect(screen.getByText('Ada')).toBeInTheDocument();
    expect(screen.getByText('ada@example.com')).toBeInTheDocument();
    expect(screen.getByText(/420/i)).toBeInTheDocument();
  });
});
