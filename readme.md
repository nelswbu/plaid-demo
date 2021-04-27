# Plaid Demo
A POC for RESTful Plaid endpoints.

## git it
```shell
git https://github.com/nelswbu/plaid-demo.git
```
## run it
```shell
cd plaid-demo &&
plaid_client_id=PLAID_CLIENT_ID \
plaid_secret=PLAID_SECRET \
server_port=SERVER_PORT \
./mvnw spring-boot:run &
```

## call it
```shell
# create a "link token"
curl -X POST 'http://localhost:%server_port%/api/create_link_token'; echo

# create a "public token" 
curl -X POST 'http://localhost:%server_port%/api/create_public_token'; echo

# exchange the "public token" for an "access key"
curl --data "public_token=PUBLIC_TOKEN" \
     -H 'Content-Type: application/x-www-form-urlencoded' \
     -X POST 'http://localhost:8012/api/exchange_public_token'; echo
```