import SwiftUI

public struct EditedStory: Identifiable, Codable, Equatable {
    public let id: Int
    public var title: String
    public var summary: String
    public var category: String
    public var contentHtml: String
    
    public init(id: Int, title: String, summary: String, category: String, contentHtml: String = "") {
        self.id = id
        self.title = title
        self.summary = summary
        self.category = category
        self.contentHtml = contentHtml.isEmpty ? "<p>\(summary)</p>" : contentHtml
    }
}

public struct AdminSettings: Codable, Equatable {
    public var headlineFontSize: Double
    public var bodyFontSize: Double
    public var fontFamily: String
    public var logoHeight: Double
    public var imageAspectRatio: Double
    public var allowGuestBookmarks: Bool
    public var allowGuestComments: Bool
    public var requireGpsForPosting: Bool
    public var directPublishWithoutApproval: Bool
    
    // Compatibility aliases
    public var fontFamilyChoice: String {
        get { fontFamily }
        set { fontFamily = newValue }
    }
    public var logoHeightDp: Double {
        get { logoHeight }
        set { logoHeight = newValue }
    }
    public var imageRatioPercent: Double {
        get { imageAspectRatio }
        set { imageAspectRatio = newValue }
    }
    public var guestModeEnabled: Bool = true
    public var guestCanBookmark: Bool {
        get { allowGuestBookmarks }
        set { allowGuestBookmarks = newValue }
    }
    public var guestCanShare: Bool = true
    public var employeeRequireApproval: Bool {
        get { !directPublishWithoutApproval }
        set { directPublishWithoutApproval = !newValue }
    }
    public var employeeAllowBreaking: Bool = true
    public var customLogoUrl: String = ""
    
    public init(
        headlineFontSize: Double = 19.0,
        bodyFontSize: Double = 15.0,
        fontFamily: String = "Default",
        logoHeight: Double = 36.0,
        imageAspectRatio: Double = 1.77,
        allowGuestBookmarks: Bool = true,
        allowGuestComments: Bool = false,
        requireGpsForPosting: Bool = true,
        directPublishWithoutApproval: Bool = false
    ) {
        self.headlineFontSize = headlineFontSize
        self.bodyFontSize = bodyFontSize
        self.fontFamily = fontFamily
        self.logoHeight = logoHeight
        self.imageAspectRatio = imageAspectRatio
        self.allowGuestBookmarks = allowGuestBookmarks
        self.allowGuestComments = allowGuestComments
        self.requireGpsForPosting = requireGpsForPosting
        self.directPublishWithoutApproval = directPublishWithoutApproval
    }
    
    public var fontDesign: Font.Design {
        switch fontFamily {
        case "Serif": return .serif
        case "Monospace": return .monospaced
        case "Rounded": return .rounded
        default: return .default
        }
    }
}
