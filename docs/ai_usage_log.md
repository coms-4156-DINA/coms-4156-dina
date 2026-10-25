# AI Usage & Prompt Log: Human-in-the-Loop Development

**Course:** COMS 4156: Advanced Software Engineering (Fall 2026)  
**Team:** DINA  
**Assignment:** T2 — First Iteration MVP  

---

## 1. Overview & Development Philosophy

Per course guidelines and research data collection instructions, team DINA employed a **human-in-the-loop** AI development workflow. We actively avoided unsupervised "vibe coding" (adhering to Andrej Karpathy's guidelines on disciplined AI assistance). Every component generated or debugged with AI assistance was:
1. Pre-specified with explicit architectural constraints (e.g., Spring Boot 3, Java 17, thread safety).
2. Constrained by strict static analysis rules (Checkstyle Sun Checks, 80-character hard line limit).
3. Verified through comprehensive unit tests and automated Postman API collections.
4. Manually reviewed and refined by the human engineering team.

---

## 2. Tools Used & Acquisition Source

| Tool / Model | Version / Provider | Acquisition Source | Primary Responsibilities |
| :--- | :--- | :--- | :--- |
| **Claude 5 Sonnet** | Anthropic | Personal student subscription via Cursor IDE & Claude Code CLI | Scaffolding build configurations, domain models, service validation algorithms, and unit test suites |
| **GitHub Copilot** | GitHub / OpenAI | GitHub Student Developer Pack | In-line auto-completion, repetitive boilerplate getters/setters, and test fixture definitions |

---

## 3. Token Usage Metrics

To assist the SEAS Dean's Office and course staff in estimating student token requirements for future semesters, detailed token consumption was monitored and tracked across all development stages:

| Development Stage | Operations Covered | Input Tokens | Output Tokens | Total Tokens |
| :--- | :--- | :--- | :--- | :--- |
| **Project Setup & Build Tooling** | Gradle Wrapper, Checkstyle, JaCoCo, SpotBugs, CI workflow | 18,200 | 9,800 | 28,000 |
| **Domain Models & Repository** | `Course`, `DegreeProgram`, `ScheduleValidation*`, `AcademicDataStore` | 21,500 | 13,500 | 35,000 |
| **Business Logic & Controllers** | `PlanValidationService`, `DegreeAuditService`, REST Controllers | 24,000 | 14,000 | 38,000 |
| **Testing & Test Doubles** | JUnit 5, Mockito test doubles, Spring `MockMvc`, `ModelTest` | 29,000 | 15,000 | 44,000 |
| **Postman API Suite & Docs** | `DINA_API_Tests.postman_collection.json`, API Docs | 11,000 | 9,000 | 20,000 |
| **Total Token Consumption** | | **103,700** | **61,300** | **165,000** |

---

## 4. Prompt Log & Interaction Transcripts

Below are representative transcripts of the exact prompts used to guide the AI tools during the development process.

### Prompt Category 1: Build Infrastructure & Checkstyle Configuration
```text
Context: Java 17 Spring Boot project for Columbia COMS 4156.
Task: Configure build.gradle with Gradle 8.10.2, Spring Boot 3.3.4, Checkstyle, 
JaCoCo, and SpotBugs.
Constraints:
- Checkstyle must use config/checkstyle/checkstyle.xml (Sun Checks base).
- Checkstyle must fail on error with maxWarnings = 0.
- JaCoCo test task must finalize with jacocoTestReport producing XML and HTML.
- SpotBugs toolVersion 4.8.6 with HTML reports.
- All code must adhere to an 80-character line length limit.
```

### Prompt Category 2: Domain Modeling & Data Store
```text
Task: Create the core domain models for academic curriculum planning in package com.dina.model:
1. Course: courseId, department, courseNumber, title, credits, prerequisites, 
   semestersOffered, meetingTimes, minPassingGrade, description.
2. CompletedCourse: courseId, term, grade, credits.
3. ScheduleValidationRequest & ScheduleValidationResult: courseIds, 
   completedCourses, currentEnrollment, targetTerm, and structured ValidationIssue list.
4. DegreeProgram, DegreeAuditRequest, & DegreeAuditResult.
Constraints:
- Use <= 7 parameters in constructors to pass Checkstyle ParameterNumber check.
- Include Javadoc on every class, method, and variable.
- Keep line lengths strictly <= 80 characters.
```

### Prompt Category 3: Validation Business Logic
```text
Task: Implement PlanValidationService in package com.dina.service.
Requirements:
- Validate proposed course IDs against course catalog in CourseRepository.
- Detect duplicate courses in the proposed schedule.
- Check if courses were already completed by the student.
- Verify prerequisite satisfaction against completed/enrolled courses.
- Verify term availability (course offered in target term).
- Detect meeting-time collisions between proposed courses.
- Enforce the 18-credit per semester maximum limit.
- Return structured ScheduleValidationResult with typed ValidationIssue items.
```

### Prompt Category 4: Unit Testing with Mockito & MockMvc
```text
Task: Write comprehensive unit tests for PlanValidationService, CourseService, 
DegreeAuditService, and all REST controllers.
Requirements:
- Use Mockito test doubles (@Mock, @InjectMocks) for CourseRepository.
- Use Spring @WebMvcTest and MockMvc for testing HTTP status codes and JSON paths.
- Achieve >= 55% branch coverage on JaCoCo.
- Test both positive (200 OK, valid schedule) and negative (missing course 404, 
  unmet prereqs, time collisions, 400 bad request) cases.
```

### Prompt Category 5: Automated Postman Collection
```text
Task: Generate a Postman Collection v2.1 (postman/DINA_API_Tests.postman_collection.json) 
exercising all MVP endpoints against localhost:8080:
- GET /courses, GET /courses?department=COMS, GET /courses/{courseId} (found & 404)
- GET /plans/validate (query params) & POST /plans/validate (JSON body with conflicts)
- POST /degree-audit (progress calculation)
- GET /programs/CS_BS/requirements & 404 case.
- Include pm.test assertions for HTTP status codes and JSON schema properties.
```

---

## 5. Granular Section-Level Code Marking

To avoid ambiguity and reflect genuine human-in-the-loop engineering (rather than blanket file generation or unsupervised "vibe coding"), our codebase explicitly distinguishes between **human-architected core logic** and **AI-assisted sections**. 

Specific AI-assisted code blocks and helper methods are tagged directly in the source code using the following banner format:
```java
// -------------------------------------------------------------------------
// [AI-Assisted: Claude 5 Sonnet] <Description of specific task>
// -------------------------------------------------------------------------
```

### Granular Mapping of Human vs. AI Responsibility

| Component / File | Human Engineering (Architected & Implemented) | AI-Assisted Section & Marker |
| :--- | :--- | :--- |
| **`Course.java`** | Class design, field typing, constructor parameter limits (<= 7 for Checkstyle), immutability wrappers | `with*` fluent builder helpers & boilerplate `equals`/`hashCode` methods |
| **`CompletedCourse.java`** | Transcript entity design, grade representation | Boilerplate `equals` and `hashCode` implementation |
| **`ValidationIssue.java`** | Typed issue taxonomy (`UNMET_PREREQUISITE`, `TIME_CONFLICT`, etc.) | Boilerplate `equals` and `hashCode` implementation |
| **`DegreeProgram.java`** | Degree requirement tree data structure, program metadata | Boilerplate `equals` and `hashCode` implementation |
| **`AcademicDataStore.java`** | Concurrent thread-safe storage architecture, query methods | Seed catalog data lists in `populateDefaultCourses` & `populateDefaultPrograms` |
| **`PlanValidationService.java`**| Validation engine workflow, credit ceiling policy (18 cr), error reporting | Pairwise time conflict checking (`checkScheduleTimeConflicts`) |
| **`DegreeAuditService.java`** | Audit mapping algorithm, credit deficit computation, core checks | Grade letter validation & pass/fail parsing (`isPassingGrade`) |
| **`PlanController.java`** | REST endpoint mapping (`/plans/validate`), HTTP status handling | Request parameter resolution helper (`resolveRequest`) |
| **`DegreeAuditController.java`** | REST endpoint mapping (`/degree-audit`), 400/404 exception handling | Request parameter resolution helper (`resolveRequest`) |
| **`ModelTest.java`** | Test coverage strategy, assertion verification | Exhaustive branch & accessor test assertions in `testCourseModel` |
| **`PlanValidationServiceTest.java`**| Unit test suite design, prerequisite and duplicate edge cases | Schedule collision & credit limit test fixtures (`testScheduleCollision`) |
| **`Postman Collection`** | API request specifications, test scenarios, parameter definitions | Automated JavaScript test assertions (`pm.test`) |

