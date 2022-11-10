import Layout from "../components/layout";
import Image from "next/image";

export default function Create() {

    const handleOnSubmit = async (event: any) => {
        event.preventDefault();
        const data = new FormData(event.target);
        const body = {
            name: data.get("name"),
            sex: data.get("radio-sex"),
            weight: data.get("weight"),
            age: data.get("age"),
            height: data.get("height"),
            arrivalDate: data.get("date"),
            motherID: data.get("motherID"),
            fatherID: data.get("fatherID")
        }

        const res = await fetch('/api/capybaras', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body),
        })
        if(res.status===200){
            alert("Capybara created successfully")
        }
        else{
            const error = await res.json();
            console.log(error);
            alert(error.message)
        }
    }

    return (
        <div className="hero min-h-screen pb-16 bg-base-200">
            <div className="hero-content flex-col lg:flex-row-reverse">
                <div className="text-center lg:text-left lg:mx-8">
                    <h1 className="text-5xl font-bold text-center my-8">Create a capybara</h1>
                    <Image src="https://animals.sandiegozoo.org/sites/default/files/2016-10/animals_hero_capybara.jpg" className="max-w-lg m-auto rounded-lg shadow-2xl" width={500} height={500} alt="Capybara"/>
                </div>
                <div className="card flex-shrink-0 w-full max-w-lg shadow-2xl bg-base-100">
                    <div className="card-body">
                        <form onSubmit={handleOnSubmit}>
                            <div className="form-control">
                                <label className="label">
                                    <span className="label-text"><span className="text-red-500 align-middle">*</span> Name</span>
                                </label>
                                <input required name="name" type="text" placeholder="e.g: Ryan" className="input input-bordered grow"/>
                            </div>
                            <div className="form-control">
                                <label className="label">
                                    <span className="label-text"><span className="text-red-500 align-middle">*</span> Sex</span>
                                </label>
                                <label className="input-group">
                                    <label className="label cursor-pointer grow justify-start">
                                        <input type="radio" value="FEMALE" name="radio-sex"
                                               className="radio checked:bg-blue-500" defaultChecked/>
                                        <span className="label-text bg-transparent">Female</span>
                                    </label>
                                    <label className="label cursor-pointer grow justify-start">
                                        <input type="radio" value="MALE" name="radio-sex"
                                               className="radio checked:bg-blue-500"/>
                                        <span className="label-text bg-transparent">Male</span>
                                    </label>
                                </label>
                            </div>
                            <div className="form-control">
                                <label className="label">
                                    <span className="label-text"><span className="text-red-500 align-middle">*</span> Age</span>
                                </label>
                                <label className="input-group">
                                    <input required name="age" type="text" placeholder="e.g: 16" className="input input-bordered grow"/>
                                    <span>Years</span>
                                </label>
                            </div>
                            <div className="form-control">
                                <label className="label">
                                    <span className="label-text"><span className="text-red-500 align-middle">*</span> Height</span>
                                </label>
                                <label className="input-group">
                                    <input required name="height" type="text" placeholder="e.g: 1.5" className="input input-bordered  grow"/>
                                    <span>Mts</span>
                                </label>
                            </div>
                            <div className="form-control">
                                <label className="label">
                                    <span className="label-text"><span className="text-red-500 align-middle">*</span> Weight</span>
                                </label>
                                <label className="input-group">
                                    <input required name="weight" type="text" placeholder="e.g: 3" className="input input-bordered grow"/>
                                    <span>Kgs</span>
                                </label>
                            </div>
                            <div className="form-control">
                                <label className="label">
                                    <span className="label-text"><span className="text-red-500 align-middle">*</span> Arrival date</span>
                                </label>
                                <label className="input-group">
                                    <input required name="date" type="datetime-local" className="input input-bordered grow"/>
                                </label>
                            </div>
                            <div className="form-control">
                                <label className="label">
                                    <span className="label-text"> Mother ID</span>
                                </label>
                                <input name="motherID" type="text" placeholder="e.g: 53421b12-2e37-47d7-9576-d76f3bac17f9" className="input input-bordered grow"/>
                            </div>
                            <div className="form-control">
                                <label className="label">
                                    <span className="label-text"> Father ID</span>
                                </label>
                                <input name="fatherID" type="text" placeholder="e.g: 0583a217-7f82-4984-bc8e-83963e5fab00" className="input input-bordered grow"/>
                            </div>
                            <div className="form-control mt-6">
                                <button className="btn btn-primary">Create</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}
Create.getLayout = function getLayout(page: any) {
    return (<Layout>
        {page}
    </Layout>)
}