import React, { useEffect, useState } from 'react';
import { useTheme } from '../ThemeContext';
import { FaSearch } from 'react-icons/fa';

function BrowseBooks() {
  const { darkMode } = useTheme();
  const [books, setBooks] = useState([]);
  const [filteredBooks, setFilteredBooks] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // Fetch all books on component mount
  useEffect(() => {
    fetch('http://localhost:8081/pahana-backend/getAllBooks')
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
  }, []);

  // Handle search
  const handleSearch = async () => {
    if (!searchTerm) {
      setFilteredBooks(books); // Reset to all books if search term is empty
      setError('');
      return;
    }

    try {
      const res = await fetch(`http://localhost:8081/pahana-backend/searchItems?name=${encodeURIComponent(searchTerm)}`);
      const data = await res.json();
      setFilteredBooks(Array.isArray(data) ? data : []);
      setError('');
    } catch {
      setError('Failed to search books');
      setFilteredBooks([]);
    }
  };

  // Update filtered books when search term changes (optional: trigger search on Enter key)
  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

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
                <img src={book.imageUrl} alt={book.name} className="w-full h-48 object-cover rounded-md mb-4" />
              ) : (
                <div className="w-full h-48 bg-gray-200 dark:bg-gray-700 rounded-md mb-4 flex items-center justify-center text-textSecondaryLight dark:text-textSecondaryDark">No Image</div>
              )}
              <h4 className="text-lg font-bold">{book.name}</h4>
              <p className="text-textSecondaryLight dark:text-textSecondaryDark">{book.author ? `by ${book.author}` : 'No author available'}</p>
              <p className="text-primary font-semibold mt-2">${book.price}</p>
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