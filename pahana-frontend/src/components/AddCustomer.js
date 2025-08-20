import React, { useState } from 'react';
import { useTheme } from '../ThemeContext';

function AddCustomer() {
  const { darkMode } = useTheme();
  const [name, setName] = useState('');
  const [addr, setAddr] = useState('');
  const [phone, setPhone] = useState('');
  const [error, setError] = useState('');

  const doAdd = async () => {
    if (!name || !addr || !phone) {
      setError('Please fill all fields');
      return;
    }
    setError('');

    try {
      const res = await fetch('http://localhost:8081/pahana-backend/addCustomer', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ name, address: addr, phone })
      });

      if (!res.ok) {
        throw new Error(`HTTP ${res.status}`);
      }

      const data = await res.json();

      if (data.ok) {
        alert('Customer Added Successfully');
        setName('');
        setAddr('');
        setPhone('');
      } else {
        setError(data.message || 'Unknown error');
      }
    } catch (err) {
      setError(`Failed to add customer: ${err.message}`);
    }
  };

  return (
    <div className={`p-6 rounded-lg shadow-md ${darkMode ? 'bg-cardDark text-textDark' : 'bg-cardLight text-textLight'}`}>
      <h3 className="text-xl font-semibold mb-4">Add New Customer</h3>
      {error && <p className="text-error mb-4">{error}</p>}
      <div className="space-y-4">
        <input
          className={`w-full p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary ${darkMode ? 'bg-backgroundDark border-borderDark text-textDark' : 'bg-white border-borderLight text-textLight'}`}
          placeholder="Customer Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
        <input
          className={`w-full p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary ${darkMode ? 'bg-backgroundDark border-borderDark text-textDark' : 'bg-white border-borderLight text-textLight'}`}
          placeholder="Address"
          value={addr}
          onChange={(e) => setAddr(e.target.value)}
        />
        <input
          className={`w-full p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary ${darkMode ? 'bg-backgroundDark border-borderDark text-textDark' : 'bg-white border-borderLight text-textLight'}`}
          placeholder="Phone Number"
          value={phone}
          onChange={(e) => setPhone(e.target.value)}
        />
        <button
          onClick={doAdd}
          className="w-full py-3 bg-primary text-white rounded-md hover:opacity-90 transition"
        >
          Add Customer
        </button>
      </div>
    </div>
  );
}

export default AddCustomer;