# Release versioning

- `VERSION` is the single source of truth for the mod version and uses `MAJOR.MINOR.PATCH` format.
- Every new user-facing release must increment the patch number in `VERSION` exactly once (for example, `1.0.6` -> `1.0.7` -> `1.0.8`).
- Use the Gradle `release` task for a distributable release. It builds the JAR with the next patch version and updates `VERSION` only after a successful build.
- Use the normal `build` task for verification builds; it must not change the version.
- Deliver the versioned file from `build/libs/minecraft-pokemon-<version>.jar`.

# Prompt logging

- When `/save-prompt` is requested, save a concise UTF-8 Japanese summary to `docs/prompt/YYMMDD-題名.md`.
- Summarize the prompts, responses, implementation, and validation instead of copying the full transcript.
- Stage every current-repository change with `git add -A`, commit it on `main`, and push explicitly to `origin/main`.
