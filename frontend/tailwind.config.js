// tailwind.config.js
export default {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        'realestate-primary': '#2563eb',
        'realestate-secondary': '#1d4ed8',
        'realestate-accent': '#93c5fd',
      },
    },
  },
  plugins: [],
}
