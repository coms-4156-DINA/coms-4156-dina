# AGENTS.md

## Project Overview

This will be a backend computation service that ingests academic curriculum rules, evaluates student transcripts against prerequisites, detects scheduling collisions, and simulates multi-semester completion pathways.

The service exposes its capabilities exclusively to external programs (there is no end-user UI).

### Development Guidelines

- Keep changes to the code base minimal and incremental. Do not take development cost into account.
- Do not commit to the repo unless explicitly asked.

### Persistent Datastore

The service maintains its own persistent datastore containing:

- Curriculum requirement trees
- Prerequisite dependencies
- Course catalog metadata:
  - Credit values
  - Minimum passing grades
  - Which semester(s) each course is offered

## API Endpoints

| # | Operation | Method & Path |
|---|-----------|---------------|
| 1 | Search and retrieve courses | `GET /courses` |
| 2 | Validate a proposed semester schedule | `GET /plans/validate` |
| 3 | Calculate degree progress | `GET /degree-audit` |
| 4 | Generate a valid semester plan | `POST /plans/generate` |
| 5 | Compare alternative plans | `GET /plans/compare` |
| 6 | Retrieve degree requirements | `GET /programs/{programId}/requirements` |

### 1. Search and Retrieve Courses — `GET /courses`

- **Input:** Filters such as department, course level, credits, term, and keywords.
- **Processing:** Searches the course catalog and filters results using the criteria.
- **Output:** List of matching courses with course IDs, titles, credits, prerequisites, and general information.

### 2. Validate a Proposed Semester Schedule — `GET /plans/validate`

- **Input:** List of course IDs, completed courses, current enrollment, and target term.
- **Processing:** Checks prerequisite eligibility, credit limits, duplicate courses, known meeting-time conflicts, and any configured restrictions.
- **Output:** A validity result, a list of detected conflicts or unmet prerequisites, and an explanation for each issue.

### 3. Calculate Degree Progress — `GET /degree-audit`

- **Input:** A degree program ID and a student's completed, transferred, and (optionally) in-progress courses.
- **Processing:** Maps completed courses to degree requirements, accounts for required credits and categories, and identifies outstanding requirements.
- **Output:** Completed requirements, remaining requirements, credits earned toward the degree, and courses or categories still needed.

### 4. Generate a Valid Semester Plan — `POST /plans/generate`

- **Input:** Completed courses, degree requirements, number of semesters remaining, target credit range, and optional preferences (e.g., preferred departments or course loads).
- **Processing:** Uses prerequisite relationships, remaining requirements, course offerings, and scheduling constraints to construct feasible semester-by-semester plans.
- **Output:** One or more valid candidate plans, with courses grouped by semester and any unsatisfied constraints or limitations explained.

### 5. Compare Alternative Plans — `GET /plans/compare`

- **Input:** Two or more candidate plans and optional comparison criteria (e.g., credit distribution, number of remaining requirements satisfied, or schedule conflicts).
- **Processing:** Evaluates each plan against the same constraints and calculates comparable metrics.
- **Output:** A structured comparison showing feasibility, credit loads, requirements fulfilled, and outstanding requirements for each plan.

### 6. Retrieve Degree Requirements — `GET /programs/{programId}/requirements`

- **Input:** A degree program ID and (optionally) a catalog year.
- **Processing:** Retrieves the applicable version of the degree rules, including required courses, elective categories, credit totals, and prerequisite-related restrictions.
- **Output:** A structured representation of the degree requirements that client programs can use for planning, auditing, or validation.

## Development Tooling

### Build & Dependencies

- **Build and dependency manager:** Gradle
- **IDE:** IntelliJ IDEA

### Testing

- **Unit test runner:** JUnit
- **API testing:** Postman
- **Mocking:** Mockito
- **Coverage:** JaCoCo

### Code Quality

- **Style checker:** Checkstyle
- **Static analysis / bug finder:** SpotBugs

### CI & Project Management

- **CI:** GitHub Actions workflows
- **Project management:** GitHub Projects
