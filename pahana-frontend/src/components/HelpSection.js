// src/components/HelpSection.js
import React, { useEffect, useState } from 'react';
import { useTheme } from '../ThemeContext';

function HelpSection() {
  const { darkMode } = useTheme();
  const [text, setText] = useState('');

  useEffect(() => {
    fetch('http://localhost:8081/pahana-backend/help')
      .then(res => res.json())
      .then(data => setText(data.help));
  }, []);

  return (
    <div className={`p-6 rounded-lg shadow-md ${darkMode ? 'bg-cardDark text-textDark' : 'bg-cardLight text-textLight'}`}>
      <h3 className="text-xl font-semibold mb-4">Help & Guidelines</h3>
      <p className="whitespace-pre-wrap text-textSecondaryLight dark:text-textSecondaryDark">{text || 'Loading help content...'}</p>
    </div>
  );
}

export default HelpSection;