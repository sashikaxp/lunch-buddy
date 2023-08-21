import React from 'react';
import './App.css';
import {Route, Routes} from 'react-router';
import MainLayout from './layouts/MainLayout';
import Home from "./pages/home/Home";
import CreateSession from "./pages/create-session/session";

function App() {
  return (
      <>
        <Routes>
          <Route>
            <Route element={<MainLayout />}>
              <Route path="/" element={<Home />} />
              <Route path="/create-session" element={<CreateSession />} />
            </Route>
          </Route>
        </Routes>
      </>
  );
}

export default App;
