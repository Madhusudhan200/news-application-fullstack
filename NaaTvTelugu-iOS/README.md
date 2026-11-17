# NAA TV Telugu — Native iOS Application (Swift & SwiftUI)

Official native iOS application for **NAA TV Telugu** (`com.naatvtelugu.news`), crafted in SwiftUI and modern Swift concurrency. This iOS project provides 100% visual and functional parity with the Android Jetpack Compose application and follows the 4 reference layouts (Way2News style):

1. **Category Bottom Sheet Overlay** (Image 1): Search box, Local Categories (`Ponnawanda` with edit pencil and yellow pin), News Categories with live unread badge counters, Breaking News card.
2. **Home Explore Feed** (Image 2): User avatar, NAA TV Telugu logo with `#IndiaReadsNaaTv`, `+` post news shortcut, GPS location banner with live geocoding, horizontal swipe cards (`1 న్యూస్ of 7`, `2 పొన్నమండ of 7`), indicator dots, `── NEWS REELS ──` row with video badges, and persistent 5-tab bottom navigation bar (`హోమ్`, `న్యూస్`, elevated gold circular `కేటగిరీలు`, `రీల్స్`, `పొన్నమండ`).
3. **More / Profile Screen** (Image 3): User profile card, 9 Indian languages picker, location changer, category icons row with "క్రీడలు" (Sports) added and "మీకోసం" removed, Fact Check, Referral system with unique code (`NAA-ADMI-8514`) and WhatsApp invite, status videos, Admin panel link, reporter desk, settings, and feedback.
4. **News Watching Screen** (Image 4): Segmented pill (`న్యూస్` | `చదవనివి`), single-story full card with gold NAA TV badge, dynamic typography (customizable via Admin), likes/dislikes/bookmark/WhatsApp share (sending headline + image preview + app link without full body text), TTS speech audio reading, full story reader sheet, and bottom navigation bar (`← హోమ్`, `న్యూస్ ⌃`, `⟳ రీలోడ్`).

---

## 🎨 Color Palette & Design System
- **Navy Primary**: `#0A192F`
- **Navy Card / Elevation**: `#112240` / `#1E293B`
- **Gold Accent**: `#E5A93C` / `#F5B700`
- **Background**: `#050B14`
- **Typography**: System Dynamic Type with custom font size scaling (Headline: 16–32pt, Body: 12–22pt) and Telugu Unicode font rendering.

---

## 📱 Project Structure

```
NaaTvTelugu-iOS/
├── NaaTvTelugu.xcodeproj/
│   └── project.pbxproj         # Complete Xcode project file (iOS 16.0+, Swift 5.0)
├── NaaTvTelugu/
│   ├── NaaTvTeluguApp.swift    # @main App entry point
│   ├── Info.plist              # Permissions (CoreLocation, Camera, Photo Library, WhatsApp scheme)
│   ├── Theme/
│   │   └── Theme.swift         # Navy & Gold color definitions and brand styling
│   ├── Models/
│   │   ├── Models.swift        # NewsItem, NewsCategory, Categories list (with sports & no mi kosam)
│   │   ├── AppUser.swift       # User roles (admin, employee, user, guest), work status
│   │   ├── CustomPost.swift    # Reporter/admin submitted posts & status (pending/approved)
│   │   ├── AdminSettings.swift # Dynamic typography, logo size, aspect ratio, module toggles
│   │   └── AppLanguage.swift   # 9 Indian languages and localized strings
│   ├── Services/
│   │   ├── LocationManager.swift    # CoreLocation GPS with village/mandal/district reverse geocoder
│   │   ├── AuthStore.swift          # User sessions, demo logins, role management, work status
│   │   ├── NewsStore.swift          # Bookmarks, read tracking, custom posts, likes/dislikes
│   │   ├── AdminSettingsStore.swift # Admin settings persistence
│   │   └── NewsRepository.swift     # WordPress REST API client + curated Telugu fallback stories
│   ├── ViewModels/
│   │   └── NewsViewModel.swift      # MainActor state coordination, search, unread, share
│   ├── Views/
│   │   ├── RootView.swift                  # Navigation coordinator, back stack, bottom sheets
│   │   ├── SplashScreenView.swift          # White card, vibrant logo, 2018 copyright
│   │   ├── HomeExploreView.swift           # Reference Image 2 explore feed & reels
│   │   ├── NewsWatchingView.swift          # Reference Image 4 news card reader
│   │   ├── CategoriesSheetView.swift       # Reference Image 1 modal categories
│   │   ├── MoreProfileView.swift           # Reference Image 3 profile & options
│   │   ├── FullStorySheetView.swift        # Full article reader + TTS voice playback
│   │   ├── AdminPanelView.swift            # Approvals, live post, edit, typography sliders, staff directory
│   │   ├── EmployeePostView.swift          # Reporter news submission desk with GPS tagging
│   │   ├── SavedStoriesView.swift          # Bookmarked stories list
│   │   ├── AuthView.swift                  # Login/Register with 2018 copyright
│   │   ├── ReferralDialogView.swift        # Unique referral code & points dialog
│   │   ├── LocationPickerDialog.swift      # GPS detect + AP/Telangana districts
│   │   └── LanguageSelectionDialog.swift   # 9 Indian languages dialog
│   └── Resources/
│       └── Assets.xcassets/
│           ├── AppIcon.appiconset/          # 1024x1024 high-resolution AppIcon
│           ├── naa_tv_logo.imageset/        # Official vibrant NAA TV Telugu logo
│           ├── naa_tv_logo_white.imageset/  # Monochrome white logo variant
│           ├── AccentColor.colorset/        # Gold accent color asset
│           └── NavyColor.colorset/          # Navy brand color asset
└── README.md
```

---

## 🛠️ How to Build and Run on macOS / Xcode

### Prerequisites:
- macOS Ventura (13.0) or higher (Sonoma / Sequoia recommended).
- Xcode 14.2+ (Xcode 15 or 16 recommended).
- iOS 16.0+ Simulator or physical iPhone connected via USB.

### Steps:
1. Open terminal on your Mac and navigate to the project directory:
   ```bash
   cd "NaaTvTelugu-iOS"
   ```
2. Double-click or open `NaaTvTelugu.xcodeproj`:
   ```bash
   open NaaTvTelugu.xcodeproj
   ```
3. In Xcode, select the **NaaTvTelugu** scheme and your target device (e.g. `iPhone 15 Pro` or your connected physical iPhone).
4. Press `Cmd + R` to build and run the app.

---

## 🚀 Automated CI/CD (GitHub Actions)

A GitHub Actions workflow is provided at `.github/workflows/ios-build.yml`:
- Runs automatically on every `push` to `main` or via `workflow_dispatch`.
- Builds the application on a macOS runner using `xcodebuild`.
- Packages the app into an unsigned `.ipa` artifact (`NaaTvTelugu-Release.ipa`).
- Retains build artifacts for 14 days, downloadable directly from the GitHub Actions tab.

---

## 🔑 Demo Accounts for Testing
- **Admin**: `admin@naatvtelugu.com` / `admin123`
  - Access to Admin Panel, Approvals, Live News Publishing, Story Editing, Font/Logo Customization sliders, Staff Work Status switcher (`డ్యూటీలో ఉన్నారు`, `సెలవులో ఉన్నారు`, `రిపోర్టింగ్ ఫీల్డ్‌లో`).
- **Reporter**: `reporter@naatvtelugu.com` / `reporter123`
  - Access to Reporter Desk with GPS location verification and submission.
- **Guest / Reader**: Instant one-tap access without password.

---

## © Copyright
`© 2018 NAA TV Telugu. All Rights Reserved.`
