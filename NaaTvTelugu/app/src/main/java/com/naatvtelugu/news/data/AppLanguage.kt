package com.naatvtelugu.news.data

enum class AppLanguage(
    val code: String,
    val nativeName: String,
    val englishName: String,
) {
    TELUGU("te", "తెలుగు", "Telugu"),
    ENGLISH("en", "English", "English"),
    HINDI("hi", "हिंदी", "Hindi"),
    TAMIL("ta", "தமிழ்", "Tamil"),
    MALAYALAM("ml", "മലയാളം", "Malayalam"),
    KANNADA("kn", "ಕನ್ನಡ", "Kannada"),
    MARATHI("mr", "मराठी", "Marathi"),
    BENGALI("bn", "বাংলা", "Bengali"),
    GUJARATI("gu", "ગુજરાતી", "Gujarati");

    companion object {
        fun fromCode(code: String): AppLanguage =
            entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: TELUGU
    }
}

object AppStrings {
    fun tagline(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "క్షణాల్లో తాజా వార్తలు • ఎప్పటికప్పుడు ప్రత్యక్ష ప్రసారం"
        AppLanguage.ENGLISH -> "Instant Verified News • Real-time Updates"
        AppLanguage.HINDI -> "क्षणों में ताज़ा समाचार • रियल-टाइम अपडेट"
        AppLanguage.TAMIL -> "நொடிகளில் முக்கிய செய்திகள் • உடனுக்குடன்"
        AppLanguage.MALAYALAM -> "നിമിഷങ്ങൾക്കുള്ളിൽ പുതിയ വാർത്തകൾ • തത്സമയ അപ്‌ഡേറ്റുകൾ"
        AppLanguage.KANNADA -> "ಕ್ಷಣಗಳಲ್ಲಿ ಪ್ರಮುಖ ಸುದ್ದಿಗಳು • ನೈಜ ಸಮಯದ ನವೀಕರಣಗಳು"
        AppLanguage.MARATHI -> "क्षणार्धात ताज्या बातम्या • रिअल-टाइम अपडेट्स"
        AppLanguage.BENGALI -> "মুহূর্তের মধ্যে তাজা খবর • রিয়েল-টাইম আপডেট"
        AppLanguage.GUJARATI -> "ક્ષણોમાં તાજા સમાચાર • રીઅલ-ટાઇમ અપડેટ્સ"
    }

    fun loginTab(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "లాగిన్ (Login)"
        AppLanguage.ENGLISH -> "Login"
        AppLanguage.HINDI -> "लॉग इन (Login)"
        AppLanguage.TAMIL -> "உள்நுழை (Login)"
        AppLanguage.MALAYALAM -> "ലോഗിൻ (Login)"
        AppLanguage.KANNADA -> "ಲಾಗಿನ್ (Login)"
        AppLanguage.MARATHI -> "लॉगिन (Login)"
        AppLanguage.BENGALI -> "লগইন (Login)"
        AppLanguage.GUJARATI -> "લૉગિન (Login)"
    }

    fun signUpTab(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "ఖాతా తెరవండి (Sign Up)"
        AppLanguage.ENGLISH -> "Sign Up"
        AppLanguage.HINDI -> "साइन अप (Sign Up)"
        AppLanguage.TAMIL -> "பதிவு செய்க (Sign Up)"
        AppLanguage.MALAYALAM -> "സൈൻ അപ്പ് (Sign Up)"
        AppLanguage.KANNADA -> "ಖಾತೆ ತೆರೆಯಿರಿ (Sign Up)"
        AppLanguage.MARATHI -> "साइन अप (Sign Up)"
        AppLanguage.BENGALI -> "সাইন আপ (Sign Up)"
        AppLanguage.GUJARATI -> "સાઇન અપ (Sign Up)"
    }

    fun fullName(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "మీ పేరు (Full Name)"
        AppLanguage.ENGLISH -> "Full Name"
        AppLanguage.HINDI -> "आपका नाम (Full Name)"
        AppLanguage.TAMIL -> "முழு பெயர் (Full Name)"
        AppLanguage.MALAYALAM -> "പൂർണ്ണമായ പേര് (Full Name)"
        AppLanguage.KANNADA -> "ಪೂರ್ಣ ಹೆಸರು (Full Name)"
        AppLanguage.MARATHI -> "पूर्ण नाव (Full Name)"
        AppLanguage.BENGALI -> "সম্পূর্ণ নাম (Full Name)"
        AppLanguage.GUJARATI -> "પૂરું નામ (Full Name)"
    }

    fun email(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "ఇమెయిల్ (Email ID)"
        AppLanguage.ENGLISH -> "Email ID"
        AppLanguage.HINDI -> "ईमेल (Email ID)"
        AppLanguage.TAMIL -> "மின்னஞ்சல் (Email ID)"
        AppLanguage.MALAYALAM -> "ഇമെയിൽ (Email ID)"
        AppLanguage.KANNADA -> "ಇಮೇಲ್ (Email ID)"
        AppLanguage.MARATHI -> "ईमेल (Email ID)"
        AppLanguage.BENGALI -> "ইমেল (Email ID)"
        AppLanguage.GUJARATI -> "ઇમેઇલ (Email ID)"
    }

    fun password(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "పాస్‌వర్డ్ (Password)"
        AppLanguage.ENGLISH -> "Password"
        AppLanguage.HINDI -> "पासवर्ड (Password)"
        AppLanguage.TAMIL -> "கடவுச்சொல் (Password)"
        AppLanguage.MALAYALAM -> "പാസ്‌വേഡ് (Password)"
        AppLanguage.KANNADA -> "ಪಾಸ್‌ವರ್ಡ್ (Password)"
        AppLanguage.MARATHI -> "पासवर्ड (Password)"
        AppLanguage.BENGALI -> "পাসওয়ার্ড (Password)"
        AppLanguage.GUJARATI -> "પાસવર્ડ (Password)"
    }

    fun signInBtn(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "ప్రవేశించండి (Sign In)"
        AppLanguage.ENGLISH -> "Sign In"
        AppLanguage.HINDI -> "साइन इन करें (Sign In)"
        AppLanguage.TAMIL -> "உள்நுழையவும் (Sign In)"
        AppLanguage.MALAYALAM -> "സൈൻ ഇൻ ചെയ്യുക (Sign In)"
        AppLanguage.KANNADA -> "ಸೈನ್ ಇನ್ ಮಾಡಿ (Sign In)"
        AppLanguage.MARATHI -> "साइन इन करा (Sign In)"
        AppLanguage.BENGALI -> "সাইন ইন করুন (Sign In)"
        AppLanguage.GUJARATI -> "સાઇન ઇન કરો (Sign In)"
    }

    fun signUpBtn(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "ఖాతా సృష్టించండి (Sign Up)"
        AppLanguage.ENGLISH -> "Create Account"
        AppLanguage.HINDI -> "खाता बनाएं (Sign Up)"
        AppLanguage.TAMIL -> "கணக்கை உருவாக்கவும் (Sign Up)"
        AppLanguage.MALAYALAM -> "അക്കൗണ്ട് സൃഷ്ടിക്കുക (Sign Up)"
        AppLanguage.KANNADA -> "ಖಾತೆ ರಚಿಸಿ (Sign Up)"
        AppLanguage.MARATHI -> "खाते तयार करा (Sign Up)"
        AppLanguage.BENGALI -> "অ্যাকাউন্ট তৈরি করুন (Sign Up)"
        AppLanguage.GUJARATI -> "ખાતું બનાવો (Sign Up)"
    }

    fun guestLogin(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "గెస్ట్‌గా కొనసాగండి (Login as Guest)"
        AppLanguage.ENGLISH -> "Continue as Guest"
        AppLanguage.HINDI -> "अतिथि के रूप में जारी रखें (Guest)"
        AppLanguage.TAMIL -> "விருந்தினராக தொடரவும் (Guest)"
        AppLanguage.MALAYALAM -> "ഗസ്റ്റായി തുടരുക (Guest)"
        AppLanguage.KANNADA -> "ಅತಿಥಿಯಾಗಿ ಮುಂದುವರಿಯಿರಿ (Guest)"
        AppLanguage.MARATHI -> "पाहुणे म्हणून सुरू ठेवा (Guest)"
        AppLanguage.BENGALI -> "অতিথি হিসেবে চালিয়ে যান (Guest)"
        AppLanguage.GUJARATI -> "અતિથિ તરીકે આગળ વધો (Guest)"
    }

    fun guestDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "ఇమెయిల్ లేకుండా తక్షణమే వార్తలు చదవడానికి గెస్ట్ మోడ్ ఎంచుకోండి"
        AppLanguage.ENGLISH -> "Read all news instantly without entering credentials"
        AppLanguage.HINDI -> "बिना किसी क्रेडेंशियल के तुरंत समाचार पढ़ें"
        AppLanguage.TAMIL -> "விவரங்களை உள்ளிடாமல் உடனடியாக செய்திகளைப் படிக்கவும்"
        AppLanguage.MALAYALAM -> "വിവരങ്ങൾ നൽകാതെ ഉടൻ വാർത്തകൾ വായിക്കുക"
        AppLanguage.KANNADA -> "ವಿವರಗಳನ್ನು ನಮೂದಿಸದೆ ತಕ್ಷಣವೇ ಸುದ್ದಿಗಳನ್ನು ಓದಿ"
        AppLanguage.MARATHI -> "माहिती न भरता त्वरित बातम्या वाचा"
        AppLanguage.BENGALI -> "কোনো তথ্য ছাড়াই তাৎক্ষণিকভাবে খবর পড়ুন"
        AppLanguage.GUJARATI -> "વિગતો આપ્યા વિના તરત સમાચાર વાંચો"
    }

    fun copyright(): String = "© 2018 NAA TV Telugu. All Rights Reserved."

    fun adminStaffNotice(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "అడ్మిన్ మరియు ఉద్యోగులు తమ అధికారిక క్రెడెన్షియల్స్‌తో లాగిన్ కావచ్చు."
        AppLanguage.ENGLISH -> "Admin and staff members may log in using authorized credentials."
        AppLanguage.HINDI -> "व्यवस्थापक और कर्मचारी अपने अधिकृत विवरण से लॉगिन कर सकते हैं।"
        AppLanguage.TAMIL -> "நிர்வாகி மற்றும் ஊழியர்கள் அங்கீகரிக்கப்பட்ட சான்றுகளுடன் உள்நுழையலாம்."
        AppLanguage.MALAYALAM -> "അഡ്മിനും ജീവനക്കാർക്കും ഔദ്യോഗിക വിവരങ്ങൾ ഉപയോഗിച്ച് ലോഗിൻ ചെയ്യാം."
        AppLanguage.KANNADA -> "ನಿರ್ವಾಹಕರು ಮತ್ತು ಸಿಬ್ಬಂದಿ ಅಧಿಕೃತ ರುಜುವಾತುಗಳೊಂದಿಗೆ ಲಾಗಿನ್ ಮಾಡಬಹುದು."
        AppLanguage.MARATHI -> "प्रशासक आणि कर्मचारी अधिकृत क्रेडेंशियल्ससह लॉगिन करू शकतात."
        AppLanguage.BENGALI -> "অ্যাডমিন এবং কর্মীরা অনুমোদিত তথ্য দিয়ে লগইন করতে পারেন।"
        AppLanguage.GUJARATI -> "એડમિન અને સ્ટાફ સત્તાવાર ઓળખપત્રો વડે લૉગિન કરી શકે છે."
    }

    fun forYou(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "తాజా వార్తలు"
        AppLanguage.ENGLISH -> "Latest News"
        AppLanguage.HINDI -> "ताज़ा समाचार"
        AppLanguage.TAMIL -> "சமீபத்திய செய்திகள்"
        AppLanguage.MALAYALAM -> "പുതിയ വാർത്തകൾ"
        AppLanguage.KANNADA -> "ಇತ್ತೀಚಿನ ಸುದ್ದಿ"
        AppLanguage.MARATHI -> "ताज्या बातम्या"
        AppLanguage.BENGALI -> "তাজা খবর"
        AppLanguage.GUJARATI -> "તાજા સમાચાર"
    }

    fun sports(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "క్రీడలు"
        AppLanguage.ENGLISH -> "Sports"
        AppLanguage.HINDI -> "खेल"
        AppLanguage.TAMIL -> "விளையாட்டு"
        AppLanguage.MALAYALAM -> "കായികം"
        AppLanguage.KANNADA -> "ಕ್ರೀಡೆಗಳು"
        AppLanguage.MARATHI -> "क्रीडा"
        AppLanguage.BENGALI -> "খেলাধুলা"
        AppLanguage.GUJARATI -> "રમતગમત"
    }

    fun politics(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "రాజకీయాలు"
        AppLanguage.ENGLISH -> "Politics"
        AppLanguage.HINDI -> "राजनीति"
        AppLanguage.TAMIL -> "அரசியல்"
        AppLanguage.MALAYALAM -> "രാഷ്ട്രീയം"
        AppLanguage.KANNADA -> "ರಾಜಕೀಯ"
        AppLanguage.MARATHI -> "राजकारण"
        AppLanguage.BENGALI -> "রাজনীতি"
        AppLanguage.GUJARATI -> "રાજકારણ"
    }

    fun business(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "వ్యాపారం"
        AppLanguage.ENGLISH -> "Business"
        AppLanguage.HINDI -> "व्यापार"
        AppLanguage.TAMIL -> "வணிகம்"
        AppLanguage.MALAYALAM -> "ബിസിനസ്"
        AppLanguage.KANNADA -> "ವ್ಯಾಪಾರ"
        AppLanguage.MARATHI -> "व्यवसाय"
        AppLanguage.BENGALI -> "ব্যবসা"
        AppLanguage.GUJARATI -> "વેપાર"
    }

    fun news(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "వార్తలు"
        AppLanguage.ENGLISH -> "News"
        AppLanguage.HINDI -> "समाचार"
        AppLanguage.TAMIL -> "செய்திகள்"
        AppLanguage.MALAYALAM -> "വാർത്തകൾ"
        AppLanguage.KANNADA -> "ಸುದ್ದಿ"
        AppLanguage.MARATHI -> "बातम्या"
        AppLanguage.BENGALI -> "সংবাদ"
        AppLanguage.GUJARATI -> "સમાચાર"
    }

    fun cinema(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "సినిమా"
        AppLanguage.ENGLISH -> "Cinema"
        AppLanguage.HINDI -> "सिनेमा"
        AppLanguage.TAMIL -> "சினிமா"
        AppLanguage.MALAYALAM -> "സിനിമ"
        AppLanguage.KANNADA -> "ಸಿನಿಮಾ"
        AppLanguage.MARATHI -> "सिनेमा"
        AppLanguage.BENGALI -> "সিনেমা"
        AppLanguage.GUJARATI -> "સિનેમા"
    }

    fun health(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "ఆరోగ్యం"
        AppLanguage.ENGLISH -> "Health"
        AppLanguage.HINDI -> "स्वास्थ्य"
        AppLanguage.TAMIL -> "சுகாதாரம்"
        AppLanguage.MALAYALAM -> "ആരോഗ്യം"
        AppLanguage.KANNADA -> "ಆರೋಗ್ಯ"
        AppLanguage.MARATHI -> "आरोग्य"
        AppLanguage.BENGALI -> "স্বাস্থ্য"
        AppLanguage.GUJARATI -> "આરોગ્ય"
    }

    fun ayurveda(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "ఆయుర్వేదం"
        AppLanguage.ENGLISH -> "Ayurveda"
        AppLanguage.HINDI -> "आयुर्वेद"
        AppLanguage.TAMIL -> "ஆயுர்வேதம்"
        AppLanguage.MALAYALAM -> "ആയുർവേദം"
        AppLanguage.KANNADA -> "ಆಯುರ್ವೇದ"
        AppLanguage.MARATHI -> "आयुर्वेद"
        AppLanguage.BENGALI -> "আয়ুর্বেদ"
        AppLanguage.GUJARATI -> "આયુર્વેદ"
    }

    fun tabHome(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "హోమ్"
        AppLanguage.ENGLISH -> "Home"
        AppLanguage.HINDI -> "होम"
        AppLanguage.TAMIL -> "முகப்பு"
        AppLanguage.MALAYALAM -> "ഹോം"
        AppLanguage.KANNADA -> "ಮುಖಪುಟ"
        AppLanguage.MARATHI -> "होम"
        AppLanguage.BENGALI -> "হোম"
        AppLanguage.GUJARATI -> "હોમ"
    }

    fun tabCategories(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "కేటగిరీలు"
        AppLanguage.ENGLISH -> "Categories"
        AppLanguage.HINDI -> "श्रेणियां"
        AppLanguage.TAMIL -> "வகைகள்"
        AppLanguage.MALAYALAM -> "വിഭാഗങ്ങൾ"
        AppLanguage.KANNADA -> "ವರ್ಗಗಳು"
        AppLanguage.MARATHI -> "वर्गवारी"
        AppLanguage.BENGALI -> "বিভাগ"
        AppLanguage.GUJARATI -> "કેટેગરીઓ"
    }

    fun tabNews(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "వార్తలు"
        AppLanguage.ENGLISH -> "News"
        AppLanguage.HINDI -> "समाचार"
        AppLanguage.TAMIL -> "செய்திகள்"
        AppLanguage.MALAYALAM -> "വാർത്തകൾ"
        AppLanguage.KANNADA -> "ಸುದ್ದಿ"
        AppLanguage.MARATHI -> "बातम्या"
        AppLanguage.BENGALI -> "সংবাদ"
        AppLanguage.GUJARATI -> "સમાચાર"
    }

    fun tabSaved(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "సేవ్ చేసినవి"
        AppLanguage.ENGLISH -> "Saved"
        AppLanguage.HINDI -> "सहेजे गए"
        AppLanguage.TAMIL -> "சேமிக்கப்பட்டவை"
        AppLanguage.MALAYALAM -> "സേവ് ചെയ്തവ"
        AppLanguage.KANNADA -> "ಉಳಿಸಿದವು"
        AppLanguage.MARATHI -> "जतन केलेले"
        AppLanguage.BENGALI -> "সংরক্ষিত"
        AppLanguage.GUJARATI -> "સાચવેલા"
    }

    fun tabMore(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "మరిన్ని"
        AppLanguage.ENGLISH -> "More"
        AppLanguage.HINDI -> "अधिक"
        AppLanguage.TAMIL -> "மேலும்"
        AppLanguage.MALAYALAM -> "കൂടുതൽ"
        AppLanguage.KANNADA -> "ಇನ್ನಷ್ಟು"
        AppLanguage.MARATHI -> "अधिक"
        AppLanguage.BENGALI -> "আরো"
        AppLanguage.GUJARATI -> "વધુ"
    }

    fun readFullArticle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "పూర్తి వార్త"
        AppLanguage.ENGLISH -> "Full Story"
        AppLanguage.HINDI -> "पूरी खबर"
        AppLanguage.TAMIL -> "முழு செய்தி"
        AppLanguage.MALAYALAM -> "പൂർണ്ണ വാർത്ത"
        AppLanguage.KANNADA -> "ಪೂರ್ಣ ಸುದ್ದಿ"
        AppLanguage.MARATHI -> "पूर्ण बातमी"
        AppLanguage.BENGALI -> "সম্পূর্ণ খবর"
        AppLanguage.GUJARATI -> "સંપૂર્ણ સમાચાર"
    }

    fun changeLanguage(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "భాష మార్చండి"
        AppLanguage.ENGLISH -> "Change Language"
        AppLanguage.HINDI -> "भाषा बदलें"
        AppLanguage.TAMIL -> "மொழியை மாற்றவும்"
        AppLanguage.MALAYALAM -> "ഭാഷ മാറ്റുക"
        AppLanguage.KANNADA -> "ಭಾಷೆ ಬದಲಿಸಿ"
        AppLanguage.MARATHI -> "भाषा बदला"
        AppLanguage.BENGALI -> "ভাষা পরিবর্তন করুন"
        AppLanguage.GUJARATI -> "ભાષા બદલો"
    }

    fun selectLanguage(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "భాషను ఎంచుకోండి"
        AppLanguage.ENGLISH -> "Select Language"
        AppLanguage.HINDI -> "भाषा चुनें"
        AppLanguage.TAMIL -> "மொழியைத் தேர்ந்தெடுக்கவும்"
        AppLanguage.MALAYALAM -> "ഭാഷ തിരഞ്ഞെടുക്കുക"
        AppLanguage.KANNADA -> "ಭಾಷೆ ಆಯ್ಕೆಮಾಡಿ"
        AppLanguage.MARATHI -> "भाषा निवडा"
        AppLanguage.BENGALI -> "ভাষা নির্বাচন করুন"
        AppLanguage.GUJARATI -> "ભાષા પસંદ કરો"
    }

    fun categoriesHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "విభాగాలు / Categories"
        AppLanguage.ENGLISH -> "Categories"
        AppLanguage.HINDI -> "श्रेणियां (Categories)"
        AppLanguage.TAMIL -> "வகைகள் (Categories)"
        AppLanguage.MALAYALAM -> "വിഭാഗങ്ങൾ (Categories)"
        AppLanguage.KANNADA -> "ವರ್ಗಗಳು (Categories)"
        AppLanguage.MARATHI -> "विभाग (Categories)"
        AppLanguage.BENGALI -> "বিভাগ (Categories)"
        AppLanguage.GUJARATI -> "શ્રેણીઓ (Categories)"
    }

    fun searchPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "శోధించండి / Search news..."
        AppLanguage.ENGLISH -> "Search news..."
        AppLanguage.HINDI -> "समाचार खोजें / Search..."
        AppLanguage.TAMIL -> "செய்திகளைத் தேடுங்கள்..."
        AppLanguage.MALAYALAM -> "വാർത്തകൾ തിരയുക..."
        AppLanguage.KANNADA -> "ಸುದ್ದಿ ಹುಡುಕಿ..."
        AppLanguage.MARATHI -> "बातम्या शोधा..."
        AppLanguage.BENGALI -> "সংবাদ অনুসন্ধান..."
        AppLanguage.GUJARATI -> "સમાચાર શોધો..."
    }

    fun connectHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "కనెక్ట్ అవ్వండి / Connect"
        AppLanguage.ENGLISH -> "Connect with us"
        AppLanguage.HINDI -> "हमसे जुड़ें / Connect"
        AppLanguage.TAMIL -> "இணையுங்கள் / Connect"
        AppLanguage.MALAYALAM -> "ബന്ധപ്പെടുക / Connect"
        AppLanguage.KANNADA -> "ಸಂಪರ್ಕಿಸಿ / Connect"
        AppLanguage.MARATHI -> "संपर्क साधा / Connect"
        AppLanguage.BENGALI -> "যুক্ত থাকুন / Connect"
        AppLanguage.GUJARATI -> "જોડાઓ / Connect"
    }

    fun darkMode(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "డార్క్ మోడ్ (Dark Mode)"
        AppLanguage.ENGLISH -> "Dark Mode"
        AppLanguage.HINDI -> "डार्क मोड (Dark Mode)"
        AppLanguage.TAMIL -> "டார்க் பயன்முறை (Dark Mode)"
        AppLanguage.MALAYALAM -> "ഡാർക്ക് മോഡ് (Dark Mode)"
        AppLanguage.KANNADA -> "ಡಾರ್ಕ್ ಮೋಡ್ (Dark Mode)"
        AppLanguage.MARATHI -> "डार्क मोड (Dark Mode)"
        AppLanguage.BENGALI -> "ডার্ক মোড (Dark Mode)"
        AppLanguage.GUJARATI -> "ડાર્ક મોડ (Dark Mode)"
    }

    fun logout(lang: AppLanguage): String = when (lang) {
        AppLanguage.TELUGU -> "లాగౌట్ (Logout)"
        AppLanguage.ENGLISH -> "Logout"
        AppLanguage.HINDI -> "लॉग आउट (Logout)"
        AppLanguage.TAMIL -> "வெளியேறு (Logout)"
        AppLanguage.MALAYALAM -> "ലോഗ് ഔട്ട് (Logout)"
        AppLanguage.KANNADA -> "ಲಾಗ್‌ಔಟ್ (Logout)"
        AppLanguage.MARATHI -> "लॉग आउट (Logout)"
        AppLanguage.BENGALI -> "লগ আউট (Logout)"
        AppLanguage.GUJARATI -> "લૉગ આઉટ (Logout)"
    }
}
