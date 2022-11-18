import React, { Component } from "react";

class Create extends Component {
  async handleOnSubmit(e) {
    e.preventDefault();
    const data = new FormData(e.target);
    const dto = {
      name: data.get("name"),
      maleParentName: data.get("father"),
      femaleParentName: data.get("mother"),
      sex: data.get("sex"),
      weight: data.get("weight"),
      height: data.get("height"),
      age: data.get("age"),
      arrivalDate: data.get("arrival-date")
    };
    const res = await fetch("http://localhost:8080/animal", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json"
      },
      body: JSON.stringify(dto)
    });
    const msg = await res.json();
    if (res.status === 200) {
      alert("Se ha registrado al animal exitosamente")
      e.target.reset();
    } else {
        alert(msg.message);
    }
  }

  render() {
    return (
      <div class="d-flex justify-content-center align-items-center h-100">
        <section class="vh-100 w-75" style={{ backgroundColor: "#2779e2" }}>
          <div class="vh-100">
            <div class="d-flex justify-content-center align-items-center h-100">
              <div
                class="card card-container text-center"
                style={{ width: 50 + "%", height: 850 + "px" }}
              >
                <div class="card-header">
                  <h1>Registrar</h1>
                </div>
                <div class="card-body">
                  <form onSubmit={this.handleOnSubmit} autocomplete="off">
                    <div class="d-flex flex-column justify-content-center align-items-center pt-4 pb-3">
                      <div class="d-flex flex-row align-items-center">
                        <div class="col-md-7 ps-3 d-flex justify-content-start">
                          <h6 class="mb-0">Nombre:</h6>
                        </div>
                        <div>
                          <input
                            type="text"
                            id="name"
                            name="name"
                            class="form-control form-control-lg border border-dark"
                            required
                          />
                        </div>
                      </div>
                      <br></br>
                      <div class="d-flex flex-row align-items-center">
                        <div class="col-md-7 ps-3 d-flex justify-content-start">
                          <h6 class="mb-0">Padre:</h6>
                        </div>
                        <div>
                          <input
                            type="text"
                            id="father"
                            name="father"
                            class="form-control form-control-lg border border-dark"
                          />
                        </div>
                      </div>
                      <br></br>
                      <div class="d-flex flex-row align-items-center">
                        <div class="col-md-7 ps-3 d-flex justify-content-start">
                          <h6 class="mb-0">Madre:</h6>
                        </div>
                        <div>
                          <input
                            type="text"
                            id="mother"
                            name="mother"
                            class="form-control form-control-lg border border-dark"
                          />
                        </div>
                      </div>
                      <br></br>
                      <div class="d-flex form-floating justify-content-center align-items-center">
                        <select
                          id="sex"
                          name="sex"
                          class="form-select border border-dark"
                          style={{ width: 345 + "px" }}
                        >
                          <option value={"M"}>Macho</option>
                          <option value={"H"}>Hembra</option>
                        </select>
                        <label for="role">Sexo:</label>
                      </div>
                      <br></br>
                      <div class="d-flex flex-row align-items-center">
                        <div class="col-md-7 ps-3 d-flex justify-content-start">
                          <h6 class="mb-0">Peso:</h6>
                        </div>
                        <div>
                          <input
                            type="number"
                            min="0"
                            id="weight"
                            name="weight"
                            class="form-control form-control-lg border border-dark"
                            required
                          />
                        </div>
                      </div>
                      <br></br>
                      <div class="d-flex flex-row align-items-center">
                        <div class="col-md-7 ps-3 d-flex justify-content-start">
                          <h6 class="mb-0">Altura:</h6>
                        </div>
                        <div>
                          <input
                            type="number"
                            min="0"
                            id="height"
                            name="height"
                            class="form-control form-control-lg border border-dark"
                            required
                          />
                        </div>
                      </div>
                      <br></br>
                      <div class="d-flex flex-row align-items-center">
                        <div class="col-md-7 ps-3 d-flex justify-content-start">
                          <h6 class="mb-0">Edad:</h6>
                        </div>
                        <div>
                          <input
                            type="number"
                            min="0"
                            id="age"
                            name="age"
                            class="form-control form-control-lg border border-dark"
                            required
                          />
                        </div>
                      </div>
                      <br></br>
                      <div class="d-flex flex-row align-items-center">
                        <div class="col-md-7 ps-3 d-flex justify-content-start">
                          <h6 class="mb-0">Fecha de llegada:</h6>
                        </div>
                        <div>
                          <input
                            type="date"
                            id="arrival-date"
                            name="arrival-date"
                            class="form-control form-control-lg border border-dark"
                            required
                          />
                        </div>
                      </div>
                      <br></br>
                    </div>
                    <div>
                      <button
                        type="submit"
                        class="btn btn-lg btn-primary btn-block"
                        style={{ backgroundColor: "#2779e2" }}
                      >
                        Registrar
                      </button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </section>
      </div>
    );
  }
}

export default Create;
