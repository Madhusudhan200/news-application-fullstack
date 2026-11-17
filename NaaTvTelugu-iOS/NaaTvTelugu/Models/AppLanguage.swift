import Foundation

public enum AppLanguage: String, Codable, CaseIterable, Identifiable {
    case telugu = "te"
    case english = "en"
    case hindi = "hi"
    case tamil = "ta"
    case malayalam = "ml"
    case kannada = "kn"
    case marathi = "mr"
    case bengali = "bn"
    case gujarati = "gu"
    
    public var id: String { rawValue }
    
    public var nativeName: String {
        switch self {
        case .telugu: return "తెలుగు"
        case .english: return "English"
        case .hindi: return "हिन्दी"
        case .tamil: return "தமிழ்"
        case .malayalam: return "മലയാളം"
        case .kannada: return "ಕನ್ನಡ"
        case .marathi: return "मराठी"
        case .bengali: return "বাংলা"
        case .gujarati: return "ગુજરાતી"
        }
    }
    
    public var englishName: String {
        switch self {
        case .telugu: return "Telugu"
        case .english: return "English"
        case .hindi: return "Hindi"
        case .tamil: return "Tamil"
        case .malayalam: return "Malayalam"
        case .kannada: return "Kannada"
        case .marathi: return "Marathi"
        case .bengali: return "Bengali"
        case .gujarati: return "Gujarati"
        }
    }
}

public struct AppStrings {
    public static func tagline(lang: AppLanguage) -> String {
        switch lang {
        case .telugu: return "క్షణాల్లో తాజా వార్తలు • ఎప్పటికప్పుడు ప్రత్యక్ష ప్రసారం"
        case .english: return "Instant Verified News • Real-time Updates"
        case .hindi: return "क्षणों में ताज़ा समाचार • रियल-टाइम अपडेट"
        case .tamil: return "நொடிகளில் முக்கிய செய்திகள் • உடனுக்குடன்"
        case .malayalam: return "നിമിഷങ്ങൾക്കുള്ളിൽ പുതിയ വാർത്തകൾ • തത്സമയ അപ്‌ഡേറ്റുകൾ"
        case .kannada: return "ಕ್ಷಣಗಳಲ್ಲಿ ಪ್ರಮುಖ ಸುದ್ದಿಗಳು • ನೈಜ ಸಮಯದ ನವೀಕರಣಗಳು"
        case .marathi: return "क्षणार्धात ताज्या बातम्या • रिअल-टाइम अपडेट्स"
        case .bengali: return "মুহূর্তের মধ্যে তাজা খবর • রিয়েল-টাইম আপডেট"
        case .gujarati: return "ક્ષણોમાં તાજા સમાચાર • રીઅલ-ટાઇમ અપડેટ્સ"
        }
    }
    
    public static func tabHome(lang: AppLanguage) -> String {
        switch lang {
        case .telugu: return "హోమ్"
        case .english: return "Home"
        case .hindi: return "होम"
        case .tamil: return "முகப்பு"
        case .malayalam: return "ഹോം"
        case .kannada: return "ಮುಖಪುಟ"
        case .marathi: return "होम"
        case .bengali: return "হোম"
        case .gujarati: return "હોમ"
        }
    }
    
    public static func tabNews(lang: AppLanguage) -> String {
        switch lang {
        case .telugu: return "న్యూస్"
        case .english: return "News"
        case .hindi: return "समाचार"
        case .tamil: return "செய்திகள்"
        case .malayalam: return "വാർത്തകൾ"
        case .kannada: return "ಸುದ್ದಿ"
        case .marathi: return "बातम्या"
        case .bengali: return "সংবাদ"
        case .gujarati: return "સમાચાર"
        }
    }
    
    public static func tabCategories(lang: AppLanguage) -> String {
        switch lang {
        case .telugu: return "కేటగిరీలు"
        case .english: return "Categories"
        case .hindi: return "श्रेणियाँ"
        case .tamil: return "வகைகள்"
        case .malayalam: return "വിഭാഗങ്ങൾ"
        case .kannada: return "ವರ್ಗಗಳು"
        case .marathi: return "वर्गवारी"
        case .bengali: return "বিভাগসমূহ"
        case .gujarati: return "શ્રેણીઓ"
        }
    }
    
    public static func tabReels(lang: AppLanguage) -> String {
        return "రీల్స్"
    }
    
    public static func searchPlaceholder(lang: AppLanguage) -> String {
        switch lang {
        case .telugu: return "సెర్చ్ చేయండి.."
        case .english: return "Search news.."
        default: return "సెర్చ్ చేయండి.."
        }
    }
    
    public static func unreadStories(lang: AppLanguage) -> String {
        switch lang {
        case .telugu: return "చదవనివి"
        case .english: return "Unread"
        default: return "చదవనివి"
        }
    }
    
    public static func fullStory(lang: AppLanguage) -> String {
        switch lang {
        case .telugu: return "పూర్ణ వార్త →"
        case .english: return "Full Story →"
        default: return "పూర్ణ వార్త →"
        }
    }
}
