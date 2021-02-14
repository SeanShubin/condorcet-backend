# Condorcet Backend

## Prerequisites

Make sure you have a mysql database running configured with the settings listed in
[DatabaseConstants](console/src/main/kotlin/com/seanshubin/condorcet/backend/console/DatabaseConstants.kt)

## Scripts

- `./scripts/prepare.sh`
  - One time initialization
- `./scripts/run.sh`
  - Run application

## Notes

Looking into JWT

- https://github.com/auth0/java-jwt
- https://auth0.com/docs/tokens/json-web-tokens
- https://afteracademy.com/blog/implement-json-web-token-jwt-authentication-using-access-token-and-refresh-token

Could be useful for figuring out java keytool

- https://keystore-explorer.org/

## Encryption notes

- PKCS#1 PEM-encoded public key starts with `-----BEGIN RSA PUBLIC KEY-----`
- x.509 public key starts with `-----BEGIN PUBLIC KEY-----`