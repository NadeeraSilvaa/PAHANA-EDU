import React, { useState } from 'react';

function ManageItems() {
  const [act, setAct] = useState('add');
  const [id, setId] = useState('');
  const [name, setName] = useState('');
  const [price, setPrice] = useState('');

  const doManage = async () => {
    const params = new URLSearchParams({ action: act });
    if (act !== 'add') params.append('id', id);
    if (act !== 'delete') {
      params.append('name', name);
      params.append('price', price);
    }
    const res = await fetch('http://localhost:8080/manageItem', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: params
    });
    const data = await res.json();
    if (data.ok) {
      alert(`${act} done`);
    } else {
      alert('Error');
    }
  };

  return (
    <div className="form-box">
      <h3>Manage Items</h3>
      <select value={act} onChange={(e) => setAct(e.target.value)}>
        <option value="add">Add</option>
        <option value="update">Update</option>
        <option value="delete">Delete</option>
      </select>
      {(act === 'update' || act === 'delete') && <input placeholder="ID" type="number" value={id} onChange={(e) => setId(e.target.value)} />}
      {act !== 'delete' && <input placeholder="Name" value={name} onChange={(e) => setName(e.target.value)} />}
      {act !== 'delete' && <input placeholder="Price" type="number" value={price} onChange={(e) => setPrice(e.target.value)} />}
      <button onClick={doManage}>Do</button>
    </div>
  );
}

export default ManageItems;