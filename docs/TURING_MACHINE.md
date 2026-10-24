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

## Phase 2: interactive large world
The application now has a **Large world** tab with pan/zoom, basic sparse Life stepping, and a file picker for standard B3/S23 RLE patterns (up to 2 MB / 100,000 live cells). This is intentionally a first iteration, not HashLife. At low zoom levels, it draws only in-view live cells but iterates across all live cells. Imports are file-based, not bundled.

**Safety/performance limitations:** Slow simulation for large patterns; not appropriate yet to run or benchmark the full Rendell Turing machine. No annotations, no RLE export, no guarantee that 2MB input bound covers all possible input formats. A proper Turing machine preset and HashLife remain open.

## Phase 3: on-demand Rendell preset
The Large World screen exposes **Load Rendell Turing Machine**, downloading `https://conwaylife.com/patterns/turingmachine.rle` on demand over HTTPS, parsing the official B3/S23 RLE, checking the expected 1714 × 1647 bounding rectangle and 36,549 live cells, and framing the imported construction. This is not yet a bundled offline pattern. The sparse engine still does not implement HashLife; interactive stepping of the full machine will be slow. Pending: verify source response and pattern validation on a real device, add reliable offline asset, benchmarks and HashLife acceleration. Source: https://conwaylife.com/wiki/Turing_machine

## Inspector v1 (read-only, no fabricated tape)
Rendell mode now shows the real Life generation/population plus a clock estimate: 11,040 Life generations per TM cycle, as documented by Rendell. Its demonstration begins with two ones and produces four after 16 cycles. This **does not decode tape values**. Display an explicit 'not decoded' status rather than guessing output. The cycle estimate is invalid if the user modifies any cell or loads a non-original state. Next phases: independently documented stack location annotations; sample and decode glider streams; exact snapshots of each state and tape values cross-verified with a second implementation (e.g. Golly). Only then offer arbitrary input editing.

Reference: https://www.rendell-attic.org/gol/tm.htm
