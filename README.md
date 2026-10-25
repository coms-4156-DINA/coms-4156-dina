# DINA: Academic Curriculum & Degree Pathway Computation Service

**COMS 4156: Advanced Software Engineering — Team DINA (Iteration 1 / T2)**  
**Repository:** [https://github.com/coms-4156-DINA/coms-4156-dina](https://github.com/coms-4156-DINA/coms-4156-dina)

---

## 1. Project Overview

DINA is a backend microservice engineered to ingest academic curriculum rules, evaluate student transcripts against prerequisite graphs, detect scheduling collisions, and simulate valid multi-semester degree completion pathways. 

The service maintains its own persistent, thread-safe academic datastore containing course catalog metadata (credit values, minimum passing grades, semester offering schedules, and meeting times) and degree program requirement specifications. DINA has no end-user UI; it exposes its capabilities exclusively as programmatic REST endpoints to external clients (e.g., registrar batch degree verification systems and corporate tuition reimbursement verification portals).

---

## 2. Team Members & Task Allocation

| Name | UNI | Assigned Responsibilities |
| :--- | :--- | :--- |
| **Dwarakesh Baraneetharan** | `db3881` | Project scaffold & architecture, Gradle build tooling, Checkstyle, JaCoCo, SpotBugs, CI workflow, Postman test suite, documentation, and integration |
| **Ismael Hernandez** | `ih2427` | Operation 1: Search and retrieve courses (`GET /courses`, `GET /courses/{courseId}`) |
| **Anh Nguyen** | `ahn2130` | Operation 2: Validate proposed semester schedule (`GET /plans/validate`, `POST /plans/validate`) |
| **Nicolas Tabet** | `nt2773` | Operation 3: Calculate degree progress (`GET /degree-audit`, `POST /degree-audit`) |

### Project Management Tracking
All milestone tasks, issues, and member assignments are tracked via GitHub Issues in our repository:
- **Project Tracking Link:** [GitHub Issues — DINA Milestone T2](https://github.com/coms-4156-DINA/coms-4156-dina/issues)
  - Issue #1: Project Scaffold, Build Tooling & CI Setup (`db3881`)
  - Issue #2: Operation 1 - Course Search and Retrieval (`ih2427`)
  - Issue #3: Operation 2 - Semester Schedule Validation (`ahn2130`)
  - Issue #4: Operation 3 - Degree Progress Calculation (`nt2773`)
  - Issue #5: API Test Suite & Integration Tests (`db3881`)
  - Issue #6: Documentation, Reports, Coverage & Release Tagging (`db3881`)

---

## 3. Operational API Documentation

### 3.1 Search & Retrieve Courses — `GET /courses`
Searches the course catalog and returns courses matching specified query parameters.

- **HTTP Method:** `GET`
- **Path:** `/courses`
- **Query Parameters:**
  - `department` *(string, optional)*: Filter by department code (e.g., `COMS`, `MATH`).
  - `level` *(integer, optional)*: Filter by course level (e.g., `4000`, `3000`).
  - `credits` *(integer, optional)*: Filter by exact credit count (e.g., `3`, `4`).
  - `term` *(string, optional)*: Filter by semester offered (e.g., `FALL`, `SPRING`).
  - `keyword` *(string, optional)*: Case-insensitive search matching course ID, title, or description.
- **Side Effects:** None (idempotent query).
- **Status Codes:**
  - `200 OK`: Successful query; returns array of matching `Course` objects, sorted by `courseId`.
  - `400 BAD REQUEST`: `level` or `credits` is not a positive integer.
- **Sample Request:**
  ```http
  GET /courses?department=COMS&level=4000&keyword=software HTTP/1.1
  Host: localhost:8080
  ```
- **Sample Response (200 OK):**
  ```json
  [
    {
      "courseId": "COMSW4156",
      "department": "COMS",
      "courseNumber": 4156,
      "title": "Advanced Software Engineering",
      "credits": 3,
      "prerequisites": ["COMSW3157"],
      "semestersOffered": ["FALL", "SPRING"],
      "meetingTimes": ["MW 10:10-11:25"],
      "minPassingGrade": "C-",
      "description": "Service architecture, testing, CI/CD, teamwork."
    }
  ]
  ```

---

### 3.2 Retrieve Single Course Details — `GET /courses/{courseId}`
Retrieves detailed metadata for a specific course by its unique course ID.

- **HTTP Method:** `GET`
- **Path:** `/courses/{courseId}`
- **Path Variables:**
  - `courseId` *(string, required)*: Unique course code (e.g., `COMSW4156`).
- **Side Effects:** None.
- **Status Codes:**
  - `200 OK`: Course found and returned.
  - `404 NOT FOUND`: No course exists with the provided `courseId`.

---

### 3.3 Validate Proposed Semester Schedule — `GET /plans/validate` & `POST /plans/validate`
Evaluates a candidate schedule against prerequisite requirements, duplicate course restrictions, semester term offerings, schedule time collisions, and maximum credit limits (18 credits). Supports both GET (with query parameters or body) and POST (with JSON payload).

- **HTTP Method:** `GET` or `POST`
- **Path:** `/plans/validate`
- **Parameters (GET Query):**
  - `courseIds` *(comma-separated strings)*: Proposed courses for the semester.
  - `targetTerm` *(string)*: Target semester (e.g., `FALL2026`).
  - `completedCourses` *(comma-separated strings, optional)*: Courses completed in prior terms.
- **Request Body (JSON for POST or GET):**
  ```json
  {
    "courseIds": ["COMSW3134", "COMSW3203"],
    "completedCourses": [
      {
        "courseId": "COMSW1004",
        "term": "FALL2025",
        "grade": "A",
        "credits": 3
      }
    ],
    "currentEnrollment": [],
    "targetTerm": "FALL2026"
  }
  ```
- **Side Effects:** None (read-only evaluation).
- **Status Codes:**
  - `200 OK`: Validation completed; returns structured `ScheduleValidationResult`.
  - `400 BAD REQUEST`: Invalid request payload or missing course IDs.
- **Response Format:**
  ```json
  {
    "valid": true,
    "totalCredits": 7,
    "issues": []
  }
  ```
- **Detected Conflict Types:**
  - `EMPTY_SCHEDULE`: No courses provided for validation.
  - `UNKNOWN_COURSE`: Course ID does not exist in the course catalog.
  - `DUPLICATE_COURSE`: Course appears multiple times in the proposed semester.
  - `ALREADY_COMPLETED`: Course is already completed or enrolled.
  - `UNMET_PREREQUISITE`: Required prerequisite has not been completed.
  - `TERM_UNAVAILABLE`: Course is not offered during the target semester.
  - `TIME_CONFLICT`: Two proposed courses have overlapping meeting times.
  - `CREDIT_LIMIT_EXCEEDED`: Total planned credits exceed the 18-credit limit.

---

### 3.4 Calculate Degree Progress — `GET /degree-audit` & `POST /degree-audit`
Maps a student's transcript against degree program requirements and calculates completed requirements, outstanding core requirements, and remaining credit deficits.

- **HTTP Method:** `GET` or `POST`
- **Path:** `/degree-audit`
- **Parameters (GET Query):**
  - `programId` *(string, required)*: Degree program identifier (e.g., `CS_BS`, `CS_MS`).
  - `catalogYear` *(integer, optional)*: Catalog year (e.g., `2026`).
  - `completedCourses` *(comma-separated strings, optional)*: Completed course codes.
- **Request Body (JSON for POST or GET):**
  ```json
  {
    "programId": "CS_MS",
    "catalogYear": 2026,
    "completedCourses": [
      {
        "courseId": "COMSW4156",
        "term": "FALL2025",
        "grade": "A",
        "credits": 3
      },
      {
        "courseId": "COMSW4118",
        "term": "SPRING2026",
        "grade": "A",
        "credits": 3
      }
    ]
  }
  ```
- **Side Effects:** None.
- **Status Codes:**
  - `200 OK`: Audit completed successfully; returns `DegreeAuditResult`.
  - `400 BAD REQUEST`: Missing or empty `programId`.
  - `404 NOT FOUND`: Specified `programId` not found in datastore.
- **Response Format:**
  ```json
  {
    "programId": "CS_MS",
    "creditsEarned": 6,
    "creditsRequired": 30,
    "degreeCompleted": false,
    "completedRequirements": ["COMSW4156", "COMSW4118"],
    "remainingRequirements": [],
    "coursesStillNeeded": ["Elective credits deficit: 24"]
  }
  ```

---

### 3.5 Retrieve Degree Requirements — `GET /programs/{programId}/requirements`
Retrieves the curricular rules, core requirements, and elective tracks for a degree program.

- **HTTP Method:** `GET`
- **Path:** `/programs/{programId}/requirements`
- **Path Variables:**
  - `programId` *(string, required)*: Program code (e.g., `CS_BS`, `CS_MS`).
- **Query Parameters:**
  - `catalogYear` *(integer, optional)*: Specific catalog year.
- **Status Codes:**
  - `200 OK`: Program requirements returned.
  - `404 NOT FOUND`: Program not found.

---

## 4. Call-Order Constraints & Usage Scenarios

### Ordering Constraints
1. **Catalog Awareness Before Schedule Validation:** Calling programs should query `GET /courses` to retrieve available courses and meeting times before submitting schedules to `GET /plans/validate`.
2. **Requirements Ingestion Before Degree Auditing:** Calling programs should verify available degree requirements via `GET /programs/{programId}/requirements` prior to invoking `GET /degree-audit`.
3. **Stateless Idempotency:** None of the evaluation endpoints modify student datastores on the server; clients are expected to maintain transcript state between audits.

### Correct Usage Scenario (Happy Path)
1. **Search Catalog:** Client queries `GET /courses?department=COMS&term=FALL` to discover courses offered in Fall.
2. **Check Requirements:** Client queries `GET /programs/CS_BS/requirements` to inspect core courses required for the BS in Computer Science.
3. **Validate Proposed Schedule:** Client submits a proposed schedule to `POST /plans/validate` specifying `COMSW3134` (with `COMSW1004` marked completed). Service responds with `valid: true`.
4. **Audit Degree Progress:** Client invokes `POST /degree-audit` for program `CS_BS` with completed courses. Service returns satisfied requirements and credit count.

### Contrived Bad Usage Scenarios (Unhappy Paths)
1. **Schedule Time Collision:**
   - **Invocation:** Client sends `POST /plans/validate` with `courseIds: ["COMSW1004", "COMSW4156"]`.
   - **Why it fails:** Both courses share the identical time slot (`MW 10:10-11:25`). The service returns `valid: false` with issue type `TIME_CONFLICT`.
2. **Unsatisfied Prerequisite:**
   - **Invocation:** Client sends `POST /plans/validate` proposing `COMSW4118` (Operating Systems) with an empty `completedCourses` list.
   - **Why it fails:** `COMSW4118` requires `COMSW3157` and `COMSW3827`. The service flags `UNMET_PREREQUISITE` for both courses.
3. **Credit Overload Violation:**
   - **Invocation:** Client submits a schedule containing 7 three-credit courses (21 credits total).
   - **Why it fails:** Total credits exceed the maximum semester credit ceiling of 18. The service flags `CREDIT_LIMIT_EXCEEDED`.
4. **Missing Program in Degree Audit:**
   - **Invocation:** Client calls `GET /degree-audit?programId=ASTRONOMY_PHD`.
   - **Why it fails:** Program does not exist in datastore. Returns `404 NOT FOUND`.

---

## 5. Build, Run, and Test Instructions

### Prerequisites
- **JDK:** Java 17 LTS or newer
- **Build System:** Gradle (via included Gradle Wrapper `./gradlew`, no global installation required)

### Build the Service
```bash
./gradlew build
```
*(On Windows: `.\gradlew.bat build`)*

### Run the Service Locally
```bash
./gradlew bootRun
```
The service will start on port `8080`. Verify with `curl http://localhost:8080/courses`.

### Run Unit Tests
```bash
./gradlew test
```

### Run Style Checking (Checkstyle)
```bash
./gradlew checkstyleMain checkstyleTest
```
Checkstyle reports are generated in `build/reports/checkstyle/main.html` and `build/reports/checkstyle/test.html`. A clean report copy is maintained in `reports/checkstyle/`.

### Run Code Coverage (JaCoCo)
```bash
./gradlew jacocoTestReport
```
HTML coverage reports are generated in `build/reports/jacoco/test/html/index.html`. A copy is preserved in `reports/jacoco/`.

### Run API Tests (Postman)
An automated Postman test suite exercising all MVP endpoints resides in:
- `postman/DINA_API_Tests.postman_collection.json`
Run via Postman GUI or Newman CLI:
```bash
npx newman run postman/DINA_API_Tests.postman_collection.json --env-var "baseUrl=http://localhost:8080"
```

---

## 6. Style Checker & Self-Documenting Code

- **Tool:** Checkstyle (v10.18.2) integrated into Gradle build.
- **Ruleset:** Custom Sun Checks configuration (`config/checkstyle/checkstyle.xml`) enforcing:
  - Hard line length limit of **80 characters**
  - Mandatory Javadoc on all classes, methods, and variables
  - Package-level documentation (`package-info.java`)
  - Final parameters on methods and constructors
  - Prohibition of star imports
- **Report Status:** **Clean (0 violations)** across all files. See `reports/checkstyle/main.html`.

---

## 7. Test Results & Branch Coverage

- **Testing Frameworks:** JUnit 5 (Jupiter), Mockito (test doubles), Spring WebMvcTest (`MockMvc`).
- **Test Results:** 36/36 tests passing (100% success rate).
- **Branch Coverage:** **69% branch coverage** achieved (exceeding the ≥ 55% rubric requirement). Total instruction coverage is **88%**.
- **Coverage Report Location:** `reports/jacoco/index.html`.

---

## 8. Third-Party Code Disclosure

All third-party dependencies are fetched strictly via the Gradle package manager (`build.gradle`) from Maven Central. No bloated or unmanaged third-party source files are vendored in the codebase.

| Library / Dependency | Version | Purpose | Source / Provider |
| :--- | :--- | :--- | :--- |
| `spring-boot-starter-web` | 3.3.4 | REST framework, embedded Tomcat, HTTP routing | [Spring Framework](https://spring.io/projects/spring-boot) |
| `spring-boot-starter-validation` | 3.3.4 | Bean validation and input constraints | [Hibernate Validator / Spring](https://hibernate.org/validator/) |
| `jackson-databind` | 2.17.x | JSON serialization / deserialization | [FasterXML Jackson](https://github.com/FasterXML/jackson) |
| `spring-boot-starter-test` | 3.3.4 | Unit testing harness (JUnit 5, Mockito, AssertJ, MockMvc) | [Spring Test](https://spring.io) |
| `spotbugs-annotations` | 4.8.6 | Static analysis annotations | [SpotBugs Team](https://spotbugs.github.io/) |

---

## 9. AI Tool Documentation

In accordance with course guidelines for team projects, team DINA utilized "human-in-the-loop" AI-assisted software development tools to accelerate boilerplate scaffolding, styling compliance, and test generation. In alignment with Andrej Karpathy's guidelines against unguided "vibe coding", all AI-generated suggestions were strictly directed by human prompts, reviewed for correctness, validated through our test suites, and audited against static analysis standards.

- **AI Tools Employed:** 
  - **Claude 5 Sonnet** (via Claude Code CLI and Cursor IDE) for build file scaffolding, domain modeling, validation algorithms, and unit test doubles.
  - **GitHub Copilot** (via GitHub Student Developer Pack) for contextual inline auto-completion and test fixture generation.
- **Where Obtained:** Personal student developer subscriptions and GitHub Student Developer Pack.
- **How Utilized:** Pair-programming workflow where human developers defined system architecture, interfaces, and constraints, prompted the LLM for initial implementation drafts, and manually reviewed, debugged, and formatted the outputs to adhere to COMS 4156 style policies.
- **Prompt Details & Transcripts:** Detailed prompts, configurations, and conversation logs are documented in [`docs/ai_usage_log.md`](docs/ai_usage_log.md).
- **Code Marking:** Rather than ambiguous blanket file markings, AI-assisted code is explicitly demarcated at the granular method and block level using clear banner comments (e.g., `// [AI-Assisted: Claude 5 Sonnet] <Task>`), cleanly distinguishing human-engineered architecture from AI-assisted boilerplate and helpers. A comprehensive mapping is documented in [`docs/ai_usage_log.md`](docs/ai_usage_log.md).
- **Token Consumption Metrics (for University Data Collection):**
  - **Total Tokens Used:** **~165,000 tokens** across all tools (Scaffolding: 28k, Domain models: 35k, Services: 38k, Unit & Mock testing: 44k, API tests & docs: 20k). See breakdown in [`docs/ai_usage_log.md`](docs/ai_usage_log.md).

