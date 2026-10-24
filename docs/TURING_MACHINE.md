# Conway-native Turing machine — implementation plan

This feature is **not** an independent simulated Turing machine. The target is a real B3/S23 Game of Life pattern implementing computation purely through Life cells.

References:
- Paul Rendell's Turing machine (2000): https://conwaylife.com/wiki/Turing_machine (1714 × 1647 cells, 36,549 alive)
- Universal Turing machine (2010): https://conwaylife.com/wiki/Universal_turing_machine (12699 × 12652, 252,192 alive)
- Official details: https://rendell-attic.org/gol/tm.htm

## Foundation (this PR)
- Pure deterministic sparse Conway Life engine with **unbounded, non-wrapping coordinates**, unlike 48×48 torus.
- Standard B3/S23 RLE decoder (no special-purpose machine transitions).
- Tests for edge independence, glider movement and RLE parsing.

## Remaining before the feature is usable
1. Add UI mode for large sparse worlds, pan/zoom and viewport culling; keep the existing 48×48 and 3D views unchanged.
2. Import documented RLE pattern (including Rendell's actual TM), with offline bundle or user file picker; preserve coordinate orientation.
3. Select the real Turing-machine preset and center camera on it; provide annotations for tape/head and life cells (annotation does not affect simulation).
4. For larger universality demonstrations, use HashLife / memoized stepping (a naïve sparse B3/S23 iteration for many thousands of generations is too slow).
5. Verify imported pattern cell count and bounding box; compare known checkpoints with Golly, benchmark on Android and add emulator tests.
6. Upgrade signing/versionCode remains separately tracked: https://github.com/cip/conway-life/issues/2.

**A passing build of this foundation does not mean a Turing machine is already running in the app.**
