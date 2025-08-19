import React, { useState } from 'react';

function Login({ setLoggedIn }) { // Changed from setIsLogged to setLoggedIn
  const [uName, setUName] = useState('');
  const [uPass, setUPass] = useState('');
  const [error, setError] = useState('');

  const doLogin = async () => {
    setError('');
    try {
      const res = await fetch('http://localhost:8081/pahana-backend/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ username: uName, password: uPass })
      });
      const data = await res.json();
      if (data.ok) {
        setLoggedIn(true); // Updated to use setLoggedIn
      } else {
        setError('Wrong username or password');
      }
    } catch (err) {
      setError('Failed to connect to server. Check CORS settings.');
    }
  };

  return (
    <div className="login-box">
      <h2>Enter to System</h2>
      <input
        placeholder="User Name"
        value={uName}
        onChange={(e) => setUName(e.target.value)}
      />
      <input
        type="password"
        placeholder="Pass"
        value={uPass}
        onChange={(e) => setUPass(e.target.value)}
      />
      <button onClick={doLogin}>Enter</button>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </div>
  );
}

export default Login;