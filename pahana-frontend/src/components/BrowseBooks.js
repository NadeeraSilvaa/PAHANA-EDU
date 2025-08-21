import React, { useState, useEffect } from 'react';
import { useTheme } from '../ThemeContext';
import { FaSearch } from 'react-icons/fa';

function BrowseBooks() {
  const { darkMode } = useTheme();
  const [books, setBooks] = useState([]);
  const [filteredBooks, setFilteredBooks] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
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

  useEffect(() => {
    if (userRole) {
      fetch('http://localhost:8081/pahana-backend/getAllBooks', {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('authToken')}` }
      })
        .then(res => res.json())
        .then(data => {
          const booksArray = Array.isArray(data) ? data : [];
          setBooks(booksArray);
          setFilteredBooks(booksArray);
          setLoading(false);
        })
        .catch(() => {
          setError('Failed to load books');
          setLoading(false);
        });
    }
  }, [userRole]);

  const handleSearch = async () => {
    if (!userRole || !['Admin', 'Cashier'].includes(userRole)) {
      setError('Access denied: Insufficient permissions');
      return;
    }
    if (!searchTerm) {
      setFilteredBooks(books);
      setError('');
      return;
    }

    try {
      const res = await fetch(`http://localhost:8081/pahana-backend/searchItems?name=${encodeURIComponent(searchTerm)}`, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('authToken')}` }
      });
      const data = await res.json();
      setFilteredBooks(Array.isArray(data) ? data : []);
      setError('');
    } catch {
      setError('Failed to search books');
      setFilteredBooks([]);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  if (!userRole) return <p className="text-error text-center">Loading role...</p>;
  if (!['Admin', 'Cashier'].includes(userRole)) return <p className="text-error text-center">Access denied</p>;
  if (loading) return <p className="text-center text-textLight dark:text-textDark">Loading books...</p>;
  if (error) return <p className="text-error text-center">{error}</p>;

  return (
    <div className={`p-6 rounded-lg shadow-md ${darkMode ? 'bg-cardDark text-textDark' : 'bg-cardLight text-textLight'}`}>
      <h3 className="text-xl font-semibold mb-6">Book Catalog</h3>
      
      {/* Search Bar */}
      <div className="flex items-center mb-6">
        <input
          className={`flex-1 p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary mr-2 ${darkMode ? 'bg-backgroundDark border-borderDark text-textDark' : 'bg-white border-borderLight text-textLight'}`}
          placeholder="Search by book name..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          onKeyPress={handleKeyPress}
        />
        <button
          onClick={handleSearch}
          className="p-3 bg-primary text-white rounded-md hover:opacity-90 transition flex items-center"
        >
          <FaSearch className="mr-2" /> Search
        </button>
      </div>

      {/* Book Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredBooks.length > 0 ? (
          filteredBooks.map((book) => (
            <div 
              key={book.id} 
              className={`p-4 border rounded-lg shadow hover:shadow-lg transition ${darkMode ? 'border-borderDark bg-backgroundDark' : 'border-borderLight bg-white'}`}
            >
              {book.imageUrl ? (
                <img src={`http://localhost:8081/pahana-backend/${book.imageUrl}`} alt={book.name} className="w-full h-48 object-cover rounded-md mb-4" />
              ) : (
                <div className="w-full h-48 bg-gray-200 dark:bg-gray-700 rounded-md mb-4 flex items-center justify-center text-textSecondaryLight dark:text-textSecondaryDark">No Image</div>
              )}
              <h4 className="text-lg font-bold">{book.name}</h4>
              <p className="text-textSecondaryLight dark:text-textSecondaryDark">{book.author ? `by ${book.author}` : 'No author available'}</p>
              <p className="text-primary font-semibold mt-2">LKR {book.price}</p>
              <p className="text-sm text-accent">{book.category || 'General'}</p>
            </div>
          ))
        ) : (
          <p className="text-center col-span-full text-textSecondaryLight dark:text-textSecondaryDark">No books found</p>
        )}
      </div>
    </div>
  );
}

export default BrowseBooks;