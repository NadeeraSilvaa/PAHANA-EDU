import React from 'react';
import { useTheme } from '../ThemeContext';
import { useNavigate } from 'react-router-dom'; // Assuming react-router for navigation

function Logout() {
  const { darkMode } = useTheme();
  const navigate = useNavigate();

  const doLogout = () => {
    localStorage.removeItem('token');
    navigate('/login'); // Redirect to login
  };

  return (
    <button
      onClick={doLogout}
      className={`py-2 px-4 bg-error text-white rounded-md hover:opacity-90 transition ${darkMode ? 'bg-errorDark' : 'bg-errorLight'}`}
    >
      Logout
    </button>
  );
}

export default Logout;