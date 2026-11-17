import Foundation

public enum PostStatus: String, Codable, CaseIterable {
    case approved = "APPROVED"
    case pending = "PENDING"
    case rejected = "REJECTED"
    
    public var label: String {
        switch self {
        case .approved: return "ఆమోదించబడింది (Approved)"
        case .pending: return "పరిశీలనలో ఉంది (Pending)"
        case .rejected: return "తిరస్కరించబడింది (Rejected)"
        }
    }
}

public struct CustomPost: Identifiable, Codable, Equatable {
    public let id: Int
    public var title: String
    public var summary: String
    public var imageUrl: String?
    public var link: String
    public var date: String
    public var timeAgo: String
    public var category: String
    public var contentHtml: String
    public var authorName: String
    public var authorEmail: String
    public var authorRole: UserRole
    public var status: PostStatus
    
    public init(
        id: Int = Int(Date().timeIntervalSince1970) % 100000000,
        title: String,
        summary: String,
        imageUrl: String?,
        link: String = NaaTvBrand.site,
        date: String = "",
        timeAgo: String = "ఇప్పుడే",
        category: String = "తాజా వార్తలు",
        contentHtml: String = "",
        authorName: String,
        authorEmail: String,
        authorRole: UserRole,
        status: PostStatus = .pending
    ) {
        self.id = id
        self.title = title
        self.summary = summary
        self.imageUrl = imageUrl
        self.link = link
        let formatter = DateFormatter()
        formatter.dateFormat = "dd MMM yyyy, hh:mm a"
        formatter.locale = Locale(identifier: "en_US")
        self.date = date.isEmpty ? formatter.string(from: Date()) : date
        self.timeAgo = timeAgo
        self.category = category
        self.contentHtml = contentHtml.isEmpty ? "<p>\(summary)</p>" : contentHtml
        self.authorName = authorName
        self.authorEmail = authorEmail
        self.authorRole = authorRole
        self.status = status
    }
    
    public func toNewsItem() -> NewsItem {
        NewsItem(
            id: id,
            title: title,
            summary: summary,
            imageUrl: imageUrl,
            link: link,
            date: date,
            timeAgo: timeAgo,
            category: category,
            contentHtml: contentHtml
        )
    }
}
