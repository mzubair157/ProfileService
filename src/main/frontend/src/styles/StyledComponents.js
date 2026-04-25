export const dashboardCss = `
  :root {
    --bg: #f4efe6;
    --surface: rgba(255, 252, 246, 0.86);
    --ink: #102542;
    --muted: #5a6472;
    --accent: #e4572e;
    --accent-soft: #ffd9c8;
    --line: rgba(16, 37, 66, 0.12);
    --shadow: 0 24px 60px rgba(16, 37, 66, 0.12);
  }

  * {
    box-sizing: border-box;
  }

  body {
    margin: 0;
    font-family: "IBM Plex Sans", sans-serif;
    color: var(--ink);
    background:
      radial-gradient(circle at top left, rgba(228, 87, 46, 0.18), transparent 28%),
      radial-gradient(circle at bottom right, rgba(16, 37, 66, 0.12), transparent 32%),
      linear-gradient(135deg, #f8f2e8 0%, #eef4f6 100%);
  }

  .dashboard-shell {
    min-height: 100vh;
    padding: 32px 20px 56px;
  }

  .dashboard-frame {
    max-width: 1180px;
    margin: 0 auto;
    display: grid;
    gap: 20px;
  }

  .hero-card,
  .panel {
    background: var(--surface);
    border: 1px solid var(--line);
    box-shadow: var(--shadow);
    backdrop-filter: blur(18px);
  }

  .hero-card {
    display: grid;
    gap: 20px;
    padding: 28px;
    border-radius: 28px;
    animation: rise 480ms ease-out;
  }

  .hero-copy h1,
  .panel h3 {
    font-family: "Space Grotesk", sans-serif;
    margin: 0;
  }

  .hero-copy h1 {
    font-size: clamp(2.2rem, 5vw, 4.2rem);
    line-height: 0.95;
    margin-top: 10px;
  }

  .hero-copy p,
  .panel-copy,
  .list-item p,
  .empty-state,
  .table-cell-muted {
    color: var(--muted);
    margin: 8px 0 0;
  }

  .profile-meta,
  .metric-row,
  .toolbar-row {
    display: flex;
    gap: 12px;
    align-items: baseline;
    flex-wrap: wrap;
  }

  .profile-meta span,
  .pill,
  .toolbar-button {
    border: 1px solid var(--line);
    padding: 8px 12px;
    border-radius: 999px;
    background: rgba(255, 255, 255, 0.65);
  }

  .grid-two {
    display: grid;
    gap: 20px;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  }

  .panel {
    border-radius: 24px;
    padding: 24px;
    animation: rise 580ms ease-out;
  }

  .stack-list,
  .favorite-grid,
  .live-strip {
    display: grid;
    gap: 14px;
    margin-top: 18px;
  }

  .favorite-grid {
    grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  }

  .favorite-tile,
  .list-item,
  .live-card {
    border-radius: 18px;
    padding: 16px;
    background: rgba(255, 255, 255, 0.7);
    border: 1px solid rgba(16, 37, 66, 0.08);
  }

  .list-item,
  .toolbar-row {
    display: flex;
    justify-content: space-between;
    gap: 16px;
    align-items: center;
  }

  .eyebrow,
  .tile-label {
    text-transform: uppercase;
    letter-spacing: 0.18em;
    font-size: 0.74rem;
    color: var(--accent);
    font-weight: 700;
  }

  .metric {
    font-size: 2.8rem;
    line-height: 1;
    font-family: "Space Grotesk", sans-serif;
  }

  .metric-label {
    color: var(--muted);
  }

  .score-pill {
    background: var(--accent-soft);
  }

  .status-card {
    text-align: center;
    padding: 72px 24px;
  }

  .status-card h2 {
    font-family: "Space Grotesk", sans-serif;
    margin-bottom: 8px;
  }

  .orders-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 18px;
  }

  .orders-table th,
  .orders-table td {
    text-align: left;
    padding: 12px 10px;
    border-bottom: 1px solid rgba(16, 37, 66, 0.08);
  }

  .orders-table tbody tr:last-child td {
    border-bottom: 0;
  }

  .toolbar-button {
    font: inherit;
    cursor: pointer;
  }

  .toolbar-button-accent {
    background: var(--accent);
    color: white;
    border-color: transparent;
  }

  .favorite-tile {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 12px;
  }

  .favorite-content {
    display: grid;
    gap: 6px;
    min-width: 0;
  }

  .favorite-actions {
    display: flex;
    gap: 8px;
    align-items: center;
  }

  .favorite-note,
  .favorite-priority {
    margin: 0;
    color: var(--muted);
  }

  .favorite-edit-stack {
    display: grid;
    gap: 8px;
  }

  .favorite-note-input,
  .favorite-quick-add {
    width: 100%;
  }

  .favorite-priority-select {
    border-radius: 12px;
    border: 1px solid var(--line);
    padding: 10px 12px;
    font: inherit;
    background: rgba(255, 255, 255, 0.88);
  }

  .favorite-add-row {
    display: grid;
    gap: 12px;
    grid-template-columns: minmax(0, 1fr) auto;
    align-items: center;
    margin-top: 16px;
  }

  .favorite-spinner {
    min-width: 24px;
    text-align: center;
    color: var(--accent);
    font-weight: 700;
  }

  .favorite-tile-removing {
    opacity: 0.45;
    transform: scale(0.98);
  }

  .icon-button {
    width: 36px;
    height: 36px;
    border-radius: 999px;
    border: 1px solid var(--line);
    background: rgba(255, 255, 255, 0.88);
    color: var(--accent);
    font: inherit;
    font-size: 1.2rem;
    cursor: pointer;
  }

  .editor-stack {
    display: grid;
    gap: 10px;
    margin-top: 10px;
  }

  .profile-input {
    width: min(100%, 460px);
    border-radius: 14px;
    border: 1px solid var(--line);
    padding: 12px 14px;
    font: inherit;
    background: rgba(255, 255, 255, 0.88);
    color: var(--ink);
  }

  .profile-input-title {
    font-family: "Space Grotesk", sans-serif;
    font-size: 1.5rem;
  }

  .toolbar-button:disabled {
    cursor: default;
    opacity: 0.55;
  }

  .spinner {
    width: 48px;
    height: 48px;
    border: 4px solid rgba(16, 37, 66, 0.12);
    border-top-color: var(--accent);
    border-radius: 50%;
    margin: 0 auto 12px;
    animation: spin 900ms linear infinite;
  }

  @keyframes spin {
    to {
      transform: rotate(360deg);
    }
  }

  @keyframes rise {
    from {
      opacity: 0;
      transform: translateY(12px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }

  @media (min-width: 900px) {
    .hero-card {
      grid-template-columns: 1.4fr 0.9fr;
      align-items: stretch;
    }
  }

  @media (max-width: 640px) {
    .favorite-add-row {
      grid-template-columns: 1fr;
    }
  }
`;
