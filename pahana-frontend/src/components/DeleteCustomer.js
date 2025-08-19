import React, { useState } from 'react';

function DeleteCustomer() {
  const [accNum, setAccNum] = useState('');
  const [message, setMessage] = useState('');

  const deleteCust = async () => {
    const res = await fetch('http://localhost:8081/pahana-backend/deleteCustomer', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: new URLSearchParams({ accountNumber: accNum })
    });
    const data = await res.json();
    setMessage(data.ok ? 'Deleted successfully' : 'Delete failed');
  };

  return (
    <div>
      <h2>Delete Customer</h2>
      <input
        placeholder="Account Number"
        value={accNum}
        onChange={(e) => setAccNum(e.target.value)}
      />
      <button onClick={deleteCust}>Delete</button>
      <p>{message}</p>
    </div>
  );
}

export default DeleteCustomer;