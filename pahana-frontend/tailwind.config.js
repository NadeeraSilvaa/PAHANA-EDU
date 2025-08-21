// tailwind.config.js
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#4A90E2', // Vibrant blue for buttons and accents
        secondary: '#50C878', // Fresh green for success messages
        accent: '#F5A623', // Warm orange for highlights
        error: '#E53E3E', // Red for errors
        backgroundLight: '#F9FAFB',
        cardLight: '#FFFFFF',
        textLight: '#2D3748',
        textSecondaryLight: '#718096',
        borderLight: '#E2E8F0',
        backgroundDark: '#1A202C',
        cardDark: '#2D3748',
        textDark: '#E2E8F0',
        textSecondaryDark: '#A0AEC0',
        borderDark: '#4A5568',
      },
    },
  },
  plugins: [],
  darkMode: 'class',
};