
## Example jwt test
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
Payload
```json
{
    "sub": "3",
    "email": "van.an.nguyen@example.com",
    "role": "ROLE_CANDIDATE",
    "type": "access",
    "iat": 1678886400,
    "exp": 9999990000,
    "iss": "localhost:3000",
    "aud": "localhost:3001"
}
```