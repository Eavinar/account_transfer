# Running port.
Application will run on standart port of Jetty. 
http://localhost:4567

# Api requests:
Get Account
Accepts only account id.

```curl --location --request GET 'localhost:4567/api/account/1'```

---
Transfer Amount. Accepts debitted account id, credited account id and amount.

```
curl --location --request POST 'localhost:4567/api/transfer' \
 --header 'Content-Type: application/json' \
 --data-raw '{
     "debit": "1",
     "credit": "2",
     "amount": "10"
```  

---
Get Transfer id to validate. Accepts only transfer id.

```curl --location --request GET 'localhost:4567/api/transfer/1''```
