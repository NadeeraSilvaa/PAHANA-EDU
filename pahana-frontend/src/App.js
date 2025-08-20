// src/App.js
import React, { useState, useEffect } from 'react';
import Login from './components/Login';
import Dashboard from './components/Dashboard';
import { ThemeProvider } from './ThemeContext';


function App() {
  const [loggedIn, setLoggedIn] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    const token = localStorage.getItem('authToken');
    if (token) {
      fetch('http://localhost:8081/pahana-backend/verify', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ token })
      })
        .then(res => res.json())
        .then(data => {
          if (data.valid) {
            setLoggedIn(true);
          } else {
            localStorage.removeItem('authToken');
            setLoggedIn(false);
          }
        })
        .catch(() => {
          setError('Failed to verify login.');
          localStorage.removeItem('authToken');
          setLoggedIn(false);
        });
    }
  }, []);

  return (
    <ThemeProvider>
      <div className="min-h-screen flex items-center justify-center bg-backgroundLight dark:bg-backgroundDark transition-colors duration-300">
        <div className="w-full max-w-7xl p-4 md:p-8 rounded-lg shadow-xl">
          {loggedIn ? (
            <Dashboard setLoggedIn={setLoggedIn} setError={setError} />
          ) : (
            <Login setLoggedIn={setLoggedIn} setError={setError} error={error} />
          )}
        </div>
      </div>
    </ThemeProvider>
  );
}

export default App;