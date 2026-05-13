# Technical Notes & Lessons Learned

## Build Issues
- **Image Extensions**: Some assets from Stitch (e.g., `onboarding_artisan.png`) were actually JPEGs. Renaming them to `.jpg` was necessary for `AAPT2` to succeed.
- **Gradle Sync**: Always ensure Gradle sync is performed after adding new files to the project structure to avoid "unresolved reference" errors in IDE.

## UI/UX Best Practices
- **Premium Aesthetics**: Use `SurfaceContainerLowest` for card backgrounds and `Primary` for accents to match the "Tactile Modernism" theme.
- **Glassmorphism**: Use translucent backgrounds with blur (where possible in Compose) for a premium feel.
- **Micro-animations**: Use `AnimatedVisibility` and `animateDpAsState` for smoother transitions.
