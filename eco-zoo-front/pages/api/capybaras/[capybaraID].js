export default async function handler(req, res) {
	if (req.method === 'GET') {
		const {capybaraID} = req.query

		const response = await fetch("http://localhost:8080/capybaras/" + capybaraID)
		const data = await response.json()

		res.status(response.status).json(data)
	}

	res.status(405).json({message: 'Method not allowed'})
}