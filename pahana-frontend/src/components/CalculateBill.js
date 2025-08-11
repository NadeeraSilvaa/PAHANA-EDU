import React, { useState } from 'react';

function CalculateBill() {
  const [custId, setCustId] = useState('');
  const [units, setUnits] = useState('');
  const [total, setTotal] = useState(null);

  const doCalc = async () => {
    const res = await fetch('http://localhost:8080/calculateBill', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: new URLSearchParams({ customerId: custId, units })
    });
    const data = await res.json();
    if (data.ok) {
      setTotal(data.total);
    } else {
      alert('Error');
    }
  };

  const doPrint = () => {
    window.print();
  };

  return (
    <div className="form-box">
      <h3>Calc Bill</h3>
      <input placeholder="Cust ID" type="number" value={custId} onChange={(e) => setCustId(e.target.value)} />
      <input placeholder="Units" type="number" value={units} onChange={(e) => setUnits(e.target.value)} />
      <button onClick={doCalc}>Calc</button>
      {total && (
        <div>
          <p>Total: {total}</p>
          <button onClick={doPrint}>Print</button>
        </div>
      )}
    </div>
  );
}

export default CalculateBill;