# CSV Validator — Methodology Learning Project

A small Java application for learning **AI-assisted software development using the BMAD, Superpowers, Openspec Methods**.

The application validates CSV files containing provider data and reports invalid or duplicate records.

The primary purpose of this repository is to practice a structured AI-assisted development workflow:

**Requirements → Specification → Architecture → Stories → Test Cases → Implementation → Verification**

---

## Project Goals

This project has two goals:

1. Build a simple CSV validation application.
2. Learn how to use BMAD, Superpower, Openspec with AI coding agents in VS Code.

Rather than asking an AI agent to build the entire application from one prompt, the project uses structured artifacts to guide development and testing.

---

## Application Overview

The user provides a CSV file containing provider information.

Example:

```csv
provider_id,provider_name,effective_date
1001,ABC Clinic,2026-01-01
1002,XYZ Hospital,2026-02-15
1003,,2026-03-10
