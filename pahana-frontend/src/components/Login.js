import React, { useState } from 'react';

function Login({ setIsLogged }) {
  const [uName, setUName] = useState('');
  const [uPass, setUPass] = useState('');

  const doLogin = async () => {
    const res = await fetch('http://localhost:8080/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: new URLSearchParams({ username: uName, password: uPass })
    });
    const data = await res.json();
    if (data.ok) {
      setIsLogged(true);
    } else {
      alert('Wrong info');
    }
  };

  return (
    <div className="login-box">
      <h2>Enter to System</h2>
      <input placeholder="User Name" value={uName} onChange={(e) => setUName(e.target.value)} />
      <input type="password" placeholder="Pass" value={uPass} onChange={(e) => setUPass(e.target.value)} />
      <button onClick={doLogin}>Enter</button>
    </div>
  );
}

export default Login;