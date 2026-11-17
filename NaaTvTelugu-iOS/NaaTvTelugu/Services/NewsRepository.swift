import Foundation

public class NewsRepository {
    public static let shared = NewsRepository()
    
    public func fetchLiveNews() async -> [NewsItem] {
        guard let url = URL(string: "https://naatvtelugu.com/wp-json/wp/v2/posts?_embed=true&per_page=25") else {
            return fallbackNews()
        }
        
        do {
            let (data, response) = try await URLSession.shared.data(from: url)
            guard let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 else {
                return fallbackNews()
            }
            
            let wpPosts = try JSONDecoder().decode([WpPost].self, from: data)
            let items: [NewsItem] = wpPosts.map { wp in
                let cleanTitle = stripHtml(wp.title?.rendered ?? "తాజా వార్త")
                let cleanSummary = stripHtml(wp.excerpt?.rendered ?? "")
                let image = wp.embedded?.featuredMedia?.first?.sourceUrl
                
                return NewsItem(
                    id: wp.id,
                    title: cleanTitle,
                    summary: cleanSummary.isEmpty ? cleanTitle : cleanSummary,
                    imageUrl: image,
                    link: wp.link ?? NaaTvBrand.site,
                    date: formatDate(wp.date),
                    timeAgo: calculateTimeAgo(wp.date),
                    category: "వార్తలు",
                    contentHtml: wp.content?.rendered ?? "<p>\(cleanSummary)</p>",
                    isVideo: false
                )
            }
            return items.isEmpty ? fallbackNews() : items
        } catch {
            print("API Error: \(error.localizedDescription), using fallback stories.")
            return fallbackNews()
        }
    }
    
    private func stripHtml(_ text: String) -> String {
        return text.replacingOccurrences(of: "<[^>]+>", with: "", options: .regularExpression, range: nil)
            .replacingOccurrences(of: "&#8211;", with: "–")
            .replacingOccurrences(of: "&#8217;", with: "'")
            .replacingOccurrences(of: "&#8220;", with: "\"")
            .replacingOccurrences(of: "&#8221;", with: "\"")
            .replacingOccurrences(of: "&amp;", with: "&")
            .trimmingCharacters(in: .whitespacesAndNewlines)
    }
    
    private func formatDate(_ dateStr: String?) -> String {
        guard let str = dateStr else { return "04 Oct" }
        return String(str.prefix(10))
    }
    
    private func calculateTimeAgo(_ dateStr: String?) -> String {
        return "10m ago"
    }
    
    public func fallbackNews() -> [NewsItem] {
        return [
            NewsItem(
                id: 101,
                title: "singeetham srinivasa rao దర్శకుడు సింగీతం శ్రీనివాసరావు కన్నుమూశారు",
                summary: "భారతీయ చిత్ర పరిశ్రమలో విభిన్న ప్రయోగాత్మక చిత్రాలతో ప్రేక్షకుల హృదయాల్లో చెరగని ముద్ర వేసిన ప్రముఖ దర్శకుడు సింగీతం శ్రీనివాసరావు (94) చెన్నైలోని నివాసంలో కన్నుమూశారు.",
                imageUrl: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=800",
                link: "https://naatvtelugu.com/singeetham-srinivasa-rao/",
                date: "04 Oct",
                timeAgo: "9m ago",
                category: "సినిమా",
                contentHtml: "<p>భారతీయ చిత్ర పరిశ్రమలో విభిన్న ప్రయోగాత్మక చిత్రాలతో ప్రేక్షకుల హృదయాల్లో చెరగని ముద్ర వేసిన ప్రముఖ దర్శకుడు సింగీతం శ్రీనివాసరావు (94) చెన్నైలోని నివాసంలో కన్నుమూశారు. ఆయన మృతి పట్ల సినీ ప్రముఖులు, అభిమానులు తీవ్ర సంతాపం వ్యక్తం చేస్తున్నారు.</p>",
                isVideo: false
            ),
            NewsItem(
                id: 102,
                title: "అల్లు అర్జున్ పాటను బీట్ చేసిన ఫోక్ సాంగ్",
                summary: "తెలంగాణ ఫోక్ సాంగ్ 'రాను బొంబాయికి రాను' సరికొత్త రికార్డు సృష్టించింది. ఇప్పటివరకు 956 మిలియన్లకు పైగా వ్యూస్ సాధించి యూట్యూబ్‌లో సంచలనం సృష్టిస్తోంది.",
                imageUrl: "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=800",
                link: "https://naatvtelugu.com/folk-song-record/",
                date: "03 Oct",
                timeAgo: "25m ago",
                category: "సినిమా",
                contentHtml: "<p>తెలంగాణ ఫోక్ సాంగ్ 'రాను బొంబాయికి రాను' సరికొత్త రికార్డు సృష్టించింది. ఇప్పటివరకు 956 మిలియన్లకు పైగా వ్యూస్ సాధించింది.</p>",
                isVideo: false
            ),
            NewsItem(
                id: 103,
                title: "కొప్పారాపు కవుల పురస్కార ప్రదానోత్సవం",
                summary: "బ్రహ్మశ్రీ సామవేదం షణ్ముఖశర్మ గారికి కొప్పారాపు కవుల జాతీయ పురస్కారం ఘనంగా ప్రదానం చేయబడింది.",
                imageUrl: "https://images.unsplash.com/photo-1511578314322-379afb476865?w=800",
                link: "https://naatvtelugu.com/kopparapu-kavula-award/",
                date: "10 Sep",
                timeAgo: "1h ago",
                category: "వార్తలు",
                contentHtml: "<p>బ్రహ్మశ్రీ సామవేదం షణ్ముఖశర్మ గారికి కొప్పారాపు కవుల జాతీయ పురస్కారం ఘనంగా ప్రదానం చేయబడింది.</p>",
                isVideo: true,
                videoUrl: "https://naatvtelugu.com/sample_reel.mp4"
            ),
            NewsItem(
                id: 104,
                title: "Chiranjeevi Tweet On Irumudi: మెగాస్టార్ చిరంజీవి భావోద్వేగ సందేశం",
                summary: "మెగాస్టార్ చిరంజీవి తాజా చిత్రం ఇరుముడి విజయంపై ట్వీట్ చేస్తూ చిత్ర యూనిట్‌ను మనస్ఫూర్తిగా అభినందించారు.",
                imageUrl: "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800",
                link: "https://naatvtelugu.com/chiranjeevi-tweet/",
                date: "27 Aug",
                timeAgo: "3h ago",
                category: "సినిమా",
                contentHtml: "<p>మెగాస్టార్ చిరంజీవి తాజా చిత్రం ఇరుముడి విజయంపై ట్వీట్ చేస్తూ చిత్ర బృందాన్ని అభినందించారు.</p>",
                isVideo: true,
                videoUrl: "https://naatvtelugu.com/sample_reel2.mp4"
            ),
            NewsItem(
                id: 105,
                title: "పొన్నమండ గ్రామంలో డ్రైనేజీ సమస్య పరిష్కారానికి చర్యలు",
                summary: "కోనసీమ జిల్లా పొన్నమండలో నిలిచిపోయిన వరద నీటి సమస్యపై అధికారులు స్పందించి యుద్ధప్రాతిపదికన పనులు చేపట్టారు.",
                imageUrl: "https://images.unsplash.com/photo-1544717305-2782549b5136?w=800",
                link: "https://naatvtelugu.com/ponnamanda-drainage/",
                date: "25 Aug",
                timeAgo: "5h ago",
                category: "వార్తలు",
                contentHtml: "<p>కోనసీమ జిల్లా పొన్నమండలో నిలిచిపోయిన వరద నీటి సమస్యపై అధికారులు చర్యలు ప్రారంభించారు.</p>",
                isVideo: false
            ),
            NewsItem(
                id: 106,
                title: "భారత క్రికెట్ జట్టు సంచలన విజయం.. సిరీస్ కైవసం",
                summary: "చివరి ఓవర్ వరకు ఉత్కంఠభరితంగా సాగిన మ్యాచ్‌లో భారత జట్టు 4 వికెట్ల తేడాతో ఘన విజయం సాధించి ట్రోఫీ గెలుచుకుంది.",
                imageUrl: "https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?w=800",
                link: "https://naatvtelugu.com/cricket-series-win/",
                date: "20 Aug",
                timeAgo: "6h ago",
                category: "క్రీడలు",
                contentHtml: "<p>చివరి ఓవర్ వరకు ఉత్కంఠభరితంగా సాగిన మ్యాచ్‌లో భారత జట్టు అద్భుత విజయం సాధించింది.</p>",
                isVideo: false
            )
        ]
    }
}
