FROM node:22.12-alpine AS build

WORKDIR /app

ARG VITE_BACKEND_URL

ENV VITE_BACKEND_URL=${VITE_BACKEND_URL}

RUN echo "VITE_BACKEND_URL=${VITE_BACKEND_URL}" > .env

COPY ./package.json ./

RUN npm install

COPY . .

RUN npm run build


FROM node:22.12-alpine

WORKDIR /app

RUN npm install -g serve

COPY --from=build /app/dist .

COPY --from=build /app/.env .env

EXPOSE 5173

CMD ["serve", "-s", ".", "-l", "tcp://0.0.0.0:5173"]