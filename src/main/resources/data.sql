INSERT INTO loyalty_account (id, balance, version, created_at, updated_at)
SELECT 1, 1130, 0, TIMESTAMP '2026-04-20 09:00:00', TIMESTAMP '2026-04-24 18:30:00'
WHERE NOT EXISTS (SELECT 1 FROM loyalty_account WHERE id = 1);

INSERT INTO loyalty_account (id, balance, version, created_at, updated_at)
SELECT 2, 540, 0, TIMESTAMP '2026-04-18 10:15:00', TIMESTAMP '2026-04-24 14:45:00'
WHERE NOT EXISTS (SELECT 1 FROM loyalty_account WHERE id = 2);

INSERT INTO loyalty_account (id, balance, version, created_at, updated_at)
SELECT 3, 90, 0, TIMESTAMP '2026-04-17 08:40:00', TIMESTAMP '2026-04-23 11:20:00'
WHERE NOT EXISTS (SELECT 1 FROM loyalty_account WHERE id = 3);

INSERT INTO loyalty_transaction (id, loyalty_account_id, delta, idempotency_key, balance_after, processed_at, created_at, updated_at)
SELECT 1, 1, 500, 'seed-loyalty-1', 500, TIMESTAMP '2026-04-20 09:05:00', TIMESTAMP '2026-04-20 09:05:00', TIMESTAMP '2026-04-20 09:05:00'
WHERE NOT EXISTS (SELECT 1 FROM loyalty_transaction WHERE id = 1);

INSERT INTO loyalty_transaction (id, loyalty_account_id, delta, idempotency_key, balance_after, processed_at, created_at, updated_at)
SELECT 2, 1, 750, 'seed-loyalty-2', 1250, TIMESTAMP '2026-04-22 12:00:00', TIMESTAMP '2026-04-22 12:00:00', TIMESTAMP '2026-04-22 12:00:00'
WHERE NOT EXISTS (SELECT 1 FROM loyalty_transaction WHERE id = 2);

INSERT INTO loyalty_transaction (id, loyalty_account_id, delta, idempotency_key, balance_after, processed_at, created_at, updated_at)
SELECT 3, 1, -120, 'seed-loyalty-3', 1130, TIMESTAMP '2026-04-24 18:30:00', TIMESTAMP '2026-04-24 18:30:00', TIMESTAMP '2026-04-24 18:30:00'
WHERE NOT EXISTS (SELECT 1 FROM loyalty_transaction WHERE id = 3);

INSERT INTO loyalty_transaction (id, loyalty_account_id, delta, idempotency_key, balance_after, processed_at, created_at, updated_at)
SELECT 4, 2, 300, 'seed-loyalty-4', 300, TIMESTAMP '2026-04-18 10:20:00', TIMESTAMP '2026-04-18 10:20:00', TIMESTAMP '2026-04-18 10:20:00'
WHERE NOT EXISTS (SELECT 1 FROM loyalty_transaction WHERE id = 4);

INSERT INTO loyalty_transaction (id, loyalty_account_id, delta, idempotency_key, balance_after, processed_at, created_at, updated_at)
SELECT 5, 2, 240, 'seed-loyalty-5', 540, TIMESTAMP '2026-04-21 16:00:00', TIMESTAMP '2026-04-21 16:00:00', TIMESTAMP '2026-04-21 16:00:00'
WHERE NOT EXISTS (SELECT 1 FROM loyalty_transaction WHERE id = 5);

INSERT INTO loyalty_transaction (id, loyalty_account_id, delta, idempotency_key, balance_after, processed_at, created_at, updated_at)
SELECT 6, 3, 90, 'seed-loyalty-6', 90, TIMESTAMP '2026-04-17 08:45:00', TIMESTAMP '2026-04-17 08:45:00', TIMESTAMP '2026-04-17 08:45:00'
WHERE NOT EXISTS (SELECT 1 FROM loyalty_transaction WHERE id = 6);

INSERT INTO user_profile (id, username, email, region, loyalty_account_id, created_at, updated_at)
SELECT 1, 'alice', 'alice@example.com', 'EU', 1, TIMESTAMP '2026-04-20 09:00:00', TIMESTAMP '2026-04-24 18:30:00'
WHERE NOT EXISTS (SELECT 1 FROM user_profile WHERE id = 1);

INSERT INTO user_profile (id, username, email, region, loyalty_account_id, created_at, updated_at)
SELECT 2, 'bob', 'bob@example.com', 'US', 2, TIMESTAMP '2026-04-18 10:15:00', TIMESTAMP '2026-04-24 14:45:00'
WHERE NOT EXISTS (SELECT 1 FROM user_profile WHERE id = 2);

INSERT INTO user_profile (id, username, email, region, loyalty_account_id, created_at, updated_at)
SELECT 3, 'charlie', 'charlie@example.com', 'APAC', 3, TIMESTAMP '2026-04-17 08:40:00', TIMESTAMP '2026-04-23 11:20:00'
WHERE NOT EXISTS (SELECT 1 FROM user_profile WHERE id = 3);

INSERT INTO discount (id, code, percentage, expiry_date, active, created_at, updated_at)
SELECT 1, 'WELCOME10', 10.00, TIMESTAMP '2026-12-31 23:59:59', TRUE, TIMESTAMP '2026-04-10 09:00:00', TIMESTAMP '2026-04-24 09:00:00'
WHERE NOT EXISTS (SELECT 1 FROM discount WHERE id = 1);

INSERT INTO discount (id, code, percentage, expiry_date, active, created_at, updated_at)
SELECT 2, 'SPRING15', 15.00, TIMESTAMP '2026-06-30 23:59:59', TRUE, TIMESTAMP '2026-04-12 10:00:00', TIMESTAMP '2026-04-23 10:30:00'
WHERE NOT EXISTS (SELECT 1 FROM discount WHERE id = 2);

INSERT INTO discount (id, code, percentage, expiry_date, active, created_at, updated_at)
SELECT 3, 'LEGACY5', 5.00, TIMESTAMP '2025-12-31 23:59:59', FALSE, TIMESTAMP '2025-01-05 08:00:00', TIMESTAMP '2025-12-31 23:59:59'
WHERE NOT EXISTS (SELECT 1 FROM discount WHERE id = 3);

INSERT INTO discount_eligible_users (discount_id, eligible_users_id)
SELECT 1, 1
WHERE NOT EXISTS (
    SELECT 1 FROM discount_eligible_users WHERE discount_id = 1 AND eligible_users_id = 1
);

INSERT INTO discount_eligible_users (discount_id, eligible_users_id)
SELECT 1, 2
WHERE NOT EXISTS (
    SELECT 1 FROM discount_eligible_users WHERE discount_id = 1 AND eligible_users_id = 2
);

INSERT INTO discount_eligible_users (discount_id, eligible_users_id)
SELECT 2, 2
WHERE NOT EXISTS (
    SELECT 1 FROM discount_eligible_users WHERE discount_id = 2 AND eligible_users_id = 2
);

INSERT INTO discount_eligible_users (discount_id, eligible_users_id)
SELECT 2, 3
WHERE NOT EXISTS (
    SELECT 1 FROM discount_eligible_users WHERE discount_id = 2 AND eligible_users_id = 3
);

INSERT INTO user_favorite (id, user_id, product_id, note, priority_level, created_at, updated_at)
SELECT 1, 1, 101, 'Gift shortlist', 5, TIMESTAMP '2026-04-21 08:00:00', TIMESTAMP '2026-04-21 08:00:00'
WHERE NOT EXISTS (SELECT 1 FROM user_favorite WHERE id = 1);

INSERT INTO user_favorite (id, user_id, product_id, note, priority_level, created_at, updated_at)
SELECT 2, 1, 205, 'Watch price drop', 4, TIMESTAMP '2026-04-21 08:05:00', TIMESTAMP '2026-04-21 08:05:00'
WHERE NOT EXISTS (SELECT 1 FROM user_favorite WHERE id = 2);

INSERT INTO user_favorite (id, user_id, product_id, note, priority_level, created_at, updated_at)
SELECT 3, 2, 310, NULL, 2, TIMESTAMP '2026-04-22 14:15:00', TIMESTAMP '2026-04-22 14:15:00'
WHERE NOT EXISTS (SELECT 1 FROM user_favorite WHERE id = 3);

INSERT INTO user_favorite (id, user_id, product_id, note, priority_level, created_at, updated_at)
SELECT 4, 3, 415, NULL, 3, TIMESTAMP '2026-04-23 16:45:00', TIMESTAMP '2026-04-23 16:45:00'
WHERE NOT EXISTS (SELECT 1 FROM user_favorite WHERE id = 4);

INSERT INTO audit_event (id, user_id, event_type, details, occurred_at, created_at, updated_at)
SELECT 1, 1, 'PROFILE_VIEW', 'Viewed profile from web dashboard', TIMESTAMP '2026-04-24 18:31:00', TIMESTAMP '2026-04-24 18:31:00', TIMESTAMP '2026-04-24 18:31:00'
WHERE NOT EXISTS (SELECT 1 FROM audit_event WHERE id = 1);

INSERT INTO audit_event (id, user_id, event_type, details, occurred_at, created_at, updated_at)
SELECT 2, 1, 'LOYALTY_REDEEM', 'Redeemed 120 points for a voucher', TIMESTAMP '2026-04-24 18:30:00', TIMESTAMP '2026-04-24 18:30:00', TIMESTAMP '2026-04-24 18:30:00'
WHERE NOT EXISTS (SELECT 1 FROM audit_event WHERE id = 2);

INSERT INTO audit_event (id, user_id, event_type, details, occurred_at, created_at, updated_at)
SELECT 3, 2, 'FAVORITE_ADD', 'Added product 310 to favorites', TIMESTAMP '2026-04-22 14:15:00', TIMESTAMP '2026-04-22 14:15:00', TIMESTAMP '2026-04-22 14:15:00'
WHERE NOT EXISTS (SELECT 1 FROM audit_event WHERE id = 3);

INSERT INTO audit_event (id, user_id, event_type, details, occurred_at, created_at, updated_at)
SELECT 4, 3, 'DISCOUNT_VIEW', 'Viewed seasonal discounts', TIMESTAMP '2026-04-23 16:50:00', TIMESTAMP '2026-04-23 16:50:00', TIMESTAMP '2026-04-23 16:50:00'
WHERE NOT EXISTS (SELECT 1 FROM audit_event WHERE id = 4);
