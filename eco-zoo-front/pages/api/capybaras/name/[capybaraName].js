export default async function handler(req, res) {
	if (req.method === 'GET') {
		const {capybaraName} = req.query

		const response = await fetch("http://localhost:8080/capybaras/name=" + capybaraName)
		const data = await response.json()

		res.status(response.status).json(data)
	}else
		res.status(405).json({message: 'Method not allowed'})
}