import Navbar from "../components/navbar";
import {useState} from "react";
import Layout from "../components/layout";
import Link from "next/link";


function Home() {


    return (
    <><div className="hero min-h-screen bg-repeat bg-contain" style={{ backgroundImage: `url("/dragon-scales.svg")` }}>
                    <div className="hero-overlay bg-opacity-60"></div>
                    <div className="hero-content text-center text-neutral-content">
                      <div className="max-w-md">
                        <h1 className="mb-5 text-5xl font-bold">Zoo-App</h1>
                        <p className="mb-5">This is a wep app to create and manage the capybaras of a zoo.</p>
                        <Link href="/create" className="btn btn-primary">Get Started</Link>
                      </div>
                    </div>
                  </div></>
    )
}

export default Home