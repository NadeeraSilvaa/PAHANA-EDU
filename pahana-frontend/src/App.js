import React, { useState } from 'react';
import './styles/App.css';
import Login from './components/Login';
import Dashboard from './components/Dashboard';

function App() {
  const [loggedIn, setLoggedIn] = useState(false);

  return (
    <div className="app">
      {loggedIn ? <Dashboard setLoggedIn={setLoggedIn} /> : <Login setLoggedIn={setLoggedIn} />}
    </div>
  );
}

export default App;