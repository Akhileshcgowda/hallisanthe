# Operating Rules

## Anti-Crash Rules
- **Pre-flight Checks**: Before running long commands or builds, check for common pitfalls (disk space, environment variables).
- **Graceful Failure**: If a tool fails, report the exact error and suggest a fix.
- **Save State**: Always update `sessions/` files before ending a turn or after significant milestones to ensure recovery is possible.

## Work Rules
- **Research First**: Understand the codebase before modifying it.
- **Test Before Completion**: Use `./gradlew build` or equivalent to verify changes.
- **Maintain Consistency**: Follow existing patterns for naming, architecture, and styling.

## Session Rules
- **Start Workflow**: Always follow the `.agents/workflows/start.md` logic at the beginning of a session.
- **Post-Chat Memory Storage**: Update the current session log and `JARVIS_MEMORY.md` after every user interaction to ensure context is never lost.
- **Recap**: Every turn should contribute to the session's recap.
- **Closing**: Use a closure marker (🔒) when a task is finished and the session is ready to be archived.
- **Pausing**: Use a pause marker (⏸️) if you are waiting for user input or a long process.
