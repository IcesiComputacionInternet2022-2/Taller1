// Next.js API route support: https://nextjs.org/docs/api-routes/introduction
import type {NextApiRequest, NextApiResponse} from 'next'

export default async function handler(
    req: NextApiRequest,
    res: NextApiResponse
) {
    if (req.method === 'POST') {

        const {name} = req.body
        const response = await fetch('http://localhost:8080/capybaras', {
            method: req.method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(req.body),
        })
        const data = await response.json()
        res.status(response.status).json(data)

    }

    res.status(400)
}
