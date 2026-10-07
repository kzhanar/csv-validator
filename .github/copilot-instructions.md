# Superpowers workspace workflow

Before starting work in this workspace, load and follow the `using-superpowers`
skill in `.agents/skills/using-superpowers/SKILL.md`, then load applicable
Superpowers skills for the task. If skill invocation is unavailable, read those
skill files directly.

This branch is the Superpowers arm of a controlled BMAD vs OpenSpec vs Superpowers
experiment. Treat the common experiment change request and requirements as fixed
inputs. Do not modify them unless the user explicitly requests it. Do not load
BMAD or OpenSpec workflows for this branch.

Superpowers skills are installed locally from `obra/superpowers`, revision
`8ca22dba9a94f28898bbce59f2537ff4d87c747d` (version 6.4.2).
This is a skills-only installation, not a user-wide Copilot plugin installation.
Upstream shell helpers may require Bash and other tools that are not available
in the Windows environment; do not assume those helpers have been validated.
