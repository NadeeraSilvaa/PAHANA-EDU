// src/components/DeleteCustomer.js
import React, { useState } from 'react';
import { useTheme } from '../ThemeContext';

function DeleteCustomer() {
  const { darkMode } = useTheme();
  const [accNum, setAccNum] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const deleteCust = async () => {
    if (!accNum) {
      setError('Please enter account number');
      return;
    }
    setError('');

    const res = await fetch('http://localhost:8081/pahana-backend/deleteCustomer', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: new URLSearchParams({ accountNumber: accNum })
    });
    const data = await res.json();
    setMessage(data.ok ? 'Customer deleted successfully' : 'Deletion failed');
  };

  return (
    <div className={`p-6 rounded-lg shadow-md ${darkMode ? 'bg-cardDark text-textDark' : 'bg-cardLight text-textLight'}`}>
      <h3 className="text-xl font-semibold mb-4">Delete Customer</h3>
      {error && <p className="text-error mb-4">{error}</p>}
      <input
        className={`w-full p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary ${darkMode ? 'bg-backgroundDark border-borderDark text-textDark' : 'bg-white border-borderLight text-textLight'}`}
        placeholder="Account Number"
        value={accNum}
        onChange={(e) => setAccNum(e.target.value)}
      />
      <button
        onClick={deleteCust}
        className="w-full py-3 bg-error text-white rounded-md hover:opacity-90 transition mt-4"
      >
        Delete Customer
      </button>
      {message && <p className="mt-4 text-secondary">{message}</p>}
    </div>
  );
}

export default DeleteCustomer;