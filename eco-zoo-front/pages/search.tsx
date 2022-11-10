import Layout from "../components/layout";
import AnimalComponent from "../components/animalComponent"
import {useState} from "react";

const initialState = {
                                                                        "object":{},
                                                                        "state": "IDLE"
                                                                        }

export default function Search(){

    const [capybara, setCapybara] = useState(initialState)
    const [capybaraF, setCapybaraF] = useState(initialState)
    const [capybaraM, setCapybaraM] = useState(initialState)

    const [searchInput, setSearchInput] = useState("")

    const handleSearchInputChange = (e) =>{
        setSearchInput(e.target.value)
    }

    const handleSearch = async () => {

        setCapybara(initialState)
        setCapybaraM(initialState)
        setCapybaraF(initialState)

        const res = await fetch('api/capybaras/'+searchInput)
        const capybaraRes = await res.json()

        if (res.status === 200){
            setCapybara({"object":{
                "id":capybaraRes.id,
                "name":capybaraRes.name,
                "sex":capybaraRes.sex,
                "weight":capybaraRes.weight,
                "height":capybaraRes.height,
                "age":capybaraRes.age,
                "arrivalDate":capybaraRes.arrivalDate
                },
                "state": "FILLED"
            })

            if(capybaraRes.father !== undefined){
                setCapybaraF({
                "object":capybaraRes.father,
                "state" : "FILLED"
                })
            }
            if(capybaraRes.mother !== undefined){
                setCapybaraM({
                                "object":capybaraRes.mother,
                                "state" : "FILLED"
                                })
            }
            console.log(capybaraRes)
        }else alert(capybaraRes.message)
    }


    return (
        <div className="search-component bg-base-200 min-h-screen pb-16">
            <div className="form-control">

            <h1 className="text-5xl font-bold text-center my-8">Search a capybara</h1>

                <div className="input-group justify-center">
                    <input onChange={handleSearchInputChange} type="text" placeholder="Search…" className="input input-bordered" />
                    <button onClick={handleSearch} className="btn btn-square">
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" /></svg>
                    </button>
                </div>
            </div>
            <div className="flex flex-col">
            <div className="flex justify-center my-8">
            {capybara.state==="FILLED"&&(<AnimalComponent capybara= {capybara.object}/>)}
            </div>
            <div className="flex justify-evenly my-8">
                     {capybaraM.state==="FILLED"&&(<div className="flex flex-col">
                                                         <h2 className="text-xl font-bold text-center my-4">Mother</h2>
                                                         <AnimalComponent capybara = {capybaraM.object}/>
                                                         </div>)}
            {capybaraF.state==="FILLED"&&(<div className="flex flex-col">
                                             <h2 className="text-xl font-bold text-center my-4">Father</h2>
                                             <AnimalComponent capybara = {capybaraF.object}/>
                                             </div>)}
            </div>
            </div>


        </div>
    )
}
Search.getLayout = function getLayout(page: any) {
    return (<Layout>
        {page}
    </Layout>)
}