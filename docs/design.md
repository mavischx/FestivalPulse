# design.md — Festival Steward Dashboard

Design rules for a crowd-reporting and alerting dashboard for a small music & food
festival. Stewards report how busy each area is; when an area gets too crowded, the
app raises an alert for organisers.

This document is the source of truth for visual and interaction design. Agents
generating UI **must** follow it. When a rule and a request conflict, prefer the rule
and flag the conflict.

---

## 0. The one rule that overrides the aesthetic

The look is iOS / Apple "Liquid Glass". But per Apple's own guidance, glass is for the
**navigation and control layer that floats above content — never the content itself.**

For this app, the *content* is the thing a steward or organiser must read in under a
second: how busy an area is, and whether there's an alert. So:

- **Glass is chrome.** Top bar, segmented controls, floating alert banner, sheet
  headers, floating action buttons.
- **Content is solid.** Area status cards and alert cards sit on opaque, high-contrast
  surfaces. Legibility beats vibe every time here — a missed "over capacity" is a
  safety problem, not a style problem.

If you're ever unsure whether something should be glass: if a person needs to *read
data off it at a glance*, it is not glass.

---

## 1. Design principles

1. **Content first, controls recede.** The busy-ness of each area is the star. Chrome
   floats quietly above it.
2. **Status is instant.** Crowd level must be readable from across a tent, at arm's
   length, in sunlight. High contrast, big type, colour **plus** label — never colour
   alone.
3. **Calm until it isn't.** The interface is quiet in normal operation. Alerts are the
   only thing allowed to demand attention — so nothing else should compete with them.
4. **Restraint.** Spend the "wow" on one signature (the glass chrome + a live-updating
   status grid). Keep everything else disciplined.

---

## 2. Typography

Apple's system typeface is **San Francisco (SF Pro)**. It ships on Apple devices but
does not render everywhere on the web, so the stack below gives Apple users real SF and
everyone else **Inter**, the closest widely-available substitute.

```css
--font-sans: -apple-system, BlinkMacSystemFont, "SF Pro Text", "SF Pro Display",
             "Inter", "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
```

> For visual consistency across *all* devices, self-host Inter and use it everywhere
> instead of the system font. Either approach is acceptable; do not mix them within one
> screen.

Use SF Pro **Display** (tighter tracking) for large titles, SF Pro **Text** for body
and UI. Numbers in status readouts should use tabular figures (`font-variant-numeric:
tabular-nums`) so counts don't jitter when they update.

### Type scale

| Role         | Size / line-height | Weight     | Use                                  |
|--------------|--------------------|------------|--------------------------------------|
| Large Title  | 34 / 41            | Bold       | Screen title ("Areas")               |
| Title 1      | 28 / 34            | Bold       | Section / sheet title                |
| Title 2      | 22 / 28            | Bold       | Card cluster heading                 |
| Headline     | 17 / 22            | Semibold   | Area name on a card                  |
| Body         | 17 / 22            | Regular    | Default text                         |
| Callout      | 16 / 21            | Regular    | Secondary info on cards              |
| Subhead      | 15 / 20            | Regular    | Metadata (last updated, steward)     |
| Footnote     | 13 / 18            | Regular    | Timestamps, fine print               |
| Caption      | 12 / 16            | Regular    | Labels under icons                   |

Apply slight negative letter-spacing (~ -0.02em) on Large Title / Title 1 only.
Default to **sentence case** everywhere, including buttons.

---

## 3. Colour

Reserve the **status ramp** (§3.3) strictly for crowd level and alerts. Nothing else —
not branding, not links, not buttons — may use those colours, or the traffic-light
meaning breaks down.

### 3.1 Neutrals — light mode
```css
--bg:               #F2F2F7;  /* app background (systemGroupedBackground) */
--surface:          #FFFFFF;  /* content cards */
--surface-2:        #F9F9FB;  /* nested / secondary surface */
--label:            #1C1C1E;  /* primary text */
--label-secondary:  rgba(60,60,67,0.60);
--label-tertiary:   rgba(60,60,67,0.30);
--separator:        rgba(60,60,67,0.29);
```

### 3.2 Neutrals — dark mode
```css
--bg:               #000000;
--surface:          #1C1C1E;
--surface-2:        #2C2C2E;
--label:            #FFFFFF;
--label-secondary:  rgba(235,235,245,0.60);
--label-tertiary:   rgba(235,235,245,0.30);
--separator:        rgba(84,84,88,0.65);
```

### 3.3 Status ramp (reserved — crowd level & alerts only)
```css
--status-calm:      #34C759;  /* Quiet — plenty of space        (systemGreen)  */
--status-moderate:  #FFCC00;  /* Moderate — filling up          (systemYellow) */
--status-busy:      #FF9500;  /* Busy — approaching capacity     (systemOrange) */
--status-critical:  #FF3B30;  /* Over capacity — alert           (systemRed)    */
```
Yellow has poor contrast for text; use it as a fill or dot with dark text on top, never
as coloured text on white.

### 3.4 Brand accent
```css
--accent: #5E5CE6;  /* systemIndigo — deliberately outside the status ramp */
```
Use for interactive controls (selected states, primary buttons, active tabs). Chosen so
it never reads as a crowd-status colour. Swap it for another festival colour if you like
— just keep it clear of green/yellow/orange/red.

---

## 4. Spacing, radius, elevation

**Spacing** — 4pt base, 8pt rhythm: `4, 8, 12, 16, 20, 24, 32, 40, 48`. Screen edge
padding is 16 on phones, 20–24 on wider screens. Be generous; Apple layouts breathe.

**Radius** — Apple uses continuous ("squircle") corners; large radii approximate them
on the web.
```css
--r-control: 10px;  /* buttons, inputs, segments */
--r-card:    18px;  /* status & alert cards */
--r-sheet:   24px;  /* modal sheets, large panels */
--r-pill:    999px; /* pills, chips, status dots */
```

**Elevation** — shadows are soft and low. Never heavy or dark.
```css
--shadow-card:  0 1px 3px rgba(0,0,0,0.06), 0 8px 24px rgba(0,0,0,0.05);
--shadow-glass: 0 8px 32px rgba(0,0,0,0.12), inset 0 1px 0 rgba(255,255,255,0.6);
```

---

## 5. Liquid Glass material

### 5.1 The recipe (chrome only)
```css
.glass {
  background: rgba(255, 255, 255, 0.60);           /* dark: rgba(30,30,30,0.50) */
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.50);     /* bright top-lit edge */
  border-radius: var(--r-sheet);
  box-shadow: var(--shadow-glass);
}
```

Real Liquid Glass does live light-bending (lensing), specular highlights that respond to
device motion, and refraction — CSS **cannot** fully reproduce this. The honest web
approximation is: `backdrop-filter` blur + saturate, a bright inset top border for the
"specular" edge, and a soft shadow. Don't promise refraction you can't deliver, and
don't fake it with distracting animated shimmer.

### 5.2 Where glass is allowed
Top / navigation bar · segmented control for reporting a level · the floating alert
banner · sheet headers · floating action buttons.

### 5.3 Where glass is forbidden
Area status cards · alert cards · any surface carrying data a person must read quickly ·
anything behind small body text over a busy photo (unless you add a solid scrim first).

### 5.4 Glass hygiene
- Never stack glass on glass — it turns to mud.
- Glass needs something behind it worth blurring (a photo, a colour field, the scrolling
  grid). Over a flat grey background it just looks foggy; use a solid surface instead.
- Always provide the reduced-transparency fallback in §8.

---

## 6. Components

### 6.1 Navigation bar (glass)
Translucent top bar that lets the status grid scroll under it. Large Title on load,
collapsing to an inline title on scroll (standard iOS behaviour). Holds the screen title
and a single primary action at most.

### 6.2 Area status card (solid — the core unit)
The workhorse. One card per area (Main Stage, Food Court, Bar, Entrance…).

- Surface: `--surface`, radius `--r-card`, shadow `--shadow-card`. **Not glass.**
- Area name in **Headline**.
- Crowd level shown three ways at once: a **coloured status dot/fill**, a **text label**
  ("Busy"), and an **icon** — so it never depends on colour alone.
- Metadata in Subhead: last updated time + reporting steward.
- The whole card's accent (dot, left edge, or fill tint) uses the matching status colour.
- A critical/over-capacity card gets a solid `--status-critical` treatment and is
  sorted to the top of the grid.

### 6.3 Crowd-level control (glass segmented control)
How a steward reports. An iOS segmented control with four options mapped to the ramp:
**Quiet · Moderate · Busy · Over capacity.** Selected segment fills with `--accent`;
each option shows its status colour as a leading dot so the mapping is obvious. Tapping
"Over capacity" is what can raise an alert.

### 6.4 Alert banner & alert list
When an area goes over capacity, a **glass banner** slides down from under the nav bar
and an alert card is added to the organisers' list.

- Banner: glass, with a solid `--status-critical` accent strip so it reads as urgent
  even through translucency. Never let the alert text sit on plain glass over a photo —
  add a scrim.
- Alert card (in the list): **solid**, high contrast. States the area, the level, the
  time, and the action.
- Organisers can acknowledge / resolve; resolved alerts move to a muted "history"
  section.

### 6.5 Buttons & controls
- **Primary:** filled `--accent`, white label, radius `--r-control`, 44px min height.
- **Secondary:** tinted (`--accent` at ~12% opacity) with `--accent` label.
- **Destructive:** `--status-critical` label/tint — but note this overlaps the status
  ramp, so only use it for genuinely destructive actions (e.g. "Delete area"), never for
  routine buttons.
- Pressed state: slight scale-down (0.97) + opacity dip. Clear keyboard focus ring
  (2px `--accent`, 2px offset).

---

## 7. Motion

Subtle, spring-like, purposeful. Apple's curves are softened, not bouncy-cartoonish.

- Transitions 0.25–0.4s, ease-out (or a gentle spring for the alert banner).
- Alert banner: springs down on arrival — this is the one moment allowed to grab the eye.
- Status changes: brief cross-fade / colour tween on the card, no more.
- Everything must be disabled under `prefers-reduced-motion` (§8).
- No ambient shimmer, no perpetual glass animation, no confetti. Extra motion reads as
  "AI-generated" and undermines the calm.

---

## 8. Accessibility (non-negotiable)

- **Contrast:** body text ≥ 4.5:1, large text ≥ 3:1. Glass lowers effective contrast —
  add a solid scrim behind any text on glass, and never put small text on glass over a
  photo.
- **Never colour alone.** Every crowd level = colour **+** label **+** icon.
- **Reduced transparency:**
  ```css
  @media (prefers-reduced-transparency: reduce) {
    .glass { background: var(--surface); backdrop-filter: none; -webkit-backdrop-filter: none; }
  }
  ```
- **Reduced motion:**
  ```css
  @media (prefers-reduced-motion: reduce) {
    * { animation: none !important; transition: none !important; }
  }
  ```
- **Tap targets** ≥ 44 × 44px.
- **Dark mode** fully supported (§3.2); test glass and status colours in both.
- Support Dynamic-Type-style scaling: use `rem`, don't hard-cap text size, keep layouts
  reflow-safe.

---

## 9. Voice & copy

Words are design material. Keep them plain, active, sentence case.

- Name things by what the user controls: "Report status," not "Submit crowd metric."
- Buttons say what happens: "Report status," "Acknowledge alert," "Mark resolved."
- Keep an action's name stable across the flow (the "Acknowledge" button produces an
  "Acknowledged" state).
- **Alert copy = area + level + action.** e.g. *"Main Stage is over capacity — dispatch
  stewards."* Not *"Alert triggered."*
- Empty states invite action: *"No alerts. All areas within capacity."*
- Errors are specific and blame-free: *"Couldn't send report. Check your connection and
  try again."*

---

## 10. Anti-patterns (do not do)

- Glass on the status or alert **cards** (content must be solid & legible).
- Reusing the status ramp (green/yellow/orange/red) for branding, buttons, or links.
- Signalling crowd level by **colour only**.
- Glass over a flat grey background, or glass stacked on glass.
- Heavy/dark drop shadows; hard 90° corners.
- Animating everything; shimmer, bounce, or confetti on alerts.
- Text over translucent glass on top of a photo with no scrim.
- All-caps labels or system-jargon copy ("Submit," "Metric updated").
