# VoteSphere — Your Vote, Your Voice

A Java 17 / Jakarta Servlet web application packaged as a Maven WAR for Apache Tomcat 10.1 and MySQL 8. The JSP interface is connected to database-backed registration, authentication, poll administration, voting, results, and private voting history.

## Requirements

- JDK 17
- Apache Maven 3.9+
- Apache Tomcat 10.1.x
- MySQL 8.0+

## Setup

1. Create the database and tables by running `src/main/resources/db/schema.sql` in MySQL Workbench or:

   ```powershell
   Get-Content -Raw .\src\main\resources\db\schema.sql | mysql.exe -u root -p
   ```

2. Configure the database connection before starting Tomcat. The defaults are URL `jdbc:mysql://localhost:3306/votesphere?useSSL=false&serverTimezone=UTC`, username `root`, and an empty password. For a local PowerShell session:

   ```powershell
   $env:VOTESPHERE_DB_URL = "jdbc:mysql://localhost:3306/votesphere?useSSL=false&serverTimezone=UTC"
   $env:VOTESPHERE_DB_USER = "root"
   $env:VOTESPHERE_DB_PASSWORD = "your-local-password"
   ```

   Set these variables in the Tomcat service environment for persistent deployments. Do not commit credentials.

3. Build the WAR:

   ```powershell
   mvn clean test package
   ```

4. Copy `target/votesphere.war` into Tomcat 10.1's `webapps` directory and start Tomcat. The app is then available at `http://localhost:8080/votesphere/`.

5. Register a voter using the application. To grant administrator access, run this statement for the account you registered:

   ```sql
   USE votesphere;
   UPDATE users SET role = 'ADMIN' WHERE username = 'your_registered_username';
   ```

   Sign out and sign in again so the session receives the administrator role.

## Project map

```text
src/main/java/com/votesphere/
  dao/          JDBC data access and transactional vote recording
  filter/       Session authentication and administrator authorization
  model/        User, poll, option, vote-history models
  servlet/      Request handlers for public, voter, and administrator flows
  util/         MySQL connection and BCrypt password hashing
src/main/resources/db/schema.sql
src/main/webapp/
  assets/css/   Responsive design system and page styles
  assets/js/    Navigation, validation, voting, and poll-form interactions
  WEB-INF/      Session configuration and reusable JSP fragments
  *.jsp         Landing, auth, voter, results, and admin views
```

## Main routes

| Route | Purpose | Access |
|---|---|---|
| `/` | Landing page and database-backed platform statistics | Public |
| `/polls` | Browse polls | Public |
| `/register`, `/login`, `/logout` | Account and session actions | Public / signed-in |
| `/dashboard`, `/poll?id=…` | Voter dashboard and poll details | Signed in |
| `/vote` | Submit a vote (POST only) | Signed in |
| `/vote/confirmation` | One-time confirmation after a successful vote | Signed in |
| `/results?id=…` | Poll results; respects the result-visibility setting | Signed in |
| `/history` | Current user's voting activity | Signed in |
| `/admin` | Admin statistics and recent polls | Administrator |
| `/admin/polls` | Create, edit, activate, close, and delete polls | Administrator |
| `/admin/users` | Recent account list | Administrator |

## Data integrity and access controls

- Passwords are hashed using BCrypt; authentication is performed by a Servlet against MySQL.
- Prepared statements are used for SQL parameters.
- Voting is performed in a transaction. The server verifies poll status, deadline, and option ownership; the database's unique `(poll_id, user_id)` constraint is the final duplicate-vote guard.
- Administrator routes check the authenticated session role on the server.
- State-changing form submissions require a per-session CSRF token.
- Voting history is queried using the authenticated user's session ID, never a request-supplied user ID.
- Poll results marked hidden are visible only to administrators.
- Confirmation is shown only after a committed vote and is consumed from the session when displayed.
- Session timeout is 30 minutes; session IDs are rotated after a successful login.

## Verification checklist

Run the automated compilation and unit tests:

```powershell
mvn clean test package
```

Then verify manually with Tomcat and a configured MySQL database:

1. Register two users; verify duplicate usernames/emails are rejected and passwords are not stored as plaintext.
2. Promote one account to `ADMIN`; sign in again and verify the admin dashboard and management routes are available only to that account.
3. Create a poll with fewer than two options and verify the server rejects it; then create an active poll with valid options.
4. Cast one vote as a voter. Refresh the confirmation route, retry the same vote, and try a different option ID from another poll; no second or invalid vote should be recorded.
5. Close a poll and verify its vote endpoint rejects submissions. Configure a deadline and verify expired polls reject votes.
6. Hide results and verify an ordinary voter receives no results while an administrator can view them.
7. Verify each user's history only contains their own votes, and test the responsive layout at desktop and mobile widths.

The manual integration checklist requires a running MySQL database and Tomcat; it is not part of the Maven unit-test suite.

## Project report

VoteSphere is a session-authenticated, role-aware polling application. Its three-layer organization separates JSP presentation, Servlet request handling, and JDBC DAOs. The schema models users, polls, poll options, and votes, with foreign keys and a uniqueness constraint enforcing one vote per account per poll. Poll metrics and results are computed from stored records; no voting results or dashboard counts are fabricated in the UI.

## Viva / review questions

1. Why are vote submission rules checked in both Java and the database?
2. How does the unique constraint prevent two concurrent submissions from creating duplicate votes?
3. Why is the voting transaction protected by a row lock on the poll?
4. How are password hashes created and verified?
5. What is the difference between authentication and authorization in this application?
6. How does the results-visibility setting affect server-side access?
7. Why is the voter ID taken from the session rather than a form parameter?
8. What would change to use a connection pool instead of `DriverManager`?
9. How would you test the concurrent-voting path against MySQL?
10. Which deployment settings should be changed before exposing the app publicly?
