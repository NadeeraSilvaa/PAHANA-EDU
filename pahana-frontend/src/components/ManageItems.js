import React, { useState } from 'react';
import { useTheme } from '../ThemeContext';

function ManageItems() {
  const { darkMode } = useTheme();
  const [act, setAct] = useState('add');
  const [id, setId] = useState('');
  const [name, setName] = useState('');
  const [price, setPrice] = useState('');
  const [author, setAuthor] = useState('');
  const [category, setCategory] = useState('');
  const [image, setImage] = useState(null);
  const [error, setError] = useState('');

  const doManage = async () => {
    if (act !== 'delete' && (!name || !price || !author || !category)) {
      setError('Please fill name, price, author, and category');
      return;
    }
    if (act !== 'add' && !id) {
      setError('Please enter ID for update/delete');
      return;
    }
    setError('');

    const formData = new FormData();
    formData.append('action', act);
    if (act !== 'add') formData.append('id', id);
    if (act !== 'delete') {
      formData.append('name', name);
      formData.append('price', price);
      formData.append('author', author);
      formData.append('category', category);
      if (image) formData.append('image', image);
    }

    try {
      const res = await fetch('http://localhost:8081/pahana-backend/manageItem', {
        method: 'POST',
        body: formData,
      });
      const data = await res.json();
      if (data.ok) {
        alert(`${act.charAt(0).toUpperCase() + act.slice(1)} successful`);
        // Reset form after successful operation
        setId('');
        setName('');
        setPrice('');
        setAuthor('');
        setCategory('');
        setImage(null);
      } else {
        setError('Operation failed');
      }
    } catch {
      setError('Failed to connect to server');
    }
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      if (!file.type.startsWith('image/')) {
        setError('Please select an image file');
        return;
      }
      if (file.size > 5 * 1024 * 1024) { // 5MB limit
        setError('Image size must be less than 5MB');
        return;
      }
      setImage(file);
      setError('');
    }
  };

  return (
    <div className={`p-6 rounded-lg shadow-md ${darkMode ? 'bg-cardDark text-textDark' : 'bg-cardLight text-textLight'}`}>
      <h3 className="text-xl font-semibold mb-4">Manage Books</h3>
      {error && <p className="text-error mb-4">{error}</p>}
      <select
        value={act}
        onChange={(e) => setAct(e.target.value)}
        className={`w-full p-3 border rounded-md mb-4 focus:outline-none focus:ring-2 focus:ring-primary ${darkMode ? 'bg-backgroundDark border-borderDark text-textDark' : 'bg-white border-borderLight text-textLight'}`}
      >
        <option value="add">Add Book</option>
        <option value="update">Update Book</option>
        <option value="delete">Delete Book</option>
      </select>
      {(act === 'update' || act === 'delete') && (
        <input
          className={`w-full p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary mb-4 ${darkMode ? 'bg-backgroundDark border-borderDark text-textDark' : 'bg-white border-borderLight text-textLight'}`}
          placeholder="Book ID"
          type="number"
          value={id}
          onChange={(e) => setId(e.target.value)}
        />
      )}
      {act !== 'delete' && (
        <>
          <input
            className={`w-full p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary mb-4 ${darkMode ? 'bg-backgroundDark border-borderDark text-textDark' : 'bg-white border-borderLight text-textLight'}`}
            placeholder="Book Name"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
          <input
            className={`w-full p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary mb-4 ${darkMode ? 'bg-backgroundDark border-borderDark text-textDark' : 'bg-white border-borderLight text-textLight'}`}
            placeholder="Price"
            type="number"
            step="0.01"
            value={price}
            onChange={(e) => setPrice(e.target.value)}
          />
          <input
            className={`w-full p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary mb-4 ${darkMode ? 'bg-backgroundDark border-borderDark text-textDark' : 'bg-white border-borderLight text-textLight'}`}
            placeholder="Author"
            value={author}
            onChange={(e) => setAuthor(e.target.value)}
          />
          <select
            value={category}
            onChange={(e) => setCategory(e.target.value)}
            className={`w-full p-3 border rounded-md mb-4 focus:outline-none focus:ring-2 focus:ring-primary ${darkMode ? 'bg-backgroundDark border-borderDark text-textDark' : 'bg-white border-borderLight text-textLight'}`}
          >
            <option value="">Select Category</option>
            <option value="Fiction">Fiction</option>
            <option value="Non-Fiction">Non-Fiction</option>
            <option value="Science">Science</option>
            <option value="History">History</option>
            <option value="Biography">Biography</option>
            <option value="Education">Education</option>
          </select>
          <input
            type="file"
            accept="image/*"
            onChange={handleImageChange}
            className={`w-full p-3 border rounded-md mb-4 ${darkMode ? 'bg-backgroundDark border-borderDark text-textDark' : 'bg-white border-borderLight text-textLight'}`}
          />
        </>
      )}
      <button
        onClick={doManage}
        className="w-full py-3 bg-primary text-white rounded-md hover:opacity-90 transition"
      >
        Perform Action
      </button>
    </div>
  );
}

export default ManageItems;