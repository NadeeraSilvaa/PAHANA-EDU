import React, { useState, useEffect } from 'react';
import Login from './components/Login';
import Dashboard from './components/Dashboard';
import { ThemeProvider } from './ThemeContext';

function App() {
  const [loggedIn, setLoggedIn] = useState(false);
  const [error, setError] = useState('');
  const [userRole, setUserRole] = useState('');

  useEffect(() => {
    const token = localStorage.getItem('authToken');
    if (token) {
      // Try GET first (common for token verification)
      fetch(`http://localhost:8081/pahana-backend/login/verify?token=${encodeURIComponent(token)}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
      })
        .then(res => {
          if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
          return res.json();
        })
        .then(data => {
          if (data.valid) {
            setLoggedIn(true);
            setUserRole(data.role || 'Cashier');
          } else {
            localStorage.removeItem('authToken');
            setLoggedIn(false);
            setUserRole('');
          }
        })
        .catch(err => {
          // Fallback to POST if GET fails
          fetch('http://localhost:8081/pahana-backend/login/verify', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({ token })
          })
            .then(res => {
              if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
              return res.json();
            })
            .then(data => {
              if (data.valid) {
                setLoggedIn(true);
                setUserRole(data.role || 'Cashier');
              } else {
                localStorage.removeItem('authToken');
                setLoggedIn(false);
                setUserRole('');
              }
            })
            .catch(err => {
              setError('Failed to verify login: ' + err.message);
              localStorage.removeItem('authToken');
              setLoggedIn(false);
              setUserRole('');
            });
        });
    }
  }, []);

  return (
    <ThemeProvider>
      <div className="min-h-screen flex items-center justify-center bg-backgroundLight dark:bg-backgroundDark transition-colors duration-300">
        <div className="w-full max-w-7xl p-4 md:p-8 rounded-lg shadow-xl">
          {loggedIn ? (
            <Dashboard setLoggedIn={setLoggedIn} setError={setError} userRole={userRole} />
          ) : (
            <Login setLoggedIn={setLoggedIn} setError={setError} error={error} />
          )}
        </div>
      </div>
    </ThemeProvider>
  );
}

export default App;