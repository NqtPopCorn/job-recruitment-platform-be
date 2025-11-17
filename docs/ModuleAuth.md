
# Token 
Header
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```
Payload
```json
{
  "sub": "2",
  "email": "employer@google.com",
  "role": "ROLE_EMPLOYER",
  "type": "access",
  "iat": 1678886400,
  "exp": 9999990000,
  "iss": "localhost:3000",
  "aud": "localhost:3001"
}
```