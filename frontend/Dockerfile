FROM node:14.1-alpine

COPY package.json package-lock.json ./
RUN npm install

COPY . ./

EXPOSE 3000

CMD [ "npm", "run", "start" ]
