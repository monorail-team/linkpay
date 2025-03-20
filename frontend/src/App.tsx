import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from '@/pages';
import Register from '@/pages/register';
const App: React.FC = () => {
  return (
    <div className="w-full h-full md:w-[456px] md:h-[820px] mx-auto border">
      <Router>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/register" element={<Register />} />
        </Routes>
      </Router>
    </div>
  );
};

export default App;
