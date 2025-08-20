import React, { useState, useEffect } from 'react';
import { useTheme } from '../ThemeContext';

function EditCustomer() {
  const { darkMode } = useTheme();
  const [accNum, setAccNum] = useState('');
  const [name, setName] = useState('');
  const [addr, setAddr] = useState('');
  const [phone, setPhone] = useState('');
  const [error, setError] = useState('');
  const [customerFetched, setCustomerFetched] = useState(false);
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

  const fetchCustomer = async () => {
    if (!userRole || !['Admin', 'Cashier'].includes(userRole)) {
      setError('Access denied: Insufficient permissions');
      return;
    }
    if (!accNum) {
      setError('Please enter an account number');
      return;
    }
    setError('');
    try {
      const res = await fetch(`http://localhost:8081/pahana-backend/getCustomer?accountNumber=${accNum}`, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('authToken')}` }
      });
      const data = await res.json();
      if (data.ok) {
        setName(data.customer.name);
        setAddr(data.customer.address);
        setPhone(data.customer.phone);
        setCustomerFetched(true);
      } else {
        setError(data.message || 'Customer not found');
        setCustomerFetched(false);
      }
    } catch {
      setError('Failed to fetch customer');
      setCustomerFetched(false);
    }
  };

  const doEdit = async () => {
    if (!userRole || !['Admin', 'Cashier'].includes(userRole)) {
      setError('Access denied: Insufficient permissions');
      return;
    }
    if (!accNum || !name || !addr || !phone) {
      setError('Please fill all fields');
      return;
    }
    setError('');

    try {
      const res = await fetch('http://localhost:8081/pahana-backend/editCustomer', {
        method: 'POST',
        headers: { 
          'Content-Type': 'application/x-www-form-urlencoded',
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`
        },
        body: new URLSearchParams({ accountNumber: accNum, name, address: addr, phone })
      });
      const data = await res.json();
      if (data.ok) {
        alert('Customer Updated Successfully');
        setAccNum('');
        setName('');
        setAddr('');
        setPhone('');
        setCustomerFetched(false);
      } else {
        setError(data.message || 'Update failed');
      }
    } catch {
      setError('Failed to update customer');
    }
  };

  if (!userRole) return <p className="text-error text-center">Loading role...</p>;

  return (
    <div className={`p-6 rounded-lg shadow-md ${darkMode ? 'bg-cardDark text-textDark' : 'bg-cardLight text-textLight'}`}>
      <h3 className="text-xl font-semibold mb-4">Edit Customer</h3>
      {error && <p className="text-error mb-4">{error}</p>}
      <div className="space-y-4">
        <div className="flex space-x-2">
          <input
            className={`flex-1 p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary ${darkMode ? 'bg-backgroundDark border-borderDark text-textDark' : 'bg-white border-borderLight text-textLight'}`}
            placeholder="Account Number"
            type="number"
            value={accNum}
            onChange={(e) => setAccNum(e.target.value)}
          />
          <button
            onClick={fetchCustomer}
            className="py-3 px-4 bg-secondary text-white rounded-md hover:opacity-90 transition"
          >
            Fetch
          </button>
        </div>
        {customerFetched && (
          <>
            <input
              className={`w-full p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary ${darkMode ? 'bg-backgroundDark border-borderDark text-textDark' : 'bg-white border-borderLight text-textLight'}`}
              placeholder="Name"
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
              placeholder="Phone"
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
            />
            <button
              onClick={doEdit}
              className="w-full py-3 bg-primary text-white rounded-md hover:opacity-90 transition"
            >
              Update Customer
            </button>
          </>
        )}
      </div>
    </div>
  );
}

export default EditCustomer;