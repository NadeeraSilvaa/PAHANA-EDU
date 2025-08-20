import React, { useState, useEffect } from 'react';
import { useTheme } from '../ThemeContext';

function CalculateBill() {
  const { darkMode } = useTheme();
  const [customerId, setCustomerId] = useState('');
  const [customerName, setCustomerName] = useState('');
  const [books, setBooks] = useState([]);
  const [selectedBooks, setSelectedBooks] = useState([]);
  const [total, setTotal] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);
  const [userRole, setUserRole] = useState('');

  // Fetch user role from token on mount
  useEffect(() => {
    const token = localStorage.getItem('authToken'); // Assume token is stored here after login
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

  // Fetch all books on mount
  useEffect(() => {
    fetch('http://localhost:8081/pahana-backend/getAllBooks')
      .then(res => res.json())
      .then(data => {
        console.log('Fetched books:', data);
        setBooks(Array.isArray(data) ? data : []);
        setLoading(false);
      })
      .catch(() => {
        setError('Failed to load books');
        setLoading(false);
      });
  }, []);

  // Fetch customer name when customerId changes
  useEffect(() => {
    if (customerId) {
      fetch(`http://localhost:8081/pahana-backend/getCustomer?accountNumber=${customerId}`)
        .then(res => res.json())
        .then(data => {
          if (data.ok) {
            setCustomerName(data.customer.name || 'Unknown');
          } else {
            setCustomerName('Unknown');
            setError(data.message || 'Customer not found');
          }
        })
        .catch(() => {
          setCustomerName('Unknown');
          setError('Failed to fetch customer');
        });
    } else {
      setCustomerName('');
    }
  }, [customerId]);

  const addBookSelection = () => {
    setSelectedBooks([...selectedBooks, { id: '', quantity: '' }]);
  };

  const updateBookSelection = (index, field, value) => {
    const updatedBooks = [...selectedBooks];
    updatedBooks[index][field] = field === 'id' ? String(value) : value;
    setSelectedBooks(updatedBooks);
  };

  const removeBookSelection = (index) => {
    setSelectedBooks(selectedBooks.filter((_, i) => i !== index));
  };

  const doCalc = async () => {
    if (!userRole) {
      setError('Please log in to calculate bills');
      return;
    }
    if (!['Admin', 'Cashier'].includes(userRole)) {
      setError('Access denied: Insufficient permissions');
      return;
    }
    if (!customerId || selectedBooks.length === 0 || selectedBooks.some(book => !book.id || !book.quantity)) {
      setError('Please fill customer ID and all book selections');
      return;
    }
    setError('');

    const bookData = selectedBooks.map(book => ({
      bookId: String(book.id),
      quantity: parseInt(book.quantity)
    }));
    console.log('Sending book data:', bookData);

    try {
      const res = await fetch('http://localhost:8081/pahana-backend/calculateBill', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${localStorage.getItem('authToken')}` },
        body: JSON.stringify({ customerId, books: bookData })
      });
      const data = await res.json();
      console.log('Backend response:', data);
      if (data.ok) {
        setTotal(data.total);
      } else {
        setError(data.error || 'Calculation failed');
      }
    } catch {
      setError('Failed to connect to server');
    }
  };

  const doPrint = () => {
    window.print();
  };

  if (loading) return <p className="text-center text-textLight dark:text-textDark">Loading books...</p>;
  if (error && !books.length) return <p className="text-error text-center">{error}</p>;
  if (!userRole) return <p className="text-error text-center">Please log in to access this page</p>;

  const currentDate = new Date().toLocaleString('en-US', { timeZone: 'Asia/Kolkata' });

  return (
    <div className={`p-6 rounded-lg shadow-md ${darkMode ? 'bg-cardDark text-textDark' : 'bg-cardLight text-textLight'}`}>
      <style>
        {`
          @media print {
            body * { visibility: hidden; }
            .receipt, .receipt * { visibility: visible; }
            .receipt { position: absolute; left: 0; top: 0; width: 100%; padding: 20px; }
            .no-print { display: none; }
          }
        `}
      </style>
      <h3 className="text-xl font-semibold mb-4 no-print">Calculate Bill</h3>
      {error && <p className="text-error mb-4 no-print">{error}</p>}
      <div className="space-y-4 no-print">
        <input
          className={`w-full p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary ${darkMode ? 'bg-backgroundDark border-borderDark text-textDark' : 'bg-white border-borderLight text-textLight'}`}
          placeholder="Customer ID"
          type="number"
          value={customerId}
          onChange={(e) => setCustomerId(e.target.value)}
        />
        <div className="space-y-2">
          <h4 className="text-lg font-medium">Select Books</h4>
          {selectedBooks.map((book, index) => (
            <div key={index} className="flex space-x-2 items-center">
              <select
                value={book.id}
                onChange={(e) => updateBookSelection(index, 'id', e.target.value)}
                className={`flex-1 p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary ${darkMode ? 'bg-backgroundDark border-borderDark text-textDark' : 'bg-white border-borderLight text-textLight'}`}
              >
                <option value="">Select a book</option>
                {books.map(book => (
                  <option key={book.id} value={String(book.id)}>{book.name} - ${book.price?.toFixed(2)}</option>
                ))}
              </select>
              <input
                className={`w-24 p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary ${darkMode ? 'bg-backgroundDark border-borderDark text-textDark' : 'bg-white border-borderLight text-textLight'}`}
                placeholder="Qty"
                type="number"
                min="1"
                value={book.quantity}
                onChange={(e) => updateBookSelection(index, 'quantity', e.target.value)}
              />
              <button
                onClick={() => removeBookSelection(index)}
                className="p-2 bg-red-500 text-white rounded-md hover:opacity-90 transition"
              >
                Remove
              </button>
            </div>
          ))}
          <button
            onClick={addBookSelection}
            className="py-2 px-4 bg-secondary text-white rounded-md hover:opacity-90 transition"
          >
            Add Book
          </button>
        </div>
        <button
          onClick={doCalc}
          className="w-full py-3 bg-primary text-white rounded-md hover:opacity-90 transition"
        >
          Calculate
        </button>
      </div>
      {total !== null && (
        <div className="mt-4 p-4 border rounded-md border-borderLight dark:border-borderDark receipt">
          <h4 className="text-lg font-bold mb-2">Bill Receipt</h4>
          <p className="mb-2">Customer ID: {customerId}</p>
          <p className="mb-2">Customer Name: {customerName}</p>
          <p className="mb-2">Date: {currentDate}</p>
          <table className="w-full border-collapse">
            <thead>
              <tr className={`border-b ${darkMode ? 'border-borderDark' : 'border-borderLight'}`}>
                <th className="text-left p-2">Book Name</th>
                <th className="text-right p-2">Price</th>
                <th className="text-right p-2">Quantity</th>
                <th className="text-right p-2">Subtotal</th>
              </tr>
            </thead>
            <tbody>
              {selectedBooks.map((book, index) => {
                const selectedBook = books.find(b => String(b.id) === String(book.id));
                if (!selectedBook) {
                  console.warn(`Book with ID ${book.id} not found in books array`);
                  return (
                    <tr key={index} className={`border-b ${darkMode ? 'border-borderDark' : 'border-borderLight'}`}>
                      <td className="p-2 text-error">Unknown Book (ID: {book.id})</td>
                      <td className="text-right p-2">-</td>
                      <td className="text-right p-2">{book.quantity}</td>
                      <td className="text-right p-2">-</td>
                    </tr>
                  );
                }
                return (
                  <tr key={index} className={`border-b ${darkMode ? 'border-borderDark' : 'border-borderLight'}`}>
                    <td className="p-2">{selectedBook.name}</td>
                    <td className="text-right p-2">${selectedBook.price?.toFixed(2) || 'N/A'}</td>
                    <td className="text-right p-2">{book.quantity}</td>
                    <td className="text-right p-2">${((selectedBook.price || 0) * book.quantity).toFixed(2)}</td>
                  </tr>
                );
              })}
            </tbody>
            <tfoot>
              <tr>
                <td colSpan="3" className="p-2 font-bold text-right">Total Amount:</td>
                <td className="p-2 font-bold text-right">${total.toFixed(2)}</td>
              </tr>
            </tfoot>
          </table>
          <button
            onClick={doPrint}
            className="mt-4 py-2 px-4 bg-secondary text-white rounded-md hover:opacity-90 transition no-print"
          >
            Print Bill
          </button>
        </div>
      )}
    </div>
  );
}

export default CalculateBill;