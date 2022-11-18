import React, { Component } from "react";
import {
  BrowserRouter,
  Route,
  Routes,
  NavLink
} from "react-router-dom";
import Create from "./Create";
import List from "./List";
import Search from "./Search";
 
class Main extends Component {
  render() {
    return (
      <BrowserRouter>
        <div>
          <h1>Aplicativo Zoo</h1>
          <ul className="header">
            <li><NavLink to="/">Crear Animal</NavLink></li>
            <li><NavLink to="/list">Listar Animales</NavLink></li>
            <li><NavLink to="/search">Buscar Animal</NavLink></li>
          </ul>
          <div className="content">
            <Routes>
	      <Route exact path="/" element={<Create />} />
              <Route path="/list" element={<List />} />
	      <Route path="/search" element={<Search />} />
            </Routes>
	   </div>
        </div>
      </BrowserRouter>
    );
  }
}

export default Main;
