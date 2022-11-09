import Layout from "../components/layout";

export default function GetAll({capybaras}: {capybaras: any[]}){
    return (
        <div className="overflow-x-auto mb-16">
            <table className="table-compact table-zebra w-full">
                <thead>
                <tr>
                    <th></th>
                    {Object.keys(capybaras[0]).map((key, index) => {
                        return(
                            <th className="uppercase" key={index}>{key}</th>
                        )
                    })}
                </tr>
                </thead>
                <tbody>
                 {capybaras.map((capybara, index) => {
                        return(
                            <tr key={index}>
                                <td>{index+1}</td>
                                <td>{capybara.id}</td>
                                <td>{capybara.name}</td>
                                <td>{capybara.sex}</td>
                                <td>{capybara.weight}</td>
                                <td>{capybara.age}</td>
                                <td>{capybara.height}</td>
                                <td>{capybara.arrivalDate}</td>
                                <td>{capybara.motherID}</td>
                                <td>{capybara.fatherID}</td>
                            </tr>
                        )
                 })}
                </tbody>
            </table>
        </div>
    )
}
export async function getServerSideProps(context : any) {
    const res = await fetch('http://localhost:8080/capybaras')
    const capybaras = await res.json()

    return {
        props: {
            capybaras,
        },
    }
}

GetAll.getLayout = function getLayout(page: any) {
    return (<Layout>
        {page}
    </Layout>)
}