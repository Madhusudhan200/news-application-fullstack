import SwiftUI

public struct SavedStoriesView: View {
    @ObservedObject var viewModel: NewsViewModel
    var onDismiss: () -> Void
    var onSelectStory: (NewsItem) -> Void
    
    public init(viewModel: NewsViewModel, onDismiss: @escaping () -> Void, onSelectStory: @escaping (NewsItem) -> Void) {
        self.viewModel = viewModel
        self.onDismiss = onDismiss
        self.onSelectStory = onSelectStory
    }
    
    public var body: some View {
        NavigationView {
            ZStack {
                Theme.darkBackground.ignoresSafeArea()
                
                VStack(spacing: 0) {
                    // Header
                    HStack {
                        Button(action: onDismiss) {
                            Image(systemName: "chevron.left")
                                .font(.system(size: 16, weight: .bold))
                                .foregroundColor(Theme.goldColor)
                        }
                        
                        Text("భద్రపరిచిన కథనాలు (Saved Stories)")
                            .font(.system(size: 16, weight: .bold))
                            .foregroundColor(.white)
                            .padding(.leading, 8)
                        
                        Spacer()
                        
                        Text("\(viewModel.newsStore.savedStories.count)")
                            .font(.system(size: 12, weight: .bold))
                            .foregroundColor(.black)
                            .padding(.horizontal, 8)
                            .padding(.vertical, 3)
                            .background(Theme.goldColor)
                            .cornerRadius(10)
                    }
                    .padding(.horizontal, 16)
                    .padding(.vertical, 12)
                    .background(Theme.navyPrimary)
                    
                    if viewModel.newsStore.savedStories.isEmpty {
                        VStack(spacing: 12) {
                            Image(systemName: "bookmark.slash")
                                .font(.system(size: 48))
                                .foregroundColor(.white.opacity(0.3))
                            
                            Text("భద్రపరిచిన వార్తలు ఏవీ లేవు")
                                .font(.system(size: 16, weight: .bold))
                                .foregroundColor(.white)
                            
                            Text("మీరు ఆసక్తికర కథనాలను బుక్‌మార్క్ చేసినప్పుడు అవి ఇక్కడ కనిపిస్తాయి.")
                                .font(.system(size: 13))
                                .foregroundColor(.white.opacity(0.6))
                                .multilineTextAlignment(.center)
                        }
                        .padding(.top, 80)
                        .padding(.horizontal, 32)
                        Spacer()
                    } else {
                        ScrollView {
                            VStack(spacing: 12) {
                                ForEach(viewModel.newsStore.savedStories) { item in
                                    HStack(spacing: 12) {
                                        Button(action: {
                                            onSelectStory(item)
                                        }) {
                                            HStack(spacing: 12) {
                                                if let urlStr = item.imageUrl, let url = URL(string: urlStr) {
                                                    AsyncImage(url: url) { phase in
                                                        switch phase {
                                                        case .success(let img):
                                                            img.resizable().scaledToFill()
                                                        default:
                                                            Color.gray.opacity(0.3)
                                                        }
                                                    }
                                                    .frame(width: 85, height: 75)
                                                    .cornerRadius(8)
                                                    .clipped()
                                                }
                                                
                                                VStack(alignment: .leading, spacing: 4) {
                                                    Text(item.title)
                                                        .font(.system(size: 13, weight: .bold))
                                                        .foregroundColor(.white)
                                                        .lineLimit(2)
                                                    
                                                    HStack(spacing: 6) {
                                                        Text(item.category)
                                                            .font(.system(size: 11, weight: .semibold))
                                                            .foregroundColor(Theme.goldColor)
                                                        
                                                        Text("•")
                                                            .foregroundColor(.white.opacity(0.4))
                                                        
                                                        Text(item.publishedDate)
                                                            .font(.system(size: 10))
                                                            .foregroundColor(.white.opacity(0.6))
                                                    }
                                                }
                                                
                                                Spacer()
                                            }
                                        }
                                        .buttonStyle(PlainButtonStyle())
                                        
                                        // Remove Bookmark Button
                                        Button(action: {
                                            viewModel.newsStore.toggleBookmark(item)
                                        }) {
                                            Image(systemName: "trash")
                                                .font(.system(size: 14))
                                                .foregroundColor(.red.opacity(0.8))
                                                .padding(8)
                                        }
                                    }
                                    .padding(12)
                                    .background(Theme.navyCard)
                                    .cornerRadius(12)
                                    .padding(.horizontal, 16)
                                }
                            }
                            .padding(.vertical, 12)
                        }
                    }
                }
            }
            .navigationBarHidden(true)
        }
    }
}
