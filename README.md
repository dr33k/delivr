## Delivr
#### Logistics microservice built with Spring Boot. 
Scope: Food Delivery, Email Notification, Vendor Validation, JWT Authentication, Payment Processing, AWS EC2, AWS S3

Authentication: JWT

Database: Postgres

### Run

    mvnw clean package

to create jar

then run using docker compose (needs your env file)

    docker-compose up --build

or run with the java cli

    java -jar target/delivr-api-1.0-SNAPSHOT.jar

ENV variables required to build and run successfully are:
* DB_USER
* DB_PASSWORD
* DB_URL

The rest are required when making the associated requests:
#### For Authentication, OTP and Mail Dispatch
* MAIL_USERNAME: An SMTP username for mail dispatch. This is required to send OTPs also
* MAIL_PASSWORD: Password for the SMTP username
* JWT_SIGNING_KEY: A 256 bit string

#### For Payment processing
* FLUTTERWAVE_URL: The Flutterwave API's base URL for payment processing

#### For Nigerian Business verification
* CAC_URL: The Nigerian government's Corporate Affairs Commission API's URL for Business/Company verification

#### For AWS operations
* AWS_ACCESS_KEY_ID
* AWS_SECRET_ACCESS_KEY
* AWS_S3_BUCKET
* AWS_REGION

#### For SMS operations
* TWILIO_SID
* TWILIO_TOKEN
* TWILIO_FROM: The Sender's phone number

Then Visit: http://localhost:8000/swagger-ui.html
The 'rider' section is in progress