import SwiftUI

public struct Theme {
    public static let navyPrimary = Color(red: 10/255, green: 25/255, blue: 47/255)       // #0A192F
    public static let darkBackground = Color(red: 5/255, green: 11/255, blue: 20/255)     // #050B14
    public static let navyCard = Color(red: 17/255, green: 34/255, blue: 64/255)         // #112240
    public static let goldColor = Color(red: 245/255, green: 183/255, blue: 0/255)        // #F5B700
    public static let goldLight = Color(red: 229/255, green: 169/255, blue: 60/255)      // #E5A93C
    public static let lightBackground = Color(red: 248/255, green: 249/255, blue: 250/255)
    public static let emeraldGreen = Color(red: 16/255, green: 185/255, blue: 129/255)
    public static let coralRed = Color(red: 239/255, green: 68/255, blue: 68/255)
}

extension Color {
    // Brand Colors
    public static let naaNavy = Theme.navyPrimary
    public static let naaNavyDark = Theme.darkBackground
    public static let naaGold = Theme.goldColor
    public static let naaGoldAccent = Theme.goldLight
    public static let naaCardDark = Theme.navyCard
    public static let naaLightBg = Theme.lightBackground
    public static let naaEmerald = Theme.emeraldGreen
    public static let naaRed = Theme.coralRed
    
    // Hex Color Initializer
    public init(hex: String) {
        let clean = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        Scanner(string: clean).scanHexInt64(&int)
        let a, r, g, b: UInt64
        switch clean.count {
        case 3: // RGB (12-bit)
            (a, r, g, b) = (255, (int >> 8) * 17, (int >> 4 & 0xF) * 17, (int & 0xF) * 17)
        case 6: // RGB (24-bit)
            (a, r, g, b) = (255, int >> 16, int >> 8 & 0xFF, int & 0xFF)
        case 8: // ARGB (32-bit)
            (a, r, g, b) = (int >> 24, int >> 16 & 0xFF, int >> 8 & 0xFF, int & 0xFF)
        default:
            (a, r, g, b) = (255, 0, 0, 0)
        }
        self.init(
            .sRGB,
            red: Double(r) / 255,
            green: Double(g) / 255,
            blue: Double(b) / 255,
            opacity: Double(a) / 255
        )
    }
    
    // Dynamic adaptive surfaces
    public static func screenBg(dark: Bool) -> Color {
        dark ? naaNavyDark : naaLightBg
    }
    
    public static func surface(dark: Bool) -> Color {
        dark ? naaCardDark : Color.white
    }
    
    public static func textPrimary(dark: Bool) -> Color {
        dark ? Color.white : Color(red: 17/255, green: 24/255, blue: 39/255)
    }
    
    public static func textSecondary(dark: Bool) -> Color {
        dark ? Color.white.opacity(0.7) : Color(red: 107/255, green: 114/255, blue: 128/255)
    }
    
    public static func border(dark: Bool) -> Color {
        dark ? Color.white.opacity(0.12) : Color.black.opacity(0.08)
    }
}

public struct NaaTvBrand {
    public static let site = "https://naatvtelugu.com/"
    public static let officialLogoUrl = "https://naatvtelugu.com/wp-content/uploads/2025/07/naatv_telugu_headder_logo.webp"
    public static let copyright = "© 2018 NAA TV Telugu. All Rights Reserved."
    public static let portal = "Official News Portal: naatvtelugu.com"
}
