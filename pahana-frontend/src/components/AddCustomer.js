import React, { useState } from 'react';

function AddCustomer() {
  const [name, setName] = useState('');
  const [addr, setAddr] = useState('');
  const [phone, setPhone] = useState('');
  const [units, setUnits] = useState('');

  const doAdd = async () => {
    if (!name || !addr || !phone || !units) {
      alert('Fill all');
      return;
    }
    const res = await fetch('http://localhost:8080/addCustomer', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: new URLSearchParams({ name, address: addr, phone, units })
    });
    const data = await res.json();
    if (data.ok) {
      alert('Added');
      setName(''); setAddr(''); setPhone(''); setUnits('');
    } else {
      alert('Error');
    }
  };

  return (
    <div className="form-box">
      <h3>Add New Cust</h3>
      <input placeholder="Name" value={name} onChange={(e) => setName(e.target.value)} />
      <input placeholder="Addr" value={addr} onChange={(e) => setAddr(e.target.value)} />
      <input placeholder="Phone" value={phone} onChange={(e) => setPhone(e.target.value)} />
      <input placeholder="Units" type="number" value={units} onChange={(e) => setUnits(e.target.value)} />
      <button onClick={doAdd}>Add</button>
    </div>
  );
}

export default AddCustomer;