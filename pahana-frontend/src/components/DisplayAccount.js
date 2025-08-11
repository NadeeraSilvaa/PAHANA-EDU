import React, { useState } from 'react';

function DisplayAccount() {
  const [accNum, setAccNum] = useState('');
  const [details, setDetails] = useState(null);

  const doDisplay = async () => {
    const res = await fetch(`http://localhost:8080/displayAccount?accountNumber=${accNum}`);
    const data = await res.json();
    setDetails(data);
  };

  return (
    <div className="form-box">
      <h3>Show Details</h3>
      <input placeholder="Acc Num" type="number" value={accNum} onChange={(e) => setAccNum(e.target.value)} />
      <button onClick={doDisplay}>Show</button>
      {details && (
        <table className="table-details">
          <tbody>
            <tr><td>Name</td><td>{details.name}</td></tr>
            <tr><td>Addr</td><td>{details.address}</td></tr>
            <tr><td>Phone</td><td>{details.phone}</td></tr>
            <tr><td>Units</td><td>{details.units}</td></tr>
          </tbody>
        </table>
      )}
    </div>
  );
}

export default DisplayAccount;