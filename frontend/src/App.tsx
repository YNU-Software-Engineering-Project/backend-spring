import { Route, Routes } from 'react-router-dom';
import DefaultPage from 'pages/DefaultPage';
import TestPage from 'pages/TestPage';

function App() {
  return (
    <Routes>
      <Route path="/" element={<DefaultPage />} />
      <Route path="/test" element={<TestPage />} />
    </Routes>
  );
}

export default App;
