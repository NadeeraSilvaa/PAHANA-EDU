// src/components/Login.js
import React, { useState } from 'react';
import { useTheme } from '../ThemeContext';

function Login({ setLoggedIn, setError, error }) {
  const { darkMode } = useTheme();
  const [uName, setUName] = useState('');
  const [uPass, setUPass] = useState('');

  const doLogin = async () => {
    setError('');
    try {
      const res = await fetch('http://localhost:8081/pahana-backend/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ username: uName, password: uPass })
      });
      const data = await res.json();
      if (data.ok && data.token) {
        localStorage.setItem('authToken', data.token);
        setLoggedIn(true);
      } else {
        setError('Invalid username or password');
      }
    } catch (err) {
      setError('Failed to connect to server.');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-r from-blue-400 to-purple-500 p-4">
      <div className={`w-full max-w-md p-8 rounded-3xl shadow-2xl transition 
        ${darkMode ? 'bg-gray-800 text-gray-100' : 'bg-white text-gray-900'}`}>
        <h2 className="text-4xl font-bold mb-6 text-center bg-clip-text text-transparent bg-gradient-to-r from-purple-500 to-pink-500">
          Welcome to Pahana Edu
        </h2>
        <div className="space-y-5">
          <input
            className={`w-full p-4 rounded-xl border focus:outline-none focus:ring-2 focus:ring-purple-500 transition
              ${darkMode ? 'bg-gray-700 border-gray-600 text-gray-100 placeholder-gray-400' : 'bg-gray-100 border-gray-300 text-gray-900 placeholder-gray-500'}`}
            placeholder="Username"
            value={uName}
            onChange={(e) => setUName(e.target.value)}
          />
          <input
            type="password"
            className={`w-full p-4 rounded-xl border focus:outline-none focus:ring-2 focus:ring-purple-500 transition
              ${darkMode ? 'bg-gray-700 border-gray-600 text-gray-100 placeholder-gray-400' : 'bg-gray-100 border-gray-300 text-gray-900 placeholder-gray-500'}`}
            placeholder="Password"
            value={uPass}
            onChange={(e) => setUPass(e.target.value)}
          />
          <button
            onClick={doLogin}
            className="w-full py-4 bg-gradient-to-r from-purple-500 to-pink-500 text-white font-semibold rounded-xl shadow-lg hover:opacity-90 transition"
          >
            Login
          </button>
        </div>
        {error && <p className="mt-4 text-red-500 text-center font-medium">{error}</p>}
      </div>
    </div>
  );
}

export default Login;
