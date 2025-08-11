import React, { useState } from 'react';
import AddCustomer from './AddCustomer';
import EditCustomer from './EditCustomer';
import ManageItems from './ManageItems';
import DisplayAccount from './DisplayAccount';
import CalculateBill from './CalculateBill';
import HelpSection from './HelpSection';

function Dashboard({ setIsLogged }) {
  const [part, setPart] = useState('addCust');

  const doLogout = () => {
    setIsLogged(false);
  };

  return (
    <div className="dash">
      <h1>Pahana Edu App</h1>
      <nav className="side">
        <button onClick={() => setPart('addCust')}>Add Cust</button>
        <button onClick={() => setPart('editCust')}>Edit Cust</button>
        <button onClick={() => setPart('manageIt')}>Manage It</button>
        <button onClick={() => setPart('displayAcc')}>Show Details</button>
        <button onClick={() => setPart('calcB')}>Calc Bill</button>
        <button onClick={() => setPart('helpS')}>Help</button>
        <button onClick={doLogout}>Out</button>
      </nav>
      <div className="cont">
        {part === 'addCust' && <AddCustomer />}
        {part === 'editCust' && <EditCustomer />}
        {part === 'manageIt' && <ManageItems />}
        {part === 'displayAcc' && <DisplayAccount />}
        {part === 'calcB' && <CalculateBill />}
        {part === 'helpS' && <HelpSection />}
      </div>
    </div>
  );
}

export default Dashboard;