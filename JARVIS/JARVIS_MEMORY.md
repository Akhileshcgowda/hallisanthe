# JARVIS Memory

## Current Project
**Title**: Halli-Santhe Digital Marketplace
**Objective**: Building a premium, culturally rich marketplace app connecting rural artisans with urban buyers.

## Project Analysis (2026-05-05)

### 🏗️ Architecture
- **UI Framework**: Jetpack Compose (Material 3).
- **Navigation**: Single Activity (`MainActivity`) with `NavHost` managing 22+ routes.
- **State Management**: ViewModel-driven (`CartViewModel`, `WishlistViewModel`) with shared `MutableState` for session-level flags (e.g., `isSeller`).
- **Data Pattern**: Mock-heavy development using `SampleData.kt` and strongly typed models in `Models.kt`.

### 🎨 UI & UX Design
- **Theme**: "Tactile Modernism" — focuses on earthy tones (`Primary`: Terracotta, `Secondary`: Amber), soft shadows, and heritage-inspired elements (Kolam patterns).
- **Branding**: Consistent use of the Halli-Santhe logo and branded artisan imagery.
- **Components**: Bento-grid layouts, custom dividers, and premium micro-animations (e.g., AI typing indicators).

### 📁 Feature Modules
- **Buyer Flow**: Onboarding → Login → Home → Search → Product Details → Cart → Checkout → Orders.
- **Seller Flow**: Onboarding → Login/Register → Dashboard → My Listings → Add Product → Inquiries → Santhe AI Assistant.
- **Support**: Notifications, Help & Support, Language Settings.

### 🛠️ Technical Debt & Notes
- **State Persistence**: Currently relies on process memory; needs local DB (Room) or Preferences DataStore for persistent login/seller status.
- **Mock Data**: Transitions to real API endpoints will require updating the Repository layer.
- **Assets**: High-resolution imagery exists but should be optimized for production.

## Project State
- **Phase 4 (Contextual Switching)**: Completed. The app now dynamically switches between Buyer and Seller views based on the `isSeller` state, including navigation bars and profile layouts.
- **Next Steps**: Implement persistent session storage (DataStore) so the role preference is remembered across app restarts.
