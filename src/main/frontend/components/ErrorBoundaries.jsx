import React from 'react';

class ErrorBoundaries extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false };
  }

  static getDerivedStateFromError() {
    return { hasError: true };
  }

  componentDidCatch(error) {
    if (this.props.onError) {
      this.props.onError(error);
    }
  }

  render() {
    if (this.state.hasError) {
      return (
        <section className="panel status-card">
          <div className="eyebrow">Recovered</div>
          <h2>Component fallback engaged</h2>
          <p>The interface caught an unexpected rendering problem.</p>
        </section>
      );
    }

    return this.props.children;
  }
}

export default ErrorBoundaries;
