# Production-Grade OAuth 2.0 Authorization Server

This is a production grade OAuth 2.0 Authorization Server and OpenID Connect (OIDC) Identity Provider, built on the latest standards using Spring Boot 3 and the official Spring Authorization Server framework. It provides a secure, centralized, and persistent solution for modern application authentication and authorization.

The server is designed for enterprise use cases, featuring dynamic client and user registration, JWT-based token issuance, and persistent storage of all security-related entities in a PostgreSQL database.

## Core Concepts: OAuth 2.0 and OpenID Connect

This server acts as the central authority in a modern security architecture. To understand its role, it's crucial to understand the protocols it implements.

### What is OAuth 2.0?

OAuth 2.0 is an authorization framework that enables applications to obtain limited access to user accounts on an HTTP service. It works by delegating user authentication to the service that hosts the user account and authorizing third-party applications to access that user account. The key concept is **delegated authority**—a user can grant an application access to their resources without ever giving it their password.

The primary roles in an OAuth 2.0 flow are:

- **Resource Owner**: The user who owns the data.
- **Client**: The application that wants to access the user's data.
- **Authorization Server (This Project)**: The server that authenticates the Resource Owner and issues Access Tokens to the Client.
- **Resource Server**: The server hosting the user's data, which accepts the Access Token.

### What is OpenID Connect (OIDC)?

OpenID Connect 1.0 is a simple identity layer built on top of the OAuth 2.0 protocol. While OAuth 2.0 is about authorization (what you can do), OIDC is about authentication (who you are). It allows clients to verify the identity of the end-user based on the authentication performed by an Authorization Server, as well as to obtain basic profile information about the end-user in an interoperable and REST-like manner. OIDC introduces the concept of an ID Token, a special JSON Web Token (JWT) that contains claims about the authenticated user.

## Features Supported by This Implementation

This server provides a robust implementation of the core OAuth 2.0 and OIDC specifications.

- **Authorization Code Grant**: The primary and most secure OAuth 2.0 flow. It is designed for server-side applications where the source code is not exposed to the public. The server exchanges an authorization code for an access token.

- **Refresh Tokens**: The server issues refresh tokens, allowing clients to obtain new access tokens without requiring the user to log in again, providing a seamless user experience.

- **OpenID Connect (OIDC) Support**: Fully supports OIDC by issuing ID Tokens when the `openid` scope is requested. This allows clients to receive verifiable identity information about the user.

- **JWT as Access Tokens**: All access tokens are issued as self-contained JSON Web Tokens (JWTs), signed with an RSA key pair, allowing resource servers to validate them offline without calling the authorization server.

- **Client Authentication**: Supports `client_secret_basic` for confidential clients to securely authenticate with the token endpoint.

- **Dynamic Registration**:
  - **Clients**: Applications (clients) can be dynamically registered via a RESTful endpoint (`POST /registration/client`).
  - **Users**: End-users (resource owners) can be registered via a RESTful endpoint (`POST /registration/user`).

## Technical Architecture

- **Java Version**: 21
- **Frameworks**: Spring Boot 3, Spring Security 6, Spring Authorization Server
- **Database**: PostgreSQL with JDBC, JPA/Hibernate
- **Database Migration**: Liquibase for schema management
- **Containerization**: Docker Compose for managing the PostgreSQL database and PgAdmin services

## Database Schema

This project uses the official database schema required by the Spring Authorization Server framework. The initial Liquibase migration script (`V1__create_oauth2_schema.sql`) sets up the following critical tables:

- **oauth2_registered_client**: Stores information about all registered client applications, including their IDs, secrets, grant types, redirect URIs, and scopes.
- **oauth2_authorization**: This is the core table that stores the state of each authorization grant, including authorization codes, access tokens, and refresh tokens.
- **oauth2_authorization_consent**: Manages user consents, remembering which permissions a user has granted to a specific client.

In addition to the standard Spring schema, a custom `resource_owner` table is used to store user credentials and profile information, managed by a separate Liquibase script (`20-01-changelog.sql`).

## Getting Started

### 1. Prerequisites

- Java 21 or higher
- Docker and Docker Compose

### 2. Environment Configuration

Before running, you must configure the following environment variables. You can create a `.env` file in the project root to store these.

```bash
# Database Configuration
POSTGRES_DB=oauth2-db
POSTGRES_USER=user
POSTGRES_PASSWORD=password

# PGAdmin Configuration (Optional)
PGADMIN_DEFAULT_EMAIL=admin@example.com
PGADMIN_DEFAULT_PASSWORD=admin

# JWT Signing Keys (Generate these securely)
RSA_PUBLIC_KEY="-----BEGIN PUBLIC KEY-----...-----END PUBLIC KEY-----"
RSA_PRIVATE_KEY="-----BEGIN PRIVATE KEY-----...-----END PRIVATE KEY-----"
```

### 3. Start the Database

Use Docker Compose to start the PostgreSQL database container.

```bash
docker-compose up -d
```

### 4. Run the Application

Start the Spring Boot authorization server.

```bash
./mvnw spring-boot:run
```

The server will start on `http://localhost:8080`.

## End-to-End Flow Example: Authorization Code Grant

This example demonstrates the complete process of a user authorizing a client application to access their data.

### Step 1: Register a User (Resource Owner)

First, create a user in the system.

```bash
curl -X POST http://localhost:8080/registration/user \
-H "Content-Type: application/json" \
-d '{
    "firstName": "John",
    "lastName": "Doe",
    "password": "password123",
    "birthDate": "1990-01-01",
    "userName": "john.doe"
}'
```

### Step 2: Register a Client Application

Next, register the application that will request access to the user's data.

```bash
curl -X POST http://localhost:8080/registration/client \
-H "Content-Type: application/json" \
-d '{
    "clientId": "my-client-app",
    "clientSecret": "secret",
    "redirectUris": ["http://127.0.0.1:8080/authorized"],
    "scopes": ["openid", "profile"],
    "grantTypes": ["authorization_code", "refresh_token"]
}'
```

### Step 3: Initiate the Authorization Flow

In a real-world scenario, the client application would redirect the user's browser to this URL. You can paste this into your browser.

```
http://localhost:8080/oauth2/authorize?response_type=code&client_id=my-client-app&redirect_uri=http://127.0.0.1:8080/authorized&scope=openid%20profile
```

- `response_type=code`: Specifies the Authorization Code Grant.
- `client_id`: Identifies the client registered in Step 2.
- `redirect_uri`: The location to send the user back to after authorization.
- `scope`: The permissions the client is requesting (`openid` for OIDC, `profile` for user info).

### Step 4: Authenticate and Grant Consent

Your browser will be redirected to the server's login page.

1. Enter the credentials for the user you created: Username: `john.doe`, Password: `password123`.
2. After successful login, you will be presented with a consent screen asking if you want to grant "my-client-app" access to your profile.
3. Click "Accept".

### Step 5: Receive the Authorization Code

The server will redirect your browser to the `redirect_uri` with an authorization code appended as a query parameter. The URL will look like this:

```
http://127.0.0.1:8080/authorized?code=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
```

**Note**: In a real application, the client would handle this redirect and extract the code. Since we don't have a live client at that URL, your browser will show a 404, but the code in the URL is still valid. Copy this code.

### Step 6: Exchange the Code for Tokens

The client application now takes this code and securely exchanges it for an access token, refresh token, and ID token by making a POST request to the `/oauth2/token` endpoint.

```bash
curl -X POST http://localhost:8080/oauth2/token \
-H "Content-Type: application/x-www-form-urlencoded" \
-u "my-client-app:secret" \
-d "grant_type=authorization_code&code=PASTE_THE_CODE_FROM_PREVIOUS_STEP&redirect_uri=http://127.0.0.1:8080/authorized"
```

- `-u "my-client-app:secret"`: This uses HTTP Basic Authentication to send the client's credentials.

### Step 7: Receive and Use the Tokens

The server will respond with a JSON payload containing the tokens:

```json
{
    "access_token": "eyJhbGciOiJSUzI1NiJ9...",
    "refresh_token": "wFz....",
    "scope": "openid profile",
    "id_token": "eyJhbGciOiJSUzI1NiJ9...",
    "token_type": "Bearer",
    "expires_in": 299
}
```

- **access_token**: The client can now use this Bearer token to make authenticated requests to a resource server.
- **id_token**: This is the OIDC token. You can decode this JWT (e.g., at jwt.io) to see the user's identity information (like `sub` for subject, which is `john.doe`).

**You have successfully completed a full, secure OAuth 2.0 and OIDC flow!**
