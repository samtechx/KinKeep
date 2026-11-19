# KinKeep (working name; "Keep In Touch" also fine) — Phase 0 Spec

Status: design only. No code in this phase has been compiled or run.

## 1. Product summary

KinKeep is a fully offline Android app that reads your call history and contacts on-device, scores how "alive" each relationship is against a cadence you choose, and nudges you at the right time with warm, shame-free reminders.

Principles
- Private by construction: no INTERNET permission, no analytics, no network SDKs.
- Gentle: reminders invite, never scold. No red, no "you failed" language.
- Useful when permissions are denied: manual logging mode is a first-class path.

## 2. Assumptions (change any of these and tell me)

1. Distribution v1 = sideloaded APK for personal use. Google Play is a later, uncertain step (see section 9).
2. READ_SMS is NOT requested in v1. Play restricts it heavily and call log + manual logging cover the need. SMS/WhatsApp contact is captured via the manual "I spoke to them" quick-log, or when you tap the in-app SMS/WhatsApp button (logged as "reached out").
3. "Meaningful contact" = answered call of 60 s or more, any manual log, or an in-app outgoing SMS/WhatsApp tap. Calls under 60 s still count, at lower weight, but do not reset the "days since" clock. Missed calls never reset it.
4. Cadence defaults: Inner Circle 7 d, Close Friends & Family 14 d, Friends 30 d, Acquaintances 90 d, Ignore = not scored.
5. Notes are encrypted at rest (SQLCipher for the whole Room DB; key held in Android Keystore).
6. Languages: English first; all strings in resources.
7. Name: I suggest KinKeep (short, memorable, fits the project). Easy to rename.

## 3. Palette (dark green + white, WCAG AA checked)

Contrast ratios below were computed, not estimated.

Light theme
| Role | Hex | Check |
|---|---|---|
| primary | #0B3D2E | on white 12.20 |
| onPrimary | #FFFFFF | |
| primaryContainer | #CDE8DC | |
| onPrimaryContainer | #062219 | 12.93 |
| background | #F6FAF8 | |
| surface | #FFFFFF | |
| onSurface | #10201A | 16.89 |
| surfaceVariant | #E6F0EB | |
| onSurfaceVariant | #3F5249 | 7.17 |
| outline | #6F8277 | 4.09 (needs 3:1) |

Dark theme
| Role | Hex | Check |
|---|---|---|
| primary | #7FD1AE | on #00281B 8.82 |
| onPrimary | #00281B | |
| primaryContainer | #0B3D2E | |
| onPrimaryContainer | #CDE8DC | 9.40 |
| background / surface | #0E1713 | |
| onSurface | #E3EEE8 | 15.35 |
| surfaceVariant | #24342C | |
| onSurfaceVariant | #B7C9BF | 7.56 |

Health status colors (text/icon on surface; always paired with an icon + label, never color alone)
| Category | Light | on white | Dark | on #0E1713 | Icon |
|---|---|---|---|---|---|
| Thriving | #0B6B4A | 6.53 | #5FD0A0 | 9.57 | favorite |
| Healthy | #2E7D57 | 5.02 | #7FD1AE | 10.11 | check_circle |
| Needs Attention | #5B7F2B | 4.65 | #B5D66F | 11.14 | schedule |
| Fading | #B45309 (single warm accent) | 5.02 | #F2A65A | 9.02 | trending_down |
| Dormant | #5E6F66 | 5.33 | #9DB0A6 | 7.99 | bedtime |

All text pairs pass AA (4.5:1). Fading uses the one warm accent; there is no red anywhere.

## 4. Data model (Room)

- `person` (id, displayName, photoUri?, circle, customCadenceDays?, preferredMethod, birthday?, anniversary?, muted, pinned, createdAt, systemContactId?)
- `person_phone` (id, personId FK, normalizedE164, rawNumber, label) — index on normalizedE164 (unique per person; resolves multiple numbers and merges)
- `interaction` (id, personId FK, type [CALL_IN, CALL_OUT, CALL_MISSED, MANUAL, SMS_TAP, WHATSAPP_TAP], timestamp, durationSec, note?, source [CALL_LOG, MANUAL, APP_ACTION], callLogId? unique) — indices (personId, timestamp), (callLogId)
- `score_snapshot` (personId, date, score, category, recency, frequency, balance, missed) — one row per person per day, feeds score history and the weekly trend
- `note` (id, personId, createdAt, body, isTalkAboutItem, done)
- `reminder` (id, personId, kind [REACH_OUT, BIRTHDAY, ANNIVERSARY, MISSED_CALL], fireAt, snoozedUntil?, status)
- `sync_state` (key, value) — last call-log date/id processed, last contacts version
- DataStore (not Room): theme, reminder default time, quiet hours, daily cap, digest, pause mode, app lock, language, circle cadences, scoring weights.

Duplicate/merged contacts: match by normalized phone (E.164 via libphonenumber-style normalization); unmatched call-log numbers go to an "Unknown numbers" bucket the user can attach to a person. Merging two people moves phones and interactions to the survivor.

## 5. Relationship Health Score

Let C = target cadence in days (custom per person, else circle default). All timestamps use the device time zone; "days" are computed on local calendar dates, so DST shifts never create a 23/25-hour off-by-one.

Components, each 0–100:

1. Recency R. d = days since last meaningful contact, r = d / C. Piecewise linear through (r=0.5 → 100), (1 → 80), (2 → 45), (4 → 10), (6 → 0); 100 below 0.5, 0 beyond 6.
2. Frequency F. Over the last 90 days, sum interaction weights w, compare with expected E = max(1, 90 / C). F = 100 · min(1, Σw / E).
   - Call weight by duration: w = 0.3 + 0.7 · ln(1 + minutes) / ln(11), capped at 1. A 30 s call = 0.42, a 5-min call = 0.80, a 10-min call = 1.0.
   - Manual log: w = 0.7 if no duration given, else the call formula. SMS/WhatsApp tap: 0.25. Max one counted interaction-day per channel to stop 20 quick calls in a day inflating it (daily cap of 1.5 total weight per day).
3. Balance B. share = outgoing / (outgoing + incoming) over 90 days, counting answered calls and manual logs marked who initiated. B = 100 · (1 − 1.6 · |share − 0.5|), floor 20. If fewer than 3 interactions, B = 70 (neutral).
4. Missed M. Unreturned = incoming missed call with no answered call and no outgoing call to that person within 48 h after it. Each counts 1.0 if within 14 days, 0.5 if older (up to 45 days). M = 100 − 25 · min(4, Σ).

Score = round(0.45·R + 0.25·F + 0.15·B + 0.15·M). Weights, the recency curve points, and thresholds live in a single `ScoringConfig` data class (defaults above), so they are tunable and unit-testable.

Edge cases
- No history: score is null and the category is "New" for a grace period of C days from the date added, so nobody is born "Dormant". Reach-out nudges can still fire after the grace period.
- Brand-new contact with one interaction: scored normally; B = 70.
- Ignore circle / muted: not scored, not in the overall ring, no reminders.
- Clamps: never Fading or Dormant while r ≤ 1; always Dormant when r > 6.

Categories (score → label)
| Range | Category | Meaning shown in-app |
|---|---|---|
| 85–100 | Thriving | You're in a good rhythm. |
| 65–84 | Healthy | Steady contact, on track. |
| 45–64 | Needs Attention | A catch-up would feel good soon. |
| 25–44 | Fading | It's been a while — a short hello goes a long way. |
| 0–24 | Dormant | Quiet for a long time. Reconnect when it feels right. |

Overall health ring = mean of scores weighted by circle importance (Inner 3, Close 2, Friends 1, Acquaintances 0.5). Weekly trend = weighted mean of `score_snapshot` per week.

## 6. Reminder logic

- Candidate = scored person with r ≥ 0.9 (due), not muted, not snoozed, circle ≠ Ignore. Ranked by importance × overdue ratio.
- Time of day: per person, the median hour of their past outgoing contacts (needs ≥ 3 data points), rounded to the half hour; otherwise the user's default time.
- Guards: quiet hours, pause mode, daily cap (default 3; extras roll into a digest if enabled).
- Scheduling: one daily WorkManager planner run computes the day's reminders, then sets inexact-while-idle alarms; exact alarms (USE_EXACT_ALARM / SCHEDULE_EXACT_ALARM) only for birthdays and user-snoozed times. A BOOT_COMPLETED receiver re-plans after reboot. Time-zone/DST change broadcasts trigger a re-plan.
- Actions: Call now, Message, Snooze (later today / tomorrow / next week), Already spoke (creates a manual log).
- Copy: 30+ rotating templates, all invitational ("{name} would probably love to hear from you", "No pressure — a quick hello to {name}?").

## 7. Key user flows

1. First run: Welcome (value) → Privacy promise (offline, no INTERNET) → Contacts rationale → grant → Call log rationale → grant → Notification rationale → grant → import progress → suggested circles (editable list, bulk move) → pick reminder time → Dashboard.
2. Permission denied: banner on Dashboard ("Manual mode"), quick-log is front and center, a "Turn on call history" link re-opens the rationale.
3. Daily use: notification → Call now (dialer intent) → on return, app offers "How did it go?" quick-log.
4. Contact detail: header score + category, score history chart, timeline, notes / "talk about" list, birthday, preferred method, buttons (Call, SMS, WhatsApp via wa.me intent, Log).
5. Settings: circles & cadences, reminders, quiet hours, pause, per-contact mute, theme, app lock, language, encrypted backup export/import, delete all data, "keep reminders reliable" screen.

## 8. Architecture

Single Gradle module `app` to start, split by package so it can be modularized later without rewrites:
`data/` (Room, content providers, repositories), `domain/` (models, use cases, `ScoringEngine`, `ReminderPlanner`), `ui/` (Compose screens, ViewModels, theme), `work/` (SyncWorker, ScoreWorker, ReminderWorker, receivers), `widget/` (Glance). Hilt for DI. Kotlin Coroutines/Flow, Paging 3 for lists, SQLCipher for the database.

Performance plan: call log read in 500-row batches with an incremental `DATE > lastSynced` query; bulk inserts in one transaction; scores recomputed only for people with new interactions plus a nightly full pass; all list screens use paging.

## 9. Distribution and Play Store risk (read this)

- Sideloading a debug or release-signed APK for yourself works with no review.
- READ_CALL_LOG on Google Play is a restricted permission. Play generally approves it only for apps that are the default phone/SMS handler or fit a narrow list of exceptions; a relationship-tracking app is likely to be rejected or asked to change. Treat the Play Store as uncertain until the Permissions Declaration Form is actually accepted. The fallback is a Play version without call-log access (manual logging only) plus an APK/F-Droid-style direct download with call-log import.
- The Phase 7 deliverables (declaration text, privacy policy, Data Safety answers) will be written either way.

## 10. Phase plan (unchanged from your brief)

0 spec (this file) → 1 project + design system + navigation + DB → 2 import + scoring + tests → 3 dashboard + detail → 4 reminders + widget → 5 settings/backup/lock/a11y → 6 testing/perf/OEM → 7 release + Play assets.

## 11. Phase 0 verification checklist

- [ ] Do the default cadences (7/14/30/90 d) match how often you actually want to be in touch?
- [ ] Are you comfortable with "meaningful contact = answered call of 60 s or more, or a manual log"?
- [ ] Is dropping READ_SMS in v1 acceptable?
- [ ] Are the scoring weights (recency 45, frequency 25, balance 15, missed 15) right for you?
