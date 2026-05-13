# /start Workflow

This workflow is executed at the beginning of every session to establish context and ensure continuity.

## Scenarios

### Case A — New Day (No session file for today)
1. **Initialize Context**: Read all base files in `JARVIS/` (`GEMINI.md`, `operating_rules.md`, `JARVIS_MEMORY.md`, `CONFIG_TECHNIQUE.md`, `NOTES_TECHNIQUES.md`, `USER.md`, `ARBORESCENCE.md`).
2. **Retrieve Past State**: Read the last closed session file (marked with 🔒) in `JARVIS/sessions/`.
3. **Create Daily Log**: Create `JARVIS/sessions/YYYY-MM-DD.md`.
4. **Recap**: Add a timestamped RECAP section summarizing the status.
5. **Tasks**: List active tasks from the `task.md` or equivalent project management tool.
6. **Greeting**: Respond with: "Good morning! Here are your tasks. What do we tackle?"

### Case B — Mid-Session Resume (Session file exists with ⏸️ marker)
1. **Initialize Context**: Read all base files + today's session file.
2. **Resume Log**: Add a "RESUME" entry to the RECAP with a timestamp.
3. **Summary**: Summarize where we left off based on the last entry before the ⏸️ marker.
4. **Greeting**: Respond with: "Welcome back. Resuming work on [Current Task]. Shall we continue?"

### Case C — Crash Recovery (Session file exists without any marker)
1. **Initialize Context**: Read all base files + today's session file.
2. **Investigate**: Check recently modified files (last 1 hour) to reconstruct what was being worked on.
3. **Recovery Log**: Add a "RECOVERY" entry with ⚠️ and a timestamp.
4. **Greeting**: Respond with: "I'm back from an interruption. Here's what I found — is this right?"
