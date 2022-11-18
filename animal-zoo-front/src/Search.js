import React, { Component } from "react";

class Search extends Component {
  constructor(props) {
    super(props);
    this.state = {
      family: []
    };
    this.findFamily = this.findFamily.bind(this);
  }

  findFamily() {
    const name = document.getElementById("to-search").value;
    if(name === '') {
        return;
    }
    fetch(`http://localhost:8080/animal/${name}`)
      .then((res) => res.json())
      .then(
        (family) => {
          if("message" in family) {
            alert(family.message)
          } else {
            this.setState({ family: family });
          }
        },
        (error) => {
          alert(error);
        }
      );
  }

  render() {
    return (
      <div class="d-flex justify-content-center align-items-center h-100">
        <section class="vh-100 w-75" style={{ backgroundColor: "#2779e2" }}>
          <div class="card card-container text-center">
            <div class="card card-header">
              <div class="d-flex justify-content-center align-items-center">
                <input
                  id="to-search"
                  type="text"
                  class="form-control"
                  placeholder="Nombre del animal"
                  autocomplete="off"
                />
                <button
                  class="btn btn-outline-secondary"
                  type="button"
                  onClick={this.findFamily}
                >
                  Buscar
                </button>
              </div>
            </div>
            <div class="card card-header">
              <h1>Familia</h1>
            </div>
            <div class="card card-body">
              <table class="table table-striped">
                <thead>
                  <tr>
                    <th>Nombre</th>
                    <th>Padre</th>
                    <th>Madre</th>
                    <th>Sexo</th>
                    <th>Altura</th>
                    <th>Peso</th>
                    <th>Edad</th>
                    <th>Fecha de llegada</th>
                  </tr>
                </thead>
                <tbody>
                  {this.state.family.map((animal) => (
                    <tr>
                      <td>{animal.name}</td>
                      <td>{animal.maleParentName}</td>
                      <td>{animal.femaleParentName}</td>
                      <td>{animal.sex}</td>
                      <td>{animal.height}</td>
                      <td>{animal.weight}</td>
                      <td>{animal.age}</td>
                      <td>{animal.arrivalDate}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </section>
      </div>
    );
  }
}

export default Search;
