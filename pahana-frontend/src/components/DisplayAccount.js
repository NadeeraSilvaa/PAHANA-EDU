// src/components/DisplayAccount.js
import React, { useState, useEffect } from 'react';
import { useTheme } from '../ThemeContext';

function DisplayAccount() {
  const { darkMode } = useTheme();
  const [searchTerm, setSearchTerm] = useState('');
  const [customers, setCustomers] = useState([]);
  const [filteredCustomers, setFilteredCustomers] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchAllCustomers = async () => {
      try {
        const res = await fetch('http://localhost:8081/pahana-backend/displayAccount?action=all');
        if (!res.ok) {
          const data = await res.json();
          throw new Error(data.error || 'Failed to fetch customers');
        }
        const data = await res.json();
        setCustomers(data);
        setFilteredCustomers(data);
      } catch (err) {
        setError(err.message);
      }
    };
    fetchAllCustomers();
  }, []);

  useEffect(() => {
    const filtered = customers.filter(customer =>
      customer.accountNumber.toString().includes(searchTerm) ||
      customer.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setFilteredCustomers(filtered);
  }, [searchTerm, customers]);

  const handleDelete = async (accountNumber) => {
    if (window.confirm('Are you sure you want to delete this customer?')) {
      try {
        const res = await fetch(`http://localhost:8081/pahana-backend/displayAccount?accountNumber=${accountNumber}`, {
          method: 'DELETE',
        });
        const data = await res.json();
        if (data.ok) {
          setCustomers(customers.filter(c => c.accountNumber !== accountNumber));
          setFilteredCustomers(filteredCustomers.filter(c => c.accountNumber !== accountNumber));
        } else {
          setError(data.message || 'Deletion failed');
        }
      } catch (err) {
        setError('Error deleting customer');
      }
    }
  };

  return (
    <div className={`p-6 rounded-lg shadow-md ${darkMode ? 'bg-cardDark text-textDark' : 'bg-cardLight text-textLight'}`}>
      <h3 className="text-xl font-semibold mb-4">View Customers</h3>
      {error && <p className="text-error mb-4">{error}</p>}
      <input
        className={`w-full p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary ${darkMode ? 'bg-backgroundDark border-borderDark text-textDark' : 'bg-white border-borderLight text-textLight'}`}
        placeholder="Search by Account Number or Name"
        type="text"
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
      />
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-6">
        {filteredCustomers.length > 0 ? (
          filteredCustomers.map((customer) => (
            <div
              key={customer.accountNumber}
              className={`p-4 border rounded-lg shadow-sm ${darkMode ? 'bg-backgroundDark border-borderDark' : 'bg-white border-borderLight'}`}
            >
              <h4 className="font-bold text-lg">{customer.name}</h4>
              <p className="text-textSecondaryLight dark:text-textSecondaryDark">Account: {customer.accountNumber}</p>
              <p className="text-textSecondaryLight dark:text-secondaryDark">Address: {customer.address}</p>
              <p className="text-textSecondaryLight dark:text-textSecondaryDark">Phone: {customer.phone}</p>
              <p className="text-textSecondaryLight dark:text-textSecondaryDark">Books Purchased: {customer.units}</p>
              <button
                onClick={() => handleDelete(customer.accountNumber)}
                className="mt-2 py-2 px-4 bg-error text-white rounded-md hover:opacity-90 transition"
              >
                Delete
              </button>
            </div>
          ))
        ) : (
          <p className="text-center col-span-full">No customers found</p>
        )}
      </div>
    </div>
  );
}

export default DisplayAccount;