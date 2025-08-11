import React, { useState } from 'react';

function EditCustomer() {
  const [accNum, setAccNum] = useState('');
  const [name, setName] = useState('');
  const [addr, setAddr] = useState('');
  const [phone, setPhone] = useState('');
  const [units, setUnits] = useState('');

  const doEdit = async () => {
    if (!accNum || !name || !addr || !phone || !units) {
      alert('Fill all');
      return;
    }
    const res = await fetch('http://localhost:8080/editCustomer', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: new URLSearchParams({ accountNumber: accNum, name, address: addr, phone, units })
    });
    const data = await res.json();
    if (data.ok) {
      alert('Updated');
    } else {
      alert('Error');
    }
  };

  return (
    <div className="form-box">
      <h3>Edit Cust</h3>
      <input placeholder="Acc Num" type="number" value={accNum} onChange={(e) => setAccNum(e.target.value)} />
      <input placeholder="Name" value={name} onChange={(e) => setName(e.target.value)} />
      <input placeholder="Addr" value={addr} onChange={(e) => setAddr(e.target.value)} />
      <input placeholder="Phone" value={phone} onChange={(e) => setPhone(e.target.value)} />
      <input placeholder="Units" type="number" value={units} onChange={(e) => setUnits(e.target.value)} />
      <button onClick={doEdit}>Update</button>
    </div>
  );
}

export default EditCustomer;