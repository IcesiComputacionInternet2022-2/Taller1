import Link from "next/link";


function Home() {


    return (
        <>
            <div className="hero min-h-screen bg-repeat bg-contain"
                 style={{backgroundImage: `url("/dragon-scales.svg")`}}>
                <div className="hero-overlay bg-opacity-60"></div>
                <div className="hero-content text-center text-neutral-content">
                    <div className="max-w-md">
                        <h1 className="mb-5 text-8xl font-bold animate-bounce">Zoo-App</h1>
                        <p className="mb-5 text-xl">This is a wep app to consume the Capybara&apos;s API.</p>
                        <Link href="/create" className="btn btn-primary rounded-3xl bg-base-100/60 border-white hover:bg-base-100 hover:border-white hover:scale-105 ease-in-out">Start</Link>
                    </div>
                </div>
            </div>
        </>
    )
}

export default Home