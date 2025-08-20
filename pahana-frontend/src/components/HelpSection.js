import React, { useEffect, useState } from 'react';
import { useTheme } from '../ThemeContext';

function HelpSection() {
  const { darkMode } = useTheme();
  const [text, setText] = useState('');
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
            setUserRole('');
          }
        })
        .catch(() => {
          setUserRole('');
        });
    } else {
      setUserRole('');
    }
  }, []);

  useEffect(() => {
    if (userRole) {
      fetch('http://localhost:8081/pahana-backend/help', {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('authToken')}` }
      })
        .then(res => res.json())
        .then(data => setText(data.help));
    }
  }, [userRole]);

  if (!userRole) return <p className="text-error text-center">Loading role...</p>;
  if (!['Admin', 'Cashier'].includes(userRole)) return <p className="text-error text-center">Access denied</p>;

  return (
    <div className={`p-6 rounded-lg shadow-md ${darkMode ? 'bg-cardDark text-textDark' : 'bg-cardLight text-textLight'}`}>
      <h3 className="text-xl font-semibold mb-4">Help & Guidelines</h3>
      <p className="whitespace-pre-wrap text-textSecondaryLight dark:text-textSecondaryDark">{text || 'Loading help content...'}</p>
    </div>
  );
}

export default HelpSection;