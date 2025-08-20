// src/components/Dashboard.js
import React, { useState } from 'react';
import {
  FaUserPlus, FaEdit, FaBoxOpen, FaEye, FaCalculator,
  FaFileInvoice, FaTrash, FaQuestionCircle,
  FaSignOutAlt, FaMoon, FaSun, FaBookOpen
} from 'react-icons/fa';
import AddCustomer from './AddCustomer';
import EditCustomer from './EditCustomer';
import ManageItems from './ManageItems';
import DisplayAccount from './DisplayAccount';
import CalculateBill from './CalculateBill';
import HelpSection from './HelpSection';
import ViewBills from './ViewBills';
import DeleteCustomer from './DeleteCustomer';
import BrowseBooks from './BrowseBooks';
import { useTheme } from '../ThemeContext';

function Dashboard({ setLoggedIn, setError }) {
  const { darkMode, setDarkMode } = useTheme();
  const [part, setPart] = useState('addCust');

  const doLogout = () => {
    localStorage.removeItem('authToken');
    setLoggedIn(false);
    setError('');
  };

  const menuItems = [
    { key: 'addCust', label: 'Add Customer', icon: <FaUserPlus /> },
    { key: 'editCust', label: 'Edit Customer', icon: <FaEdit /> },
    { key: 'manageIt', label: 'Manage Books', icon: <FaBoxOpen /> },
    { key: 'displayAcc', label: 'View Customer', icon: <FaEye /> },
    { key: 'calcB', label: 'Calculate Bill', icon: <FaCalculator /> },
    { key: 'viewBills', label: 'View Bills', icon: <FaFileInvoice /> },
    { key: 'deleteCust', label: 'Delete Customer', icon: <FaTrash /> },
    { key: 'browseBooks', label: 'Browse Books', icon: <FaBookOpen /> },
    { key: 'helpS', label: 'Help', icon: <FaQuestionCircle /> },
  ];

  return (
    <div className={`flex h-screen overflow-hidden ${darkMode ? 'bg-gray-900 text-gray-100' : 'bg-gray-100 text-gray-900'}`}>
      
      {/* Sidebar */}
      <nav className={`w-64 p-6 space-y-6 shadow-xl ${darkMode ? 'bg-gray-800' : 'bg-white'} rounded-r-2xl`}>
        <h2 className="text-2xl font-bold mb-8 flex items-center text-gradient-to-r from-blue-400 to-purple-500">
          <FaBookOpen className="mr-2" /> Pahana Edu
        </h2>

        <div className="space-y-2">
          {menuItems.map(item => (
            <button
              key={item.key}
              onClick={() => setPart(item.key)}
              className={`w-full flex items-center p-3 rounded-xl transition 
              hover:bg-gradient-to-r hover:from-blue-400 hover:to-purple-500 hover:text-white
              ${part === item.key ? 'bg-gradient-to-r from-blue-400 to-purple-500 text-white' : ''}`}
            >
              <span className="mr-3">{item.icon}</span> {item.label}
            </button>
          ))}
        </div>

        <div className="mt-6 space-y-2">
          <button
            onClick={doLogout}
            className="w-full flex items-center justify-center py-3 bg-red-500 text-white rounded-xl shadow-md hover:bg-red-600 transition"
          >
            <FaSignOutAlt className="mr-2" /> Logout
          </button>
          <button
            onClick={() => setDarkMode(!darkMode)}
            className="w-full flex items-center justify-center py-3 bg-gray-700 text-white rounded-xl shadow-md hover:bg-gray-600 transition"
          >
            {darkMode ? <FaSun className="mr-2" /> : <FaMoon className="mr-2" />} Toggle {darkMode ? 'Light' : 'Dark'}
          </button>
        </div>
      </nav>

      {/* Main Content */}
      <main className="flex-1 p-8 overflow-y-auto">
        <div className="max-w-6xl mx-auto bg-white dark:bg-gray-800 rounded-3xl p-8 shadow-lg transition">
          {part === 'addCust' && <AddCustomer />}
          {part === 'editCust' && <EditCustomer />}
          {part === 'manageIt' && <ManageItems />}
          {part === 'displayAcc' && <DisplayAccount />}
          {part === 'calcB' && <CalculateBill />}
          {part === 'viewBills' && <ViewBills />}
          {part === 'deleteCust' && <DeleteCustomer />}
          {part === 'browseBooks' && <BrowseBooks />}
          {part === 'helpS' && <HelpSection />}
        </div>
      </main>
    </div>
  );
}

export default Dashboard;
