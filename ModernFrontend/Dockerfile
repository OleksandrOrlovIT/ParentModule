# Use an official node image as the base image
FROM node:18-alpine3.19 as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the package.json file to the working directory
COPY package.json ./

# Install the dependencies
RUN npm install

# Copy the rest of the application code to the working directory
COPY . .

# Build the React application for production
RUN npm run build

# Use an official Nginx image to serve the built files
FROM nginx:alpine

# Copy the built files from the previous stage to the Nginx container
COPY --from=builder /app/build /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start the Nginx server
CMD ["nginx", "-g", "daemon off;"]