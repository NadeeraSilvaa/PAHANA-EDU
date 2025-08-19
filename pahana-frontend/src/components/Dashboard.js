import React, { useState } from 'react';
import AddCustomer from './AddCustomer';
import EditCustomer from './EditCustomer';
import ManageItems from './ManageItems';
import DisplayAccount from './DisplayAccount';
import CalculateBill from './CalculateBill';
import HelpSection from './HelpSection';
import ViewBills from './ViewBills'; // New component
import DeleteCustomer from './DeleteCustomer'; // New component
import SearchItems from './SearchItems'; // New component

function Dashboard({ setLoggedIn }) {
  const [part, setPart] = useState('addCust');

  const doLogout = () => {
     setLoggedIn(true); 
  };

  return (
    <div className="flex h-screen bg-gray-100">
      {/* Sidebar */}
      <nav className="w-64 bg-blue-800 text-white p-4 space-y-4">
        <h2 className="text-xl font-bold mb-4">Pahana Edu App</h2>
        <button
          onClick={() => setPart('addCust')}
          className="w-full text-left p-2 hover:bg-blue-700 rounded transition duration-200"
        >
          Add Customer
        </button>
        <button
          onClick={() => setPart('editCust')}
          className="w-full text-left p-2 hover:bg-blue-700 rounded transition duration-200"
        >
          Edit Customer
        </button>
        <button
          onClick={() => setPart('manageIt')}
          className="w-full text-left p-2 hover:bg-blue-700 rounded transition duration-200"
        >
          Manage Items
        </button>
        <button
          onClick={() => setPart('displayAcc')}
          className="w-full text-left p-2 hover:bg-blue-700 rounded transition duration-200"
        >
          Show Details
        </button>
        <button
          onClick={() => setPart('calcB')}
          className="w-full text-left p-2 hover:bg-blue-700 rounded transition duration-200"
        >
          Calculate Bill
        </button>
        <button
          onClick={() => setPart('viewBills')}
          className="w-full text-left p-2 hover:bg-blue-700 rounded transition duration-200"
        >
          View Bills
        </button>
        <button
          onClick={() => setPart('deleteCust')}
          className="w-full text-left p-2 hover:bg-blue-700 rounded transition duration-200"
        >
          Delete Customer
        </button>
        <button
          onClick={() => setPart('searchIt')}
          className="w-full text-left p-2 hover:bg-blue-700 rounded transition duration-200"
        >
          Search Items
        </button>
        <button
          onClick={() => setPart('helpS')}
          className="w-full text-left p-2 hover:bg-blue-700 rounded transition duration-200"
        >
          Help
        </button>
        <button
          onClick={doLogout}
          className="w-full text-left p-2 bg-red-600 hover:bg-red-500 rounded transition duration-200 mt-4"
        >
          Logout
        </button>
      </nav>

      {/* Content Area */}
      <div className="flex-1 p-6 overflow-y-auto">
        <div className="max-w-4xl mx-auto">
          {part === 'addCust' && <AddCustomer />}
          {part === 'editCust' && <EditCustomer />}
          {part === 'manageIt' && <ManageItems />}
          {part === 'displayAcc' && <DisplayAccount />}
          {part === 'calcB' && <CalculateBill />}
          {part === 'viewBills' && <ViewBills />}
          {part === 'deleteCust' && <DeleteCustomer />}
          {part === 'searchIt' && <SearchItems />}
          {part === 'helpS' && <HelpSection />}
        </div>
      </div>
    </div>
  );
}

export default Dashboard;