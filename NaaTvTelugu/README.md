# NAA TV Telugu – Android App

A short-news app for **naatvtelugu.com**. It takes the features of Way2News and reverses the design.

| Way2News | NAA TV Telugu (this app) |
|---|---|
| Light theme by default | **Dark navy + gold** by default (light mode available) |
| Flip stories **up/down** | Swipe stories **left/right** with a card zoom effect |
| Photo on top, text below | **Headline + summary on top, photo at the bottom** |
| Categories in top tabs | Rounded category **chips**; bottom navigation bar |

**Content** comes live from your website through the WordPress REST API (`/wp-json/wp/v2/posts`). The app has these categories: All, News (వార్తలు), Cinema (సినిమా), Health & Fitness (ఆరోగ్యం) and Ayur Vedam (ఆయుర్వేదం). New posts appear in the app automatically.

**Logo** loads from your website's header and footer logo files.

**Features**
- About 60-word summary cards that load more stories as the reader swipes
- Full-article reader with YouTube embeds
- Bookmarks that are saved on the phone and readable offline
- Sharing to WhatsApp and other apps
- Dark/light toggle
- Links to YouTube, Facebook, Instagram, X and the Contact page

## Build the APK
1. Install **Android Studio** (Ladybug or newer).
2. Unzip this folder, then choose **File → Open** and select `NaaTvTelugu`. Wait for Gradle sync to finish.
3. Connect a phone with USB debugging enabled, or start an emulator, then press **Run ▶**.
4. To make an APK to share, choose **Build → Build App Bundle(s)/APK(s) → Build APK(s)**.
5. To publish on the Play Store, choose **Build → Generate Signed App Bundle** (.aab) and upload the file in Play Console.

## Things to change before publishing
- **App icon:** right-click `app/src/main/res`, choose **New → Image Asset**, and pick your NAA TV logo PNG. This replaces the placeholder gold-TV icon.
- **Categories:** edit `data/Models.kt → Categories.list`. Each slug must match the category slug in WordPress.
- **Colours:** edit `ui/theme/Theme.kt`.
- **Package name:** `com.naatvtelugu.news` (in `app/build.gradle.kts`).

## If no news loads
The app needs the WordPress REST API to be reachable. Open `https://naatvtelugu.com/wp-json/wp/v2/posts` in a browser. It should show JSON text. If it shows an error, check whether a security plugin (Wordfence, iThemes, "Disable REST API" and similar) is blocking `wp-json`. Allow it for public GET requests.
