# Release versioning

- `VERSION` is the single source of truth for the mod version and uses `MAJOR.MINOR.PATCH` format.
- Every new user-facing release must increment the patch number in `VERSION` exactly once (for example, `1.0.6` -> `1.0.7` -> `1.0.8`).
- Use the Gradle `release` task for a distributable release. It builds the JAR with the next patch version and updates `VERSION` only after a successful build.
- Use the normal `build` task for verification builds; it must not change the version.
- Deliver the versioned file from `build/libs/minecraft-pokemon-<version>.jar`.

# Prompt logging

- Run the [save-prompt skill](.agents/skills/save-prompt/SKILL.md) at the end of every completed task in this repository by default, and whenever the user requests `/save-prompt`. Follow any explicit instruction to skip saving, committing, or pushing.
