import SwiftUI

public struct HomeExploreView: View {
    @ObservedObject var viewModel: NewsViewModel
    
    // Callbacks to parent for navigation / sheet presentation
    var onOpenMore: () -> Void
    var onOpenNews: () -> Void
    var onOpenCategories: () -> Void
    var onOpenReels: () -> Void
    var onOpenLocationPicker: () -> Void
    var onOpenCreatePost: () -> Void
    var onSelectStory: (NewsItem) -> Void
    
    @State private var carouselPage: Int = 0
    
    public init(
        viewModel: NewsViewModel,
        onOpenMore: @escaping () -> Void,
        onOpenNews: @escaping () -> Void,
        onOpenCategories: @escaping () -> Void,
        onOpenReels: @escaping () -> Void,
        onOpenLocationPicker: @escaping () -> Void,
        onOpenCreatePost: @escaping () -> Void,
        onSelectStory: @escaping (NewsItem) -> Void
    ) {
        self.viewModel = viewModel
        self.onOpenMore = onOpenMore
        self.onOpenNews = onOpenNews
        self.onOpenCategories = onOpenCategories
        self.onOpenReels = onOpenReels
        self.onOpenLocationPicker = onOpenLocationPicker
        self.onOpenCreatePost = onOpenCreatePost
        self.onSelectStory = onSelectStory
    }
    
    public var body: some View {
        ZStack {
            Theme.darkBackground.ignoresSafeArea()
            
            VStack(spacing: 0) {
                // Top Navigation Bar (Avatar, Logo, Plus button)
                topNavBar
                
                // Location Bar
                locationBar
                
                ScrollView(.vertical, showsIndicators: false) {
                    VStack(spacing: 20) {
                        // Horizontal Card Carousel (1 న్యూస్ of 7, 2 పొన్నమండ of 7)
                        cardsCarousel
                        
                        // Carousel Page Indicator Dots
                        carouselDots
                        
                        // Section Header: ── NEWS REELS ──
                        newsReelsHeader
                        
                        // Horizontal News Reels Row
                        newsReelsRow
                        
                        // Additional Trending / Latest Stories section
                        latestStoriesSection
                        
                        Spacer(minLength: 90) // Safe padding for bottom bar
                    }
                    .padding(.top, 8)
                }
            }
        }
    }
    
    // MARK: - Top Nav Bar
    private var topNavBar: some View {
        HStack(alignment: .center, spacing: 12) {
            // Profile / User Avatar Button
            Button(action: onOpenMore) {
                ZStack {
                    Circle()
                        .stroke(Theme.goldColor.opacity(0.6), lineWidth: 1.5)
                        .frame(width: 38, height: 38)
                    
                    if let user = viewModel.authStore.currentUser, !user.isGuest {
                        Text(String(user.name.prefix(1)).uppercased())
                            .font(.system(size: 16, weight: .bold))
                            .foregroundColor(Theme.goldColor)
                    } else {
                        Image(systemName: "person.crop.circle")
                            .font(.system(size: 24))
                            .foregroundColor(.white.opacity(0.85))
                    }
                }
            }
            
            Spacer()
            
            // NAA TV Telugu Brand Logo
            VStack(spacing: 2) {
                Image("naa_tv_logo")
                    .resizable()
                    .scaledToFit()
                    .frame(height: 32)
                
                Text("#IndiaReadsNaaTv")
                    .font(.system(size: 9, weight: .semibold))
                    .foregroundColor(Theme.goldColor.opacity(0.85))
            }
            
            Spacer()
            
            // Plus Button (Submit news / Reporter desk)
            Button(action: onOpenCreatePost) {
                ZStack {
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(Color.white.opacity(0.3), lineWidth: 1.2)
                        .frame(width: 36, height: 36)
                    
                    Image(systemName: "plus")
                        .font(.system(size: 16, weight: .semibold))
                        .foregroundColor(.white)
                }
            }
        }
        .padding(.horizontal, 16)
        .padding(.top, 8)
        .padding(.bottom, 6)
        .background(Theme.navyPrimary)
    }
    
    // MARK: - Location Bar
    private var locationBar: some View {
        Button(action: onOpenLocationPicker) {
            HStack(spacing: 6) {
                Image(systemName: "location.fill")
                    .font(.system(size: 12))
                    .foregroundColor(Theme.goldColor)
                
                Text(viewModel.locationManager.userLocation)
                    .font(.system(size: 13, weight: .medium))
                    .foregroundColor(.white)
                    .lineLimit(1)
                
                Text("(మార్చండి)")
                    .font(.system(size: 11, weight: .medium))
                    .foregroundColor(Theme.goldColor.opacity(0.9))
                
                Spacer()
                
                if viewModel.locationManager.isDetecting {
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle(tint: Theme.goldColor))
                        .scaleEffect(0.7)
                } else {
                    Image(systemName: "chevron.right")
                        .font(.system(size: 10, weight: .semibold))
                        .foregroundColor(.white.opacity(0.5))
                }
            }
            .padding(.horizontal, 14)
            .padding(.vertical, 7)
            .background(Color.white.opacity(0.06))
            .cornerRadius(8)
            .padding(.horizontal, 16)
            .padding(.vertical, 4)
        }
    }
    
    // MARK: - Carousel Cards
    private var cardsCarousel: some View {
        let stories = viewModel.filteredItems.prefix(7)
        let count = max(stories.count, 2)
        
        return TabView(selection: $carouselPage) {
            // Card 1: Main Breaking News
            if let first = stories.first {
                carouselCardView(
                    indexNumber: 1,
                    totalCount: count,
                    titleBadge: "న్యూస్",
                    headline: first.title,
                    locationBadge: "Breaking News",
                    actionLabel: "న్యూస్ కొరకు... >",
                    imageUrl: first.imageUrl,
                    item: first
                )
                .tag(0)
            } else {
                carouselCardView(
                    indexNumber: 1,
                    totalCount: 7,
                    titleBadge: "న్యూస్",
                    headline: "తాజా తెలుగు బ్రేకింగ్ న్యూస్ అప్‌డేట్స్",
                    locationBadge: "Breaking News",
                    actionLabel: "న్యూస్ కొరకు... >",
                    imageUrl: nil,
                    item: nil
                )
                .tag(0)
            }
            
            // Card 2: Local Ponnманда / District News
            let secondItem = stories.count > 1 ? stories[1] : stories.first
            carouselCardView(
                indexNumber: 2,
                totalCount: count,
                titleBadge: viewModel.locationManager.userLocation.split(separator: ",").first.map(String.init) ?? "పొన్నమండ",
                headline: secondItem?.title ?? "మీ ప్రాంత స్థానిక వార్తలు మరియు ముఖ్యాంశాలు",
                locationBadge: "Local News",
                actionLabel: "మీ ఊరి వార్తలకోసం... >",
                imageUrl: secondItem?.imageUrl,
                item: secondItem
            )
            .tag(1)
            
            // Additional cards from top stories
            ForEach(Array(stories.dropFirst(2).enumerated()), id: \.offset) { offset, item in
                carouselCardView(
                    indexNumber: offset + 3,
                    totalCount: count,
                    titleBadge: item.category,
                    headline: item.title,
                    locationBadge: item.category,
                    actionLabel: "పూర్తి కథనం కోసం... >",
                    imageUrl: item.imageUrl,
                    item: item
                )
                .tag(offset + 2)
            }
        }
        .frame(height: 380)
        .tabViewStyle(PageTabViewStyle(indexDisplayMode: .never))
    }
    
    private func carouselCardView(
        indexNumber: Int,
        totalCount: Int,
        titleBadge: String,
        headline: String,
        locationBadge: String,
        actionLabel: String,
        imageUrl: String?,
        item: NewsItem?
    ) -> some View {
        Button(action: {
            if let item = item {
                onSelectStory(item)
            } else {
                onOpenNews()
            }
        }) {
            ZStack(alignment: .bottomLeading) {
                // Background image or gradient
                if let urlStr = imageUrl, let url = URL(string: urlStr) {
                    AsyncImage(url: url) { phase in
                        switch phase {
                        case .success(let image):
                            image
                                .resizable()
                                .scaledToFill()
                        default:
                            LinearGradient(
                                colors: [Theme.navyPrimary, Theme.navyCard],
                                startPoint: .topLeading,
                                endPoint: .bottomTrailing
                            )
                        }
                    }
                } else {
                    LinearGradient(
                        colors: [Color(hex: "132F4C"), Theme.navyPrimary],
                        startPoint: .topLeading,
                        endPoint: .bottomTrailing
                    )
                }
                
                // Dark overlay gradient for contrast
                LinearGradient(
                    gradient: Gradient(colors: [
                        Color.black.opacity(0.3),
                        Color.black.opacity(0.85)
                    ]),
                    startPoint: .center,
                    endPoint: .bottom
                )
                
                // Card Header: Gold Large Number + Title Badge ("1 న్యూస్ of 7")
                VStack(alignment: .leading, spacing: 0) {
                    HStack(alignment: .firstTextBaseline, spacing: 8) {
                        Text("\(indexNumber)")
                            .font(.system(size: 46, weight: .heavy))
                            .foregroundColor(Theme.goldColor)
                        
                        VStack(alignment: .leading, spacing: 2) {
                            Text(titleBadge)
                                .font(.system(size: 18, weight: .bold))
                                .foregroundColor(.white)
                            
                            Text("of \(totalCount)")
                                .font(.system(size: 12, weight: .medium))
                                .foregroundColor(.white.opacity(0.7))
                        }
                        
                        Spacer()
                        
                        // Chevron or close dot
                        Circle()
                            .fill(Color.white.opacity(0.2))
                            .frame(width: 28, height: 28)
                            .overlay(
                                Image(systemName: "chevron.right")
                                    .font(.system(size: 11, weight: .bold))
                                    .foregroundColor(.white)
                            )
                    }
                    .padding(16)
                    
                    Spacer()
                    
                    // Card Bottom Content
                    VStack(alignment: .leading, spacing: 10) {
                        // Badge (Breaking News or Local News)
                        HStack(spacing: 6) {
                            if locationBadge == "Local News" {
                                Image(systemName: "mappin.circle.fill")
                                    .foregroundColor(Theme.goldColor)
                            }
                            Text(locationBadge)
                                .font(.system(size: 11, weight: .bold))
                                .foregroundColor(locationBadge == "Breaking News" ? .white : Theme.goldColor)
                        }
                        .padding(.horizontal, 10)
                        .padding(.vertical, 4)
                        .background(locationBadge == "Breaking News" ? Color.red.opacity(0.85) : Color.black.opacity(0.6))
                        .cornerRadius(6)
                        
                        // Headline
                        Text(headline)
                            .font(.system(size: 16, weight: .bold))
                            .foregroundColor(.white)
                            .lineLimit(3)
                            .lineSpacing(4)
                        
                        // Action link (న్యూస్ కొరకు... >)
                        HStack(spacing: 4) {
                            Text(actionLabel)
                                .font(.system(size: 14, weight: .bold))
                                .foregroundColor(Theme.goldColor)
                        }
                    }
                    .padding(18)
                }
            }
            .frame(maxWidth: .infinity)
            .cornerRadius(18)
            .overlay(
                RoundedRectangle(cornerRadius: 18)
                    .stroke(Color.white.opacity(0.12), lineWidth: 1)
            )
            .padding(.horizontal, 16)
            .shadow(color: Color.black.opacity(0.4), radius: 8, x: 0, y: 4)
        }
        .buttonStyle(PlainButtonStyle())
    }
    
    // MARK: - Carousel Dots
    private var carouselDots: some View {
        HStack(spacing: 6) {
            ForEach(0..<min(viewModel.filteredItems.count, 7), id: \.self) { idx in
                Capsule()
                    .fill(idx == carouselPage ? Theme.goldColor : Color.white.opacity(0.3))
                    .frame(width: idx == carouselPage ? 20 : 6, height: 6)
                    .animation(.spring(), value: carouselPage)
            }
        }
    }
    
    // MARK: - Reels Header
    private var newsReelsHeader: some View {
        HStack(spacing: 12) {
            Rectangle()
                .fill(Color.white.opacity(0.2))
                .frame(height: 1)
            
            Text("NEWS REELS")
                .font(.system(size: 13, weight: .heavy))
                .tracking(1.5)
                .foregroundColor(.white)
            
            Rectangle()
                .fill(Color.white.opacity(0.2))
                .frame(height: 1)
        }
        .padding(.horizontal, 24)
        .padding(.top, 8)
    }
    
    // MARK: - Horizontal News Reels Row
    private var newsReelsRow: some View {
        let reels = viewModel.reelsItems.isEmpty ? Array(viewModel.filteredItems.prefix(6)) : viewModel.reelsItems
        
        return ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 12) {
                ForEach(reels) { reel in
                    Button(action: {
                        onSelectStory(reel)
                    }) {
                        VStack(alignment: .leading, spacing: 6) {
                            ZStack(alignment: .topTrailing) {
                                // Thumbnail
                                if let urlStr = reel.imageUrl, let url = URL(string: urlStr) {
                                    AsyncImage(url: url) { phase in
                                        switch phase {
                                        case .success(let img):
                                            img
                                                .resizable()
                                                .scaledToFill()
                                        default:
                                            Color.gray.opacity(0.3)
                                        }
                                    }
                                    .frame(width: 140, height: 210)
                                    .clipped()
                                } else {
                                    Color(hex: "1E293B")
                                        .frame(width: 140, height: 210)
                                }
                                
                                // Dark bottom gradient for text readability
                                LinearGradient(
                                    colors: [Color.clear, Color.black.opacity(0.85)],
                                    startPoint: .top,
                                    endPoint: .bottom
                                )
                                
                                // Play icon overlay in center
                                Circle()
                                    .fill(Color.black.opacity(0.6))
                                    .frame(width: 36, height: 36)
                                    .overlay(
                                        Image(systemName: "play.fill")
                                            .font(.system(size: 14))
                                            .foregroundColor(.white)
                                    )
                                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
                                
                                // Top right video badge
                                Image(systemName: "video.fill")
                                    .font(.system(size: 11))
                                    .foregroundColor(Theme.goldColor)
                                    .padding(6)
                                    .background(Color.black.opacity(0.6))
                                    .clipShape(Circle())
                                    .padding(8)
                                
                                // Bottom time stamp
                                HStack(spacing: 4) {
                                    Image(systemName: "clock")
                                        .font(.system(size: 9))
                                    Text(reel.publishedDate)
                                        .font(.system(size: 10, weight: .medium))
                                }
                                .foregroundColor(.white.opacity(0.85))
                                .padding(6)
                                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .bottomLeading)
                            }
                            .frame(width: 140, height: 210)
                            .cornerRadius(12)
                            .overlay(
                                RoundedRectangle(cornerRadius: 12)
                                    .stroke(Color.white.opacity(0.15), lineWidth: 1)
                            )
                            
                            // Reel Headline
                            Text(reel.title)
                                .font(.system(size: 12, weight: .semibold))
                                .foregroundColor(.white)
                                .lineLimit(2)
                                .frame(width: 140, alignment: .leading)
                        }
                    }
                    .buttonStyle(PlainButtonStyle())
                }
            }
            .padding(.horizontal, 16)
        }
    }
    
    // MARK: - Latest Stories Section
    private var latestStoriesSection: some View {
        VStack(alignment: .leading, spacing: 12) {
            HStack {
                Text("తాజా ముఖ్యాంశాలు")
                    .font(.system(size: 16, weight: .bold))
                    .foregroundColor(.white)
                
                Spacer()
                
                Button(action: onOpenNews) {
                    Text("మరిన్ని చూడండి >")
                        .font(.system(size: 12, weight: .semibold))
                        .foregroundColor(Theme.goldColor)
                }
            }
            .padding(.horizontal, 16)
            
            ForEach(viewModel.filteredItems.prefix(4)) { item in
                Button(action: {
                    onSelectStory(item)
                }) {
                    HStack(spacing: 12) {
                        // Thumbnail
                        if let urlStr = item.imageUrl, let url = URL(string: urlStr) {
                            AsyncImage(url: url) { phase in
                                switch phase {
                                case .success(let img):
                                    img
                                        .resizable()
                                        .scaledToFill()
                                default:
                                    Color.gray.opacity(0.2)
                                }
                            }
                            .frame(width: 80, height: 70)
                            .cornerRadius(8)
                            .clipped()
                        } else {
                            RoundedRectangle(cornerRadius: 8)
                                .fill(Color.white.opacity(0.08))
                                .frame(width: 80, height: 70)
                                .overlay(
                                    Image(systemName: "newspaper")
                                        .foregroundColor(.white.opacity(0.4))
                                )
                        }
                        
                        VStack(alignment: .leading, spacing: 4) {
                            Text(item.title)
                                .font(.system(size: 13, weight: .bold))
                                .foregroundColor(.white)
                                .lineLimit(2)
                            
                            HStack(spacing: 6) {
                                Text(item.category)
                                    .font(.system(size: 10, weight: .semibold))
                                    .foregroundColor(Theme.goldColor)
                                
                                Text("•")
                                    .font(.system(size: 10))
                                    .foregroundColor(.white.opacity(0.4))
                                
                                Text(item.publishedDate)
                                    .font(.system(size: 10))
                                    .foregroundColor(.white.opacity(0.6))
                            }
                        }
                        
                        Spacer()
                    }
                    .padding(10)
                    .background(Theme.navyCard)
                    .cornerRadius(12)
                    .padding(.horizontal, 16)
                }
                .buttonStyle(PlainButtonStyle())
            }
        }
    }
}
