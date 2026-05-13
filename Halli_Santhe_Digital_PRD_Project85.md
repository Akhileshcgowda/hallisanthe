

MindMatrix VTU Internship ProgramPage 1
MindMatrix
MindMatrix VTU Internship Program
## Project Title: 85
n Halli-Santhe Digital – Hyper-Local
## Marketplace App
## Comprehensive Project Requirement Document
n  1. Project Overview
## Project Title:
Halli-Santhe Digital – An Android App for Hyper-Local Artisan Marketplace
## Objective:
Halli-Santhe Digital aims to bridge the gap between local village artisans and urban buyers by creating a
digital marketplace where artisans can list their products and buyers can discover and connect with
them — all without leaving their home.
The app provides a vibrant, intuitive interface for product discovery, artisan uploads, and buyer-seller
communication — built using Android Studio, Kotlin, Jetpack Compose, and GenAI capabilities.
## Core Idea:
"Digitize the weekly village market — making local goods discoverable, 24x7."
Halli-Santhe Digital focuses on empowering local artisans and promoting Vocal for Local —
transforming traditional physical markets into a "Phygital" commerce experience.
n  2. Purpose and Goals
## Purpose:
-  To create a fully functional Android app that demonstrates the students' understanding of Kotlin,
Jetpack Compose, Firebase/Room, and GenAI integration.
-  To build a real-world social impact use case that supports local artisans and traditional craft
preservation.
## Goals:
-  Enable artisans to upload and list products digitally.
-  Allow buyers to browse and discover local goods by category.
-  Facilitate direct buyer-seller communication via mock messaging.
-  Promote Indian traditional crafts and generate economic growth.
-  Provide a foundation for GenAI-enhanced product recommendations in the future.
n  3. Scope of the Application

MindMatrix VTU Internship ProgramPage 2
In-Scope Functionalities:
4  Product listing and categorization by artisans
4  Grid-based product browsing for buyers
4  Product detail view with Call-to-Action
4  Search functionality by product name
4  Mock buyer-seller messaging (check stock)
4  Local/Firebase data storage
4  GenAI-powered product description generation (optional)
4  Image upload with compression
Out of Scope (for this internship):
6  Real payment gateway integration
6  Live delivery tracking system
6  Cloud-based AI model training
6  Multi-vendor admin dashboard
(These can be considered as future extensions.)
n  4. Target Users and Personas
-  Village Artisans: To list handicrafts, handlooms, and local produce for broader discovery.
-  Urban Buyers / Consumers: To browse and purchase authentic local goods from the comfort of
home.
-  Local Market Organizers: To manage and promote their weekly bazaar digitally.
## User Persona Example:
## Name: Ramaiah K.
## Age: 45
Occupation: Traditional Toy Maker (Channapatna)
Goal: Wants to sell his wooden toys beyond the weekly haat bazaar.
Pain Points: No digital presence; buyers don't know he exists.
Expectation: A simple app where he can upload a photo and price — and buyers can contact him.
n  5. Functional Requirements
## 1   Artisan / Seller Module
-  Register or login as an artisan (optional for basic use).
-  Upload product with: Name, Price, Category, and 1 Photo.
-  Edit or delete existing product listings.
-  View all uploaded products in a personal dashboard.
## 2   Buyer / Browser Module

MindMatrix VTU Internship ProgramPage 3
-  Browse all products in a responsive grid layout (RecyclerView / LazyVerticalGrid).
-  Filter products by category (Handicrafts, Handloom, Produce, Toys, Others).
-  Search products by name in real time.
-  View product detail page with full image, price, and artisan info.
## 3   Communication Module
-  Buyers can send a mock "Check Stock" message to the seller.
-  Display seller contact intent (WhatsApp-style mock message).
-  Optional: In-app notification for new buyer inquiries.
4   GenAI Integration (Optional/Advanced)
-  Auto-generate product description using GenAI from image and product name.
-  Suggest relevant category using keyword/image analysis.
-  Optional integration with Gemini API for smart product tagging.
## 5   Empty States & Error Handling
-  Display a friendly "No products yet" screen when no listings exist.
-  Handle image load failures gracefully with placeholder images.
-  Show appropriate feedback for network or data errors.
n  6. Non-Functional Requirements
RequirementDescription
PerformanceProduct grid should load within 2 seconds; images load progressively.
UsabilityColorful, vibrant UI reflecting traditional Indian market aesthetics; minimal navigation.
ReliabilityApp should handle empty states gracefully without crashing.
PortabilityCompatible with Android 9.0 (API 28) and above.
ScalabilityArchitecture should support future AI recommendation and payment features.
SecurityArtisan credentials stored securely; Firebase security rules enforce data access
control.
n  7. System Architecture
## App Architecture:
-  MVVM Pattern (Model-View-ViewModel)
-  Local Database: Room (offline) / Firebase Firestore (online sync)
-  UI: Jetpack Compose / RecyclerView with GridLayoutManager

MindMatrix VTU Internship ProgramPage 4
-  Data Layer: Repository classes handle CRUD and API calls
-  View Layer: Screens built using Compose or XML layouts
-  GenAI Layer: Gemini API calls via Retrofit / OkHttp
## User
↓  View (Compose UI / RecyclerView)
↓  ViewModel
## ↓  Repository
↓  Room Database / Firebase Firestore / Gemini API
n  8. Database Schema
TableFieldsDescription
Productid (PK), name, price, category, image_url, artisan_id,
description, timestamp
Stores product listings
Artisanid (PK), name, village, phone, email, profile_imageArtisan/seller profile data
Categoryid (PK), name, icon_urlProduct category master list
## Message
## (optional)
id (PK), buyer_id, artisan_id, product_id, message,
timestamp
Mock buyer enquiry messages
n  9. User Flow Diagram (Text Outline)
## Artisan Flow:
-  Splash Screen → Register/Login as Artisan
-  Artisan Dashboard → "Add Product" (name, price, photo, category)
-  Save Product → Return to Dashboard (product listed)
-  View / Edit / Delete my listings
## Buyer Flow:
-  Splash Screen → Home (Grid of all products)
-  Browse by Category or Search by Name
## 3.  Tap Product → Product Detail View
-  Tap "Check Stock" → Mock message to seller
n  10. Reference Apps
AppDescriptionReference

MindMatrix VTU Internship ProgramPage 5
GramBazaarLocal artisan marketplace for handcrafted goods.Play Store
MeeshoReseller platform showcasing small-business products.meesho.com
India Handmade (GI
## Tag Store)
Government platform promoting GI-tagged Indian crafts.handicrafts.gov.in
n  11. Impact Goals
-  Economic Growth: Connecting village artisans directly to urban consumers, expanding their
market reach.
-  Preserving Crafts: Saving traditional arts like Channapatna toys, Bidriware, and handlooms by
increasing digital visibility.
-  Digital Commerce: Moving local weekly markets from purely physical to "Phygital" — physical +
digital.
-  Vocal for Local: Promoting indigenous Indian products and supporting the AtmaNirbhar Bharat
initiative.
n  12. Future Scope & Enhancement Ideas
-  AI-based product recommendation using GenAI/Gemini API.
-  Real payment gateway integration (Razorpay / UPI).
-  OCR-based price tag scanning for quick product upload.
-  Multi-language support (Kannada, Hindi, Tamil) for wider artisan reach.
-  Geo-location based discovery — "Artisans near me."
-  Cloud backup and artisan profile verification (Firebase + Aadhaar mock).
-  Goal-based savings tracker for artisans to plan inventory.