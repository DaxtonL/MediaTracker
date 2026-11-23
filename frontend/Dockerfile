# Build stage: produce the static Vite bundle.
FROM node:22-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npm run build

# Runtime stage: Caddy serves the bundle, proxies /api to the backend,
# and gets an HTTPS certificate automatically when SITE_ADDRESS is a domain.
FROM caddy:2-alpine
COPY Caddyfile /etc/caddy/Caddyfile
COPY --from=build /app/dist /srv
