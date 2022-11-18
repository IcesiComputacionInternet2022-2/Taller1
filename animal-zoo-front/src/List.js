import React, { Component } from "react";

class List extends Component {
  constructor(props) {
    super(props);
    this.state = {
      animals: []
    };
  }

  componentDidMount() {
    fetch("http://localhost:8080/animal")
      .then((res) => res.json())
      .then(
        (animals) => {
          this.setState({ animals: animals });
        },
        (error) => {
          alert(error);
        }
      );
  }

  render() {
    return (
      <div class="d-flex justify-content-center align-items-center">
        <section class="w-75" style={{ backgroundColor: "#2779e2" }}>
          <div class="vh-100">
            <div class="d-flex justify-content-center align-items-center h-100">
              <div class="card card-container text-center">
                <div class="card card-header">
                    <h1>Listado</h1>
                </div>
                <div class="card card-body">
                  <table class="table table-striped">
                    <thead>
                      <tr>
                        <th>Nombre</th>
                        <th>Sexo</th>
                        <th>Altura</th>
                        <th>Peso</th>
                        <th>Edad</th>
                        <th>Fecha de llegada</th>
                      </tr>
                    </thead>
                    <tbody>
                      {this.state.animals.map((animal) => (
                        <tr>
                          <td>{animal.name}</td>
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
            </div>
          </div>
        </section>
      </div>
    );
  }
}

export default List;
