# LecMan Mobile Design Context (Material Design 3)

This document contains the core "Design DNA" extracted from the Stitch MCP UI generation for the LecMan Mobile app. It serves as the single source of truth for translating the web-based mockups into native Jetpack Compose code.

## 1. Color Palette (Hex)

We use a clean, minimal palette based on Deep Indigo and Teal, with specific semantic colors for urgent actions.

| Token             | Color Role               | Hex Value | Typical Usage                                     |
|-------------------|--------------------------|-----------|---------------------------------------------------|
| `md_theme_primary`| Primary                  | `#6750A4` | Top app bars, FABs, primary buttons, active tabs. |
| `md_theme_onPrimary`| On Primary             | `#FFFFFF` | Text/icons on top of primary color.               |
| `md_theme_secondary`| Teal Accent            | `#006B5E` | Secondary buttons, progress indicators.           |
| `md_theme_surface`| App Background           | `#FEFEFB` | Main screen background.                           |
| `md_theme_surfaceVariant`| Card Background   | `#F3EDF7` | Standard card backgrounds (e.g., Timetable cards).|
| `md_error`        | Urgent / Decline         | `#BA1A1A` | "Decline" actions, critical alerts.               |
| `md_warning`      | Attention / Countdown    | `#FFC107` | 15-minute countdown, pending tasks.               |
| `md_success`      | Accept                   | `#4CAF50` | "Accept" actions.                                 |

## 2. Typography Scale

Based on the Material 3 type scale, specifically tailored for mobile-first legibility.

| Name               | Size | Weight  | Usage                                          |
|--------------------|------|---------|------------------------------------------------|
| `Headline Large`   | 32sp | Regular | Primary screen greetings ("Good morning").     |
| `Title Large`      | 22sp | Medium  | Section headers ("Today's Quick Schedule").    |
| `Title Medium`     | 16sp | Medium  | Card titles, prominent list items.             |
| `Body Large`       | 16sp | Regular | Standard body text.                            |
| `Body Medium`      | 14sp | Regular | Secondary descriptions, timestamps, room info. |
| `Label Large`      | 14sp | Medium  | Button texts.                                  |

## 3. Spacing Guidelines

A strict 4dp grid system ensures rhythm and consistency across mobile screens.

| Token          | Value | Usage                                          |
|----------------|-------|------------------------------------------------|
| `space_extra_small` | 4dp   | Spacing between an icon and text within a chip.|
| `space_small`  | 8dp   | Padding inside cards, spacing between list items.|
| `space_medium` | 16dp  | Standard screen edge margins, primary padding. |
| `space_large`  | 24dp  | Vertical separation between major UI sections. |
| `space_extra_large`| 32dp  | Top margins above critical/urgent components.  |

## 4. Shape & Corner Radius

Material 3 uses rounded corners to create a friendly, approachable minimal aesthetic.

| Component      | Corner Radius | Notes                                          |
|----------------|---------------|------------------------------------------------|
| **Cards**      | `16dp`        | Applied to Schedule Cards, Request Cards.      |
| **Chips**      | `8dp`         | Department tags on the master timetable.       |
| **Small FAB**  | `16dp`        | Rounded square logic for secondary actions.    |
| **Buttons**    | `50%` (Pill)  | Fully rounded stadium shape for standard buttons.|
| **Bottom Sheet**| `28dp` (Top) | Top corners only, for standard modal sheets.   |
