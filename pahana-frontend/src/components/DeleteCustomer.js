import React, { useState, useEffect } from 'react';
import { useTheme } from '../ThemeContext';

function DeleteCustomer() {
  const { darkMode } = useTheme();
  const [accNum, setAccNum] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [userRole, setUserRole] = useState('');

  useEffect(() => {
    const token = localStorage.getItem('authToken');
    if (token) {
      fetch('http://localhost:8081/pahana-backend/login/verify?token=' + token)
        .then(res => res.json())
        .then(data => {
          if (data.valid) {
            setUserRole(data.role || 'Cashier');
          } else {
            setError('Unauthorized access');
            setUserRole('');
          }
        })
        .catch(() => {
          setError('Failed to verify token');
          setUserRole('');
        });
    } else {
      setError('Please log in');
      setUserRole('');
    }
  }, []);

  const deleteCust = async () => {
    if (!userRole || userRole !== 'Admin') {
      setError('Access denied: Admin role required');
      return;
    }
    if (!accNum) {
      setError('Please enter account number');
      return;
    }
    setError('');

    const res = await fetch('http://localhost:8081/pahana-backend/deleteCustomer', {
      method: 'POST',
      headers: { 
        'Content-Type': 'application/x-www-form-urlencoded',
        'Authorization': `Bearer ${localStorage.getItem('authToken')}`
      },
      body: new URLSearchParams({ accountNumber: accNum })
    });
    const data = await res.json();
    setMessage(data.ok ? 'Customer deleted successfully' : 'Deletion failed');
  };

  if (!userRole) return <p className="text-error text-center">Loading role...</p>;
  if (userRole !== 'Admin') return <p className="text-error text-center">Access denied: Admin role required</p>;

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