import Foundation

public struct NewsItem: Identifiable, Codable, Equatable {
    public let id: Int
    public var title: String
    public var summary: String
    public var imageUrl: String?
    public var link: String
    public var date: String
    public var timeAgo: String
    public var category: String
    public var contentHtml: String
    public var isVideo: Bool
    public var videoUrl: String?
    
    public init(
        id: Int,
        title: String,
        summary: String,
        imageUrl: String?,
        link: String = NaaTvBrand.site,
        date: String = "",
        timeAgo: String = "ఇప్పుడే",
        category: String = "తాజా వార్తలు",
        contentHtml: String = "",
        isVideo: Bool = false,
        videoUrl: String? = nil
    ) {
        self.id = id
        self.title = title
        self.summary = summary
        self.imageUrl = imageUrl
        self.link = link
        self.date = date
        self.timeAgo = timeAgo
        self.category = category
        self.contentHtml = contentHtml.isEmpty ? "<p>\(summary)</p>" : contentHtml
        self.isVideo = isVideo
        self.videoUrl = videoUrl
    }
}

public struct NewsCategory: Identifiable, Hashable {
    public var id: String { slug ?? "latest" }
    public let label: String
    public let slug: String?
    
    public init(label: String, slug: String?) {
        self.label = label
        self.slug = slug
    }
}

public struct Categories {
    public static let list: [NewsCategory] = [
        NewsCategory(label: "తాజా వార్తలు", slug: nil),             // Latest / All News (Replaced "మీకోసం")
        NewsCategory(label: "వార్తలు", slug: "news"),               // News
        NewsCategory(label: "క్రీడలు", slug: "sports"),             // Sports (Added)
        NewsCategory(label: "సినిమా", slug: "cinema"),              // Cinema
        NewsCategory(label: "రాజకీయాలు", slug: "politics"),         // Politics
        NewsCategory(label: "వ్యాపారం", slug: "business"),          // Business
        NewsCategory(label: "ఆరోగ్యం", slug: "health-fitness"),     // Health
        NewsCategory(label: "ఆయుర్వేదం", slug: "ayur_vedam")        // Ayurveda
    ]
}

// WordPress REST API DTOs
struct WpPost: Decodable {
    let id: Int
    let date: String?
    let link: String?
    let title: RenderedText?
    let excerpt: RenderedText?
    let content: RenderedText?
    let embedded: WpEmbedded?
    
    enum CodingKeys: String, CodingKey {
        case id, date, link, title, excerpt, content
        case embedded = "_embedded"
    }
}

struct RenderedText: Decodable {
    let rendered: String?
}

struct WpEmbedded: Decodable {
    let featuredMedia: [WpMedia]?
    
    enum CodingKeys: String, CodingKey {
        case featuredMedia = "wp:featuredmedia"
    }
}

struct WpMedia: Decodable {
    let sourceUrl: String?
    
    enum CodingKeys: String, CodingKey {
        case sourceUrl = "source_url"
    }
}
