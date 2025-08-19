import React, { useState } from 'react';

function SearchItems() {
  const [itemName, setItemName] = useState('');
  const [items, setItems] = useState([]);

  const searchItems = async () => {
    const res = await fetch(`http://localhost:8081/pahana-backend/searchItems?name=${itemName}`);
    const data = await res.json();
    setItems(data);
  };

  return (
    <div>
      <h2>Search Items</h2>
      <input
        placeholder="Item Name"
        value={itemName}
        onChange={(e) => setItemName(e.target.value)}
      />
      <button onClick={searchItems}>Search</button>
      <ul>
        {items.map((item) => (
          <li key={item.id}>{item.name} - ${item.price}</li>
        ))}
      </ul>
    </div>
  );
}

export default SearchItems;