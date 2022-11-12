export default function AnimalComponent({capybara}: { capybara: any }) {
    return (
        <div className="card w-96 bg-base-100 shadow-xl">
            <div className="card-body">
                <h2 className="card-title">ID</h2>
                <p>{capybara.id}</p>
                <h2 className="card-title">NAME</h2>
                <p>{capybara.name}</p>
                <h2 className="card-title">SEX</h2>
                <p>{capybara.sex}</p>
                <h2 className="card-title">WEIGHT</h2>
                <p>{capybara.weight}</p>
                <h2 className="card-title">AGE</h2>
                <p>{capybara.age}</p>
                <h2 className="card-title">HEIGHT</h2>
                <p>{capybara.height}</p>
                <h2 className="card-title">ARRIVAL DATE</h2>
                <p>{capybara.arrivalDate}</p>

            </div>
        </div>
    )
}