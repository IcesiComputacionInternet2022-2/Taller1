import Navbar from './navbar'
import {useState} from "react";
import {useRouter} from "next/router";

export default function Layout({children}: { children: any }) {

    const router = useRouter()

    const actualPath = router.pathname.replace('/', '')

    const [page, setPage] = useState(actualPath);
    return (
        <>
            <main>{children}</main>
            <Navbar page={page} setPage={setPage}/>
        </>
    )
}