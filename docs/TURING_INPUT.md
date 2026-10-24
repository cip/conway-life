# Rendell Turing input pathway

This feature starts **input preparation**, not physical injection. The actual Rendell machine is a three-state, three-symbol TM with its tape represented by **two glider-encoded stacks**, not by a linear set of editable bits. The sample uses two ones on the right tape. The original construction provides six stack cells; it must not be assumed six freely editable characters are immediately supported.

In the Android Large World UI, **Prepare tape input** opens a textual **draft-only** editor. It validates alphabet 0/1/2, whitespace and a conservative 6-symbol bound. It explicitly does not mutate the Life pattern; the original demo remains loaded and running regardless of draft values.

## Exact implementation contract still needed before Apply may exist

1. Obtain a documented construction format (e.g. Rendell's Golly scripts / original *.lif stack-cell patterns) and determine left/right stack origin, orientation, first symbol positions, and time phase relative to generation zero.
2. Identify cell sets encoding all three values on each stack, including which gliders/ships are absent and present.
3. Implement a *pure* decoder and injector for **actual Life cells** with strict preservation of surrounding machinery. It must reject unsupported length/layouts.
4. Test decode(initial pattern) against the documented default input and ensure encode(decode(initial)) reproduces **all** original Life cells.
5. Run modified inputs independently in Golly and HashLife and compare full cell positions at multiple 11,040-generation checkpoints; only then expose an Apply control.
6. Explain which stack contains the symbol under the read head: the machine reads during POP in one cycle and pushes the replacement in the next; the “head symbol” is not stored as a separate tape cell.

Primary documentation:
- https://www.rendell-attic.org/gol/tm.htm
- https://rendell-attic.org/gol/tmdetails.htm

Avoid presenting a virtual TM simulation as the outcome: physical cell modifications are required.
