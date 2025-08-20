// src/components/ViewBills.js
import React, { useState } from 'react';
import { useTheme } from '../ThemeContext';

function ViewBills() {
  const { darkMode } = useTheme();
  const [bills, setBills] = useState([]);
  const [custId, setCustId] = useState('');
  const [error, setError] = useState('');

  const fetchBills = async () => {
    if (!custId) {
      setError('Please enter customer ID');
      return;
    }
    setError('');

    try {
      const res = await fetch(`http://localhost:8081/pahana-backend/viewBills?customerId=${custId}`);
      const data = await res.json();
      setBills(Array.isArray(data.bills) ? data.bills : Array.isArray(data) ? data : []);
    } catch (err) {
      setError('Fetch failed');
    }
  };

  return (
    <div className={`p-6 rounded-lg shadow-md ${darkMode ? 'bg-cardDark text-textDark' : 'bg-cardLight text-textLight'}`}>
      <h3 className="text-xl font-semibold mb-4">View Customer Bills</h3>
      {error && <p className="text-error mb-4">{error}</p>}
      <input
        className={`w-full p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary mb-4 ${darkMode ? 'bg-backgroundDark border-borderDark text-textDark' : 'bg-white border-borderLight text-textLight'}`}
        placeholder="Customer ID"
        value={custId}
        onChange={(e) => setCustId(e.target.value)}
      />
      <button
        onClick={fetchBills}
        className="w-full py-3 bg-primary text-white rounded-md hover:opacity-90 transition mb-6"
      >
        Fetch Bills
      </button>
      <ul className="space-y-4">
        {bills.map((bill) => (
          <li key={bill.id} className="p-4 border rounded-md border-borderLight dark:border-borderDark">
            Bill ID: {bill.id} - Amount: ${bill.billAmount}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default ViewBills;