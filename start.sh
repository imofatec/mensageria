docker-compose down
docker build -t producer-service:latest ./producer_service
docker build -t confirmation-email-service:latest ./confirmation_email_service
docker-compose up --build --force-recreate --remove-orphans
