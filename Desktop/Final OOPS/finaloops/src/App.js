import logo from './logo.svg';
import './App.css';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import CustomerHome from './Pages/CustomerHome'
import Cart from './Pages/Cart';
import Detail from './Pages/Detail'


function App() {
  return (
    <div className="App">
      <BrowserRouter>
      <Routes>
      <Route path="/" element={<CustomerHome />} />
      <Route path="/cart" element={<Cart/>} />
      <Route path="/detail" element={<Detail/>} />
      </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
