import React, { useState } from 'react';

function ViewBills() {
  const [bills, setBills] = useState([]);
  const [custId, setCustId] = useState('');

  const fetchBills = async () => {
  try {
    const res = await fetch(`http://localhost:8081/pahana-backend/viewBills?customerId=${custId}`);
    const data = await res.json();

    // if backend wraps bills inside { bills: [...] }
    if (Array.isArray(data)) {
      setBills(data);
    } else if (Array.isArray(data.bills)) {
      setBills(data.bills);
    } else {
      console.error("Unexpected response:", data);
      setBills([]); // fallback
    }
  } catch (err) {
    console.error("Fetch failed:", err);
    setBills([]);
  }
};


  return (
    <div>
      <h2>View Bills</h2>
      <input
        placeholder="Customer ID"
        value={custId}
        onChange={(e) => setCustId(e.target.value)}
      />
      <button onClick={fetchBills}>Fetch</button>
      <ul>
        {bills.map((bill) => (
          <li key={bill.id}>Amount: ${bill.billAmount}</li>
        ))}
      </ul>
    </div>
  );
}

export default ViewBills;