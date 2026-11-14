# Product

<!-- impeccable:product-schema 1 -->

## Platform

android

## Users

Primary: University and college students in Vietnam (starting with key campuses such as ĐH Bách Khoa Hà Nội and neighboring universities).
Situation: Living on campus or nearby student housing on a tight student budget, regularly needing textbooks, electronics, dorm essentials, and fashion, or looking to declutter and earn pocket money.
Jobs to be done:
- Find affordable, trustworthy secondhand goods directly from peers within their university or nearby campuses.
- Quickly list unused items for sale, donation, or exchange without complex logistics.
- Engage with peers through interactive, fun campus commerce formats (blind boxes, auctions, live deals).

## Product Purpose

StuMart is a hyper-local campus marketplace and student community app designed to make peer-to-peer buying, selling, donating, and trading affordable, safe, and entertaining. Success means active student engagement, high trust among campus peers, frictionless local handoffs, and sustainable reuse of items within university communities.

## Positioning

Unlike generic classifieds or large e-commerce platforms (Shopee, Chợ Tốt) that involve shipping fees, scam risks, and impersonal sellers, StuMart is campus-anchored and peer-to-peer: verified by university context, designed for same-campus handoffs, and infused with playful, gamified student culture (blind boxes, live auctions, and the friendly duck mascot).

## Operating Context

- Mobile-first on Android smartphones used on campus, in dorms, in lecture halls, and on public transit.
- Transactions are primarily completed via in-person meetups on campus (dorms, library, campus gates) or local student delivery.
- Network environment varies between campus Wi-Fi and mobile data (4G/5G).

## Capabilities and Constraints

- **Campus & University Filtering**: Feed and listings tailored to the student's selected university/campus.
- **Listing Modes**: Standard Buy/Sell (`Bán thường`), Free Giveaways (`Cho tặng`), Item Exchange/Barter (`Trao đổi`), Blind Box (`Túi mù`), and Auctions (`Đấu giá`).
- **Media & Discovery**: Multi-photo uploads, category navigation, search, and recommendation feeds.
- **Community & Deals**: Student discount hub (`DiscountTab`), live interactive streams (`LiveTab`), and profile management (`ProfileTab`).
- **Tech Stack**: Native Android app written in Kotlin with Jetpack Compose, Material 3, and Supabase for authentication, database, and storage.

## Brand Commitments

- **Name**: StuMart (Student Mart).
- **Mascot**: Friendly duck ("Vịt con" 🦆), evoking cheerfulness, thriftiness, and camaraderie.
- **Personality & Tone**: Playful, lively, approachable, and warm. Humorous and student-friendly copy without feeling juvenile or unpolished.
- **Core Palette**: StuMart Yellow (`#FFD447`), StuMart Green (`#136E51`), StuMart Orange (`#FF7A45`), Warm Light Background (`#FBF8F1`), Dark Charcoal Text (`#333333`).

## Evidence on Hand

- Native Android source code in Jetpack Compose:
  - App screens: [HomeScreen.kt](file:///D:/StuMart/app/src/main/java/com/example/stumart/ui/screens/home/HomeScreen.kt), [OnboardingScreen.kt](file:///D:/StuMart/app/src/main/java/com/example/stumart/ui/screens/onboarding/OnboardingScreen.kt), [PostBottomSheet.kt](file:///D:/StuMart/app/src/main/java/com/example/stumart/ui/screens/home/PostBottomSheet.kt)
  - Color & theme tokens: [Color.kt](file:///D:/StuMart/app/src/main/java/com/example/stumart/ui/theme/Color.kt), [Theme.kt](file:///D:/StuMart/app/src/main/java/com/example/stumart/ui/theme/Theme.kt)
- Supabase integration configured in `SupabaseClient.kt`.

## Product Principles

1. **Campus Trust First**: Prioritize transparency, peer recognition, and university proximity to make transactions safe, familiar, and personal.
2. **Frictionless Student Commerce**: Minimize form friction for posting and browsing; students should be able to snap a photo and list in seconds.
3. **Joyful & Playful Interactions**: Infuse delight into everyday thrift culture with blind boxes, auctions, and warm duck-themed cues without cluttering functional tasks.
4. **Budget-Conscious Practicality**: Every feature should respect student realities—clear pricing, student discounts, and zero unnecessary fees.

## Accessibility & Inclusion

- Support clear contrast for outdoor daylight viewing around campus.
- Adhere to Android touch target sizes (minimum 48dp) and dynamic font scaling.
