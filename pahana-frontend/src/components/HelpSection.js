import React, { useEffect, useState } from 'react';

function HelpSection() {
  const [text, setText] = useState('');

  useEffect(() => {
    fetch('http://localhost:8080/help')
      .then(res => res.json())
      .then(data => setText(data.help));
  }, []);

  return (
    <div className="form-box">
      <h3>Help</h3>
      <p>{text}</p>
    </div>
  );
}

export default HelpSection;