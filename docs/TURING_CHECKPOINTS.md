# Turing-machine cycle checkpoints

This increment builds on the read-only inspector in PR #4. It is deliberately **not** a tape decoder.

- "Next TM cycle" advances the real loaded, unedited Rendell Life pattern to the next 11,040-generation boundary using the existing HashLife engine; stepping from a boundary advances a full cycle.
- "Snapshot" records measured generation and exact current living-cell bounding box/population, with only the most recent 16 summaries kept in memory.
- Editing the machine marks the standard cycle assumption invalid and clears checkpoint summaries.
- The inspector continues to say "Input tape: not decoded / Output tape: not decoded".
- A cycle boundary is a timing reference, not proof that an operation completed, halted, or wrote a specific symbol.
- Next: locate the stack/outgate signal lanes in the documented layout, independently verify expected glider phases with Golly, and only then decode bits and show input/output values.

Reference: https://rendell-attic.org/gol/tmdetails.htm
