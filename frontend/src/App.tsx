import { Route, Routes } from 'react-router-dom';

import DefaultPage from 'pages/DefaultPage';
import TestPage from 'pages/TestPage';
import SignUpTest from 'pages/SignUpTest';

function App() {
  return (
    <Routes>
      <Route path="/" element={<DefaultPage />} />
      <Route path="/register" element ={<SignUpTest />} />
      <Route path="/test" element={<TestPage />} />
    </Routes>
  );
}

export default App;
