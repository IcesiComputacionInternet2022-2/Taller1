/** @type {import('next').NextConfig} */
const nextConfig = {
	reactStrictMode: true,
	swcMinify: true,
	images: {
		domains: ['animals.sandiegozoo.org'],
	},
}

module.exports = nextConfig
