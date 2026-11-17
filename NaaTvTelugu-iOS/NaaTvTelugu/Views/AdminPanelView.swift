import SwiftUI

public struct AdminPanelView: View {
    @ObservedObject var viewModel: NewsViewModel
    var onDismiss: () -> Void
    
    @State private var selectedTab: Int = 0
    
    // Tab 1: Post News State
    @State private var newTitle: String = ""
    @State private var newCategory: String = "తాజా వార్తలు"
    @State private var newSummary: String = ""
    @State private var newHtml: String = ""
    @State private var newImageUrl: String = ""
    @State private var newVideoUrl: String = ""
    @State private var isVideo: Bool = false
    @State private var postSuccessMessage: String?
    
    // Tab 2: Edit Stories State
    @State private var selectedStoryToEdit: NewsItem?
    @State private var editTitle: String = ""
    @State private var editSummary: String = ""
    @State private var editCategory: String = ""
    @State private var editHtml: String = ""
    @State private var editSuccessMessage: String?
    
    // Tab 4: Directory Filter State
    @State private var directoryRoleFilter: String = "All"
    
    private let tabs = [
        "ఆమోదాలు",
        "పోస్ట్ న్యూస్",
        "సవరించండి",
        "కస్టమైజేషన్",
        "సిబ్బంది & యూజర్లు"
    ]
    
    public init(viewModel: NewsViewModel, onDismiss: @escaping () -> Void) {
        self.viewModel = viewModel
        self.onDismiss = onDismiss
    }
    
    public var body: some View {
        NavigationView {
            ZStack {
                Theme.darkBackground.ignoresSafeArea()
                
                VStack(spacing: 0) {
                    // Custom Navigation Header
                    adminHeader
                    
                    // Horizontal Tab Selector
                    tabSelector
                    
                    // Tab Content
                    TabView(selection: $selectedTab) {
                        approvalsTab.tag(0)
                        postNewsTab.tag(1)
                        editNewsTab.tag(2)
                        customizationTab.tag(3)
                        directoryTab.tag(4)
                    }
                    .tabViewStyle(PageTabViewStyle(indexDisplayMode: .never))
                }
            }
            .navigationBarHidden(true)
        }
    }
    
    // MARK: - Admin Header
    private var adminHeader: some View {
        HStack(spacing: 12) {
            Button(action: onDismiss) {
                Image(systemName: "chevron.left")
                    .font(.system(size: 18, weight: .bold))
                    .foregroundColor(Theme.goldColor)
            }
            
            VStack(alignment: .leading, spacing: 2) {
                Text("అడ్మిన్ నియంత్రణ ప్యానెల్")
                    .font(.system(size: 17, weight: .bold))
                    .foregroundColor(.white)
                
                Text("NAA TV Telugu Master Control")
                    .font(.system(size: 11, weight: .medium))
                    .foregroundColor(Theme.goldColor.opacity(0.85))
            }
            
            Spacer()
            
            // Badge
            Text("ADMIN")
                .font(.system(size: 10, weight: .heavy))
                .foregroundColor(.black)
                .padding(.horizontal, 8)
                .padding(.vertical, 3)
                .background(Theme.goldColor)
                .cornerRadius(4)
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
        .background(Theme.navyPrimary)
    }
    
    // MARK: - Tab Selector
    private var tabSelector: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 8) {
                ForEach(0..<tabs.count, id: \.self) { idx in
                    Button(action: { selectedTab = idx }) {
                        Text(tabs[idx])
                            .font(.system(size: 13, weight: selectedTab == idx ? .bold : .medium))
                            .foregroundColor(selectedTab == idx ? .black : .white)
                            .padding(.horizontal, 14)
                            .padding(.vertical, 8)
                            .background(selectedTab == idx ? Theme.goldColor : Theme.navyCard)
                            .cornerRadius(20)
                    }
                }
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 10)
        }
        .background(Theme.navyPrimary.opacity(0.85))
    }
    
    // MARK: - Tab 0: Approvals
    private var approvalsTab: some View {
        let pending = viewModel.newsStore.customPosts.filter { $0.status == .pending }
        
        return ScrollView {
            VStack(spacing: 14) {
                if pending.isEmpty {
                    VStack(spacing: 12) {
                        Image(systemName: "checkmark.seal.fill")
                            .font(.system(size: 48))
                            .foregroundColor(Theme.goldColor)
                        
                        Text("పెండింగ్ కథనాలు ఏవీ లేవు")
                            .font(.system(size: 16, weight: .bold))
                            .foregroundColor(.white)
                        
                        Text("సిబ్బంది లేదా రిపోర్టర్లు పంపిన వార్తలన్నీ సమీక్షించబడ్డాయి.")
                            .font(.system(size: 13))
                            .foregroundColor(.white.opacity(0.6))
                            .multilineTextAlignment(.center)
                    }
                    .padding(.top, 60)
                    .padding(.horizontal, 24)
                } else {
                    ForEach(pending) { post in
                        VStack(alignment: .leading, spacing: 10) {
                            HStack {
                                Text(post.category)
                                    .font(.system(size: 11, weight: .bold))
                                    .foregroundColor(Theme.goldColor)
                                    .padding(.horizontal, 8)
                                    .padding(.vertical, 3)
                                    .background(Theme.navyPrimary)
                                    .cornerRadius(4)
                                
                                Spacer()
                                
                                Text(post.authorName)
                                    .font(.system(size: 11, weight: .semibold))
                                    .foregroundColor(.white.opacity(0.8))
                            }
                            
                            Text(post.title)
                                .font(.system(size: 15, weight: .bold))
                                .foregroundColor(.white)
                            
                            Text(post.summary)
                                .font(.system(size: 13))
                                .foregroundColor(.white.opacity(0.75))
                                .lineLimit(3)
                            
                            HStack(spacing: 12) {
                                Button(action: {
                                    viewModel.newsStore.approvePost(post.id)
                                }) {
                                    HStack(spacing: 6) {
                                        Image(systemName: "checkmark.circle.fill")
                                        Text("ఆమోదించండి (Approve)")
                                    }
                                    .font(.system(size: 13, weight: .bold))
                                    .foregroundColor(.black)
                                    .frame(maxWidth: .infinity)
                                    .padding(.vertical, 10)
                                    .background(Color.green)
                                    .cornerRadius(8)
                                }
                                
                                Button(action: {
                                    viewModel.newsStore.rejectPost(post.id)
                                }) {
                                    HStack(spacing: 6) {
                                        Image(systemName: "xmark.circle.fill")
                                        Text("తిరస్కరించండి (Reject)")
                                    }
                                    .font(.system(size: 13, weight: .bold))
                                    .foregroundColor(.white)
                                    .frame(maxWidth: .infinity)
                                    .padding(.vertical, 10)
                                    .background(Color.red.opacity(0.8))
                                    .cornerRadius(8)
                                }
                            }
                            .padding(.top, 6)
                        }
                        .padding(14)
                        .background(Theme.navyCard)
                        .cornerRadius(12)
                        .padding(.horizontal, 16)
                    }
                }
            }
            .padding(.vertical, 14)
        }
    }
    
    // MARK: - Tab 1: Post News
    private var postNewsTab: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                Text("కొత్త కథనాన్ని ప్రచురించండి")
                    .font(.system(size: 16, weight: .bold))
                    .foregroundColor(Theme.goldColor)
                
                if let msg = postSuccessMessage {
                    Text(msg)
                        .font(.system(size: 13, weight: .semibold))
                        .foregroundColor(.green)
                        .padding(10)
                        .background(Color.green.opacity(0.15))
                        .cornerRadius(8)
                }
                
                VStack(alignment: .leading, spacing: 6) {
                    Text("శీర్షిక (Headline)")
                        .font(.system(size: 12, weight: .semibold))
                        .foregroundColor(.white.opacity(0.8))
                    
                    TextField("వార్త ముఖ్యాంశం రాయండి...", text: $newTitle)
                        .padding(12)
                        .background(Theme.navyCard)
                        .cornerRadius(8)
                        .foregroundColor(.white)
                }
                
                VStack(alignment: .leading, spacing: 6) {
                    Text("కేటగిరీ (Category)")
                        .font(.system(size: 12, weight: .semibold))
                        .foregroundColor(.white.opacity(0.8))
                    
                    Picker("Category", selection: $newCategory) {
                        ForEach(Categories.list, id: \.label) { cat in
                            Text(cat.label).tag(cat.label)
                        }
                    }
                    .pickerStyle(MenuPickerStyle())
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(8)
                    .background(Theme.navyCard)
                    .cornerRadius(8)
                }
                
                VStack(alignment: .leading, spacing: 6) {
                    Text("సంక్షిప్త వివరణ (Summary)")
                        .font(.system(size: 12, weight: .semibold))
                        .foregroundColor(.white.opacity(0.8))
                    
                    TextEditor(text: $newSummary)
                        .frame(height: 80)
                        .padding(6)
                        .background(Theme.navyCard)
                        .cornerRadius(8)
                        .foregroundColor(.white)
                }
                
                VStack(alignment: .leading, spacing: 6) {
                    Text("పూర్తి కథనం (Full HTML Story)")
                        .font(.system(size: 12, weight: .semibold))
                        .foregroundColor(.white.opacity(0.8))
                    
                    TextEditor(text: $newHtml)
                        .frame(height: 100)
                        .padding(6)
                        .background(Theme.navyCard)
                        .cornerRadius(8)
                        .foregroundColor(.white)
                }
                
                VStack(alignment: .leading, spacing: 6) {
                    Text("చిత్రం URL (Image URL)")
                        .font(.system(size: 12, weight: .semibold))
                        .foregroundColor(.white.opacity(0.8))
                    
                    TextField("https://...", text: $newImageUrl)
                        .padding(12)
                        .background(Theme.navyCard)
                        .cornerRadius(8)
                        .foregroundColor(.white)
                }
                
                Toggle(isOn: $isVideo) {
                    Text("వీడియో న్యూస్ / రీల్స్ (Is Video Story)")
                        .font(.system(size: 13, weight: .semibold))
                        .foregroundColor(.white)
                }
                .padding(.vertical, 4)
                
                if isVideo {
                    VStack(alignment: .leading, spacing: 6) {
                        Text("వీడియో URL (MP4 / Stream URL)")
                            .font(.system(size: 12, weight: .semibold))
                            .foregroundColor(.white.opacity(0.8))
                        
                        TextField("https://...", text: $newVideoUrl)
                            .padding(12)
                            .background(Theme.navyCard)
                            .cornerRadius(8)
                            .foregroundColor(.white)
                    }
                }
                
                Button(action: publishStory) {
                    Text("తక్షణమే ప్రచురించండి (Publish Live)")
                        .font(.system(size: 15, weight: .bold))
                        .foregroundColor(.black)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 14)
                        .background(Theme.goldColor)
                        .cornerRadius(10)
                }
                .disabled(newTitle.trimmingCharacters(in: .whitespaces).isEmpty)
                .opacity(newTitle.trimmingCharacters(in: .whitespaces).isEmpty ? 0.5 : 1.0)
            }
            .padding(16)
        }
    }
    
    private func publishStory() {
        let post = CustomPost(
            id: Int(Date().timeIntervalSince1970),
            title: newTitle,
            summary: newSummary.isEmpty ? newTitle : newSummary,
            imageUrl: newImageUrl.isEmpty ? nil : newImageUrl,
            videoUrl: newVideoUrl.isEmpty ? nil : newVideoUrl,
            category: newCategory,
            contentHtml: newHtml.isEmpty ? "<p>\(newSummary)</p>" : newHtml,
            authorName: viewModel.authStore.currentUser?.name ?? "Admin",
            authorEmail: viewModel.authStore.currentUser?.email ?? "admin@naatvtelugu.com",
            authorRole: .admin,
            status: .approved,
            isVideo: isVideo
        )
        viewModel.newsStore.addCustomPost(post)
        postSuccessMessage = "వార్త విజయవంతంగా ప్రచురించబడింది!"
        newTitle = ""
        newSummary = ""
        newHtml = ""
        newImageUrl = ""
        newVideoUrl = ""
        isVideo = false
    }
    
    // MARK: - Tab 2: Edit Existing News
    private var editNewsTab: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 14) {
                Text("వార్తలను సవరించండి (Edit Stories)")
                    .font(.system(size: 16, weight: .bold))
                    .foregroundColor(Theme.goldColor)
                
                if let msg = editSuccessMessage {
                    Text(msg)
                        .font(.system(size: 13, weight: .semibold))
                        .foregroundColor(.green)
                        .padding(10)
                        .background(Color.green.opacity(0.15))
                        .cornerRadius(8)
                }
                
                if let story = selectedStoryToEdit {
                    // Editing Form
                    VStack(alignment: .leading, spacing: 12) {
                        HStack {
                            Text("సవరిస్తున్న వార్త ID: #\(story.id)")
                                .font(.system(size: 13, weight: .bold))
                                .foregroundColor(Theme.goldColor)
                            
                            Spacer()
                            
                            Button(action: { selectedStoryToEdit = nil }) {
                                Text("రద్దు చేయండి")
                                    .font(.system(size: 12, weight: .semibold))
                                    .foregroundColor(.white.opacity(0.7))
                            }
                        }
                        
                        VStack(alignment: .leading, spacing: 4) {
                            Text("శీర్షిక (Title)")
                                .font(.system(size: 12))
                                .foregroundColor(.white.opacity(0.8))
                            TextField("Title", text: $editTitle)
                                .padding(10)
                                .background(Theme.navyCard)
                                .cornerRadius(8)
                                .foregroundColor(.white)
                        }
                        
                        VStack(alignment: .leading, spacing: 4) {
                            Text("కేటగిరీ (Category)")
                                .font(.system(size: 12))
                                .foregroundColor(.white.opacity(0.8))
                            TextField("Category", text: $editCategory)
                                .padding(10)
                                .background(Theme.navyCard)
                                .cornerRadius(8)
                                .foregroundColor(.white)
                        }
                        
                        VStack(alignment: .leading, spacing: 4) {
                            Text("సారాంశం (Summary)")
                                .font(.system(size: 12))
                                .foregroundColor(.white.opacity(0.8))
                            TextEditor(text: $editSummary)
                                .frame(height: 70)
                                .padding(6)
                                .background(Theme.navyCard)
                                .cornerRadius(8)
                                .foregroundColor(.white)
                        }
                        
                        VStack(alignment: .leading, spacing: 4) {
                            Text("పూర్తి కథనం (HTML)")
                                .font(.system(size: 12))
                                .foregroundColor(.white.opacity(0.8))
                            TextEditor(text: $editHtml)
                                .frame(height: 80)
                                .padding(6)
                                .background(Theme.navyCard)
                                .cornerRadius(8)
                                .foregroundColor(.white)
                        }
                        
                        Button(action: saveEditedStory) {
                            Text("మార్పులను భద్రపరచండి (Save Edits)")
                                .font(.system(size: 14, weight: .bold))
                                .foregroundColor(.black)
                                .frame(maxWidth: .infinity)
                                .padding(.vertical, 12)
                                .background(Theme.goldColor)
                                .cornerRadius(8)
                        }
                    }
                    .padding(14)
                    .background(Theme.navyPrimary)
                    .cornerRadius(12)
                } else {
                    // Story Picker List
                    Text("సవరించడానికి వార్తను ఎంచుకోండి:")
                        .font(.system(size: 13))
                        .foregroundColor(.white.opacity(0.7))
                    
                    ForEach(viewModel.allCombinedItems.prefix(25)) { item in
                        Button(action: {
                            selectedStoryToEdit = item
                            editTitle = item.title
                            editCategory = item.category
                            editSummary = item.summary
                            editHtml = item.contentHtml
                        }) {
                            HStack(alignment: .top, spacing: 10) {
                                VStack(alignment: .leading, spacing: 4) {
                                    Text(item.title)
                                        .font(.system(size: 13, weight: .bold))
                                        .foregroundColor(.white)
                                        .lineLimit(2)
                                    
                                    HStack {
                                        Text(item.category)
                                            .font(.system(size: 10, weight: .semibold))
                                            .foregroundColor(Theme.goldColor)
                                        
                                        Spacer()
                                        
                                        Text("సవరించండి ✏️")
                                            .font(.system(size: 11, weight: .semibold))
                                            .foregroundColor(Theme.goldColor)
                                    }
                                }
                            }
                            .padding(12)
                            .background(Theme.navyCard)
                            .cornerRadius(10)
                        }
                        .buttonStyle(PlainButtonStyle())
                    }
                }
            }
            .padding(16)
        }
    }
    
    private func saveEditedStory() {
        guard let story = selectedStoryToEdit else { return }
        let edited = EditedStory(
            id: story.id,
            title: editTitle,
            summary: editSummary,
            category: editCategory,
            contentHtml: editHtml
        )
        viewModel.adminStore.saveEditedStory(edited)
        editSuccessMessage = "వార్త విజయవంతంగా సవరించబడింది!"
        selectedStoryToEdit = nil
    }
    
    // MARK: - Tab 3: Customization (Typography, Logos, Modules)
    private var customizationTab: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                Text("యాప్ కస్టమైజేషన్ సెట్టింగ్‌లు")
                    .font(.system(size: 16, weight: .bold))
                    .foregroundColor(Theme.goldColor)
                
                // Font Size Customization
                VStack(alignment: .leading, spacing: 14) {
                    Text("ఫాంట్ పరిమాణాలు (Font Sizes)")
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(.white)
                    
                    // Headline Size Slider
                    VStack(alignment: .leading, spacing: 4) {
                        HStack {
                            Text("శీర్షిక పరిమాణం (Headline Size):")
                                .font(.system(size: 12))
                                .foregroundColor(.white.opacity(0.8))
                            Spacer()
                            Text("\(Int(viewModel.adminStore.settings.headlineFontSize)) pt")
                                .font(.system(size: 12, weight: .bold))
                                .foregroundColor(Theme.goldColor)
                        }
                        
                        Slider(
                            value: Binding(
                                get: { viewModel.adminStore.settings.headlineFontSize },
                                set: { var s = viewModel.adminStore.settings; s.headlineFontSize = $0; viewModel.adminStore.updateSettings(s) }
                            ),
                            in: 16...32,
                            step: 1
                        )
                        .accentColor(Theme.goldColor)
                    }
                    
                    // Body Size Slider
                    VStack(alignment: .leading, spacing: 4) {
                        HStack {
                            Text("బాడీ టెక్స్ట్ పరిమాణం (Body Size):")
                                .font(.system(size: 12))
                                .foregroundColor(.white.opacity(0.8))
                            Spacer()
                            Text("\(Int(viewModel.adminStore.settings.bodyFontSize)) pt")
                                .font(.system(size: 12, weight: .bold))
                                .foregroundColor(Theme.goldColor)
                        }
                        
                        Slider(
                            value: Binding(
                                get: { viewModel.adminStore.settings.bodyFontSize },
                                set: { var s = viewModel.adminStore.settings; s.bodyFontSize = $0; viewModel.adminStore.updateSettings(s) }
                            ),
                            in: 12...22,
                            step: 1
                        )
                        .accentColor(Theme.goldColor)
                    }
                    
                    // Logo Height Slider
                    VStack(alignment: .leading, spacing: 4) {
                        HStack {
                            Text("లోగో ఎత్తు (Logo Height):")
                                .font(.system(size: 12))
                                .foregroundColor(.white.opacity(0.8))
                            Spacer()
                            Text("\(Int(viewModel.adminStore.settings.logoHeight)) pt")
                                .font(.system(size: 12, weight: .bold))
                                .foregroundColor(Theme.goldColor)
                        }
                        
                        Slider(
                            value: Binding(
                                get: { viewModel.adminStore.settings.logoHeight },
                                set: { var s = viewModel.adminStore.settings; s.logoHeight = $0; viewModel.adminStore.updateSettings(s) }
                            ),
                            in: 24...60,
                            step: 2
                        )
                        .accentColor(Theme.goldColor)
                    }
                    
                    // Image Aspect Ratio
                    VStack(alignment: .leading, spacing: 4) {
                        HStack {
                            Text("చిత్రం యాస్పెక్ట్ రేషియో (Image Aspect Ratio):")
                                .font(.system(size: 12))
                                .foregroundColor(.white.opacity(0.8))
                            Spacer()
                            Text(String(format: "%.2f:1", viewModel.adminStore.settings.imageAspectRatio))
                                .font(.system(size: 12, weight: .bold))
                                .foregroundColor(Theme.goldColor)
                        }
                        
                        Slider(
                            value: Binding(
                                get: { viewModel.adminStore.settings.imageAspectRatio },
                                set: { var s = viewModel.adminStore.settings; s.imageAspectRatio = $0; viewModel.adminStore.updateSettings(s) }
                            ),
                            in: 1.0...2.2,
                            step: 0.05
                        )
                        .accentColor(Theme.goldColor)
                    }
                }
                .padding(14)
                .background(Theme.navyCard)
                .cornerRadius(12)
                
                // Font Family Selector
                VStack(alignment: .leading, spacing: 10) {
                    Text("ఫాంట్ ఫ్యామిలీ (Font Family)")
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(.white)
                    
                    Picker("Font Family", selection: Binding(
                        get: { viewModel.adminStore.settings.fontFamily },
                        set: { var s = viewModel.adminStore.settings; s.fontFamily = $0; viewModel.adminStore.updateSettings(s) }
                    )) {
                        Text("Default System").tag("Default")
                        Text("Serif (Classic)").tag("Serif")
                        Text("Monospace").tag("Monospace")
                        Text("Rounded").tag("Rounded")
                    }
                    .pickerStyle(SegmentedPickerStyle())
                }
                .padding(14)
                .background(Theme.navyCard)
                .cornerRadius(12)
                
                // Module Toggles (Guest & Employee modules)
                VStack(alignment: .leading, spacing: 12) {
                    Text("మాడ్యూల్ నియంత్రణలు (Module Controls)")
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(.white)
                    
                    Toggle(isOn: Binding(
                        get: { viewModel.adminStore.settings.allowGuestBookmarks },
                        set: { var s = viewModel.adminStore.settings; s.allowGuestBookmarks = $0; viewModel.adminStore.updateSettings(s) }
                    )) {
                        VStack(alignment: .leading, spacing: 2) {
                            Text("గెస్ట్ బుక్‌మార్క్స్ అనుమతించండి")
                                .font(.system(size: 13, weight: .medium))
                                .foregroundColor(.white)
                            Text("ఖాతా లేకపోయినా కథనాలను భద్రపరచవచ్చు")
                                .font(.system(size: 11))
                                .foregroundColor(.white.opacity(0.6))
                        }
                    }
                    
                    Divider().background(Color.white.opacity(0.1))
                    
                    Toggle(isOn: Binding(
                        get: { viewModel.adminStore.settings.requireGpsForPosting },
                        set: { var s = viewModel.adminStore.settings; s.requireGpsForPosting = $0; viewModel.adminStore.updateSettings(s) }
                    )) {
                        VStack(alignment: .leading, spacing: 2) {
                            Text("రిపోర్టింగ్ సమయంలో GPS తప్పనిసరి")
                                .font(.system(size: 13, weight: .medium))
                                .foregroundColor(.white)
                            Text("వార్త పంపినప్పుడు ఖచ్చితమైన స్థానాన్ని నమోదు చేస్తుంది")
                                .font(.system(size: 11))
                                .foregroundColor(.white.opacity(0.6))
                        }
                    }
                }
                .padding(14)
                .background(Theme.navyCard)
                .cornerRadius(12)
                
                // Reset Button
                Button(action: {
                    viewModel.adminStore.updateSettings(AdminSettings())
                }) {
                    Text("డిఫాల్ట్ సెట్టింగ్‌లకు పునరుద్ధరించండి (Reset)")
                        .font(.system(size: 13, weight: .semibold))
                        .foregroundColor(.red.opacity(0.9))
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 10)
                        .background(Color.white.opacity(0.06))
                        .cornerRadius(8)
                }
            }
            .padding(16)
        }
    }
    
    // MARK: - Tab 4: Directory (Users & Staff)
    private var directoryTab: some View {
        let all = viewModel.authStore.allUsers
        let filtered: [AppUser] = {
            switch directoryRoleFilter {
            case "Employees": return all.filter { $0.role == .employee || $0.role == .admin }
            case "Users": return all.filter { $0.role == .user }
            case "Guests": return all.filter { $0.role == .guest }
            default: return all
            }
        }()
        
        return ScrollView {
            VStack(alignment: .leading, spacing: 14) {
                HStack {
                    Text("నమోదిత వినియోగదారులు & సిబ్బంది")
                        .font(.system(size: 15, weight: .bold))
                        .foregroundColor(Theme.goldColor)
                    
                    Spacer()
                    
                    Text("\(filtered.count) మంది")
                        .font(.system(size: 12, weight: .semibold))
                        .foregroundColor(.white.opacity(0.7))
                }
                .padding(.horizontal, 16)
                .padding(.top, 8)
                
                // Role Filter Pills
                HStack(spacing: 8) {
                    ForEach(["All", "Employees", "Users", "Guests"], id: \.self) { roleKey in
                        Button(action: { directoryRoleFilter = roleKey }) {
                            Text(roleKey)
                                .font(.system(size: 12, weight: directoryRoleFilter == roleKey ? .bold : .medium))
                                .foregroundColor(directoryRoleFilter == roleKey ? .black : .white)
                                .padding(.horizontal, 12)
                                .padding(.vertical, 6)
                                .background(directoryRoleFilter == roleKey ? Theme.goldColor : Theme.navyCard)
                                .cornerRadius(14)
                        }
                    }
                }
                .padding(.horizontal, 16)
                
                // User Cards
                ForEach(filtered) { user in
                    VStack(alignment: .leading, spacing: 10) {
                        HStack(spacing: 12) {
                            // Avatar Circle
                            ZStack {
                                Circle()
                                    .fill(Theme.navyPrimary)
                                    .frame(width: 44, height: 44)
                                    .overlay(Circle().stroke(Theme.goldColor.opacity(0.6), lineWidth: 1.5))
                                
                                Text(String(user.name.prefix(1)).uppercased())
                                    .font(.system(size: 18, weight: .bold))
                                    .foregroundColor(Theme.goldColor)
                            }
                            
                            VStack(alignment: .leading, spacing: 2) {
                                Text(user.name)
                                    .font(.system(size: 14, weight: .bold))
                                    .foregroundColor(.white)
                                
                                Text(user.email)
                                    .font(.system(size: 11))
                                    .foregroundColor(.white.opacity(0.6))
                                
                                Text(user.phone)
                                    .font(.system(size: 11))
                                    .foregroundColor(.white.opacity(0.5))
                            }
                            
                            Spacer()
                            
                            // Role Badge
                            Text(user.role.displayName)
                                .font(.system(size: 10, weight: .bold))
                                .foregroundColor(.black)
                                .padding(.horizontal, 8)
                                .padding(.vertical, 3)
                                .background(roleColor(for: user.role))
                                .cornerRadius(4)
                        }
                        
                        Divider().background(Color.white.opacity(0.08))
                        
                        // Status & Post Stats
                        HStack {
                            VStack(alignment: .leading, spacing: 2) {
                                Text("పని స్థితి (Work Status):")
                                    .font(.system(size: 10))
                                    .foregroundColor(.white.opacity(0.6))
                                
                                Text(user.workStatus)
                                    .font(.system(size: 12, weight: .semibold))
                                    .foregroundColor(workStatusColor(for: user.workStatus))
                            }
                            
                            Spacer()
                            
                            VStack(alignment: .trailing, spacing: 2) {
                                Text("పోస్ట్ చేసిన వార్తలు:")
                                    .font(.system(size: 10))
                                    .foregroundColor(.white.opacity(0.6))
                                
                                Text("\(viewModel.getPostCount(for: user.email)) కథనాలు")
                                    .font(.system(size: 12, weight: .bold))
                                    .foregroundColor(Theme.goldColor)
                            }
                        }
                        
                        // Work Status Switcher (for Employees & Admins)
                        if user.role == .employee || user.role == .admin {
                            HStack(spacing: 6) {
                                ForEach(UserRole.workStatuses, id: \.self) { st in
                                    let isCurrent = user.workStatus == st
                                    Button(action: {
                                        viewModel.authStore.updateWorkStatus(email: user.email, newStatus: st)
                                    }) {
                                        Text(st.components(separatedBy: " ").first ?? st)
                                            .font(.system(size: 10, weight: isCurrent ? .bold : .medium))
                                            .foregroundColor(isCurrent ? .black : .white)
                                            .padding(.horizontal, 8)
                                            .padding(.vertical, 5)
                                            .background(isCurrent ? Theme.goldColor : Color.white.opacity(0.08))
                                            .cornerRadius(6)
                                    }
                                }
                            }
                        }
                        
                        // Role Promotion / Demotion Actions
                        if user.role != .admin {
                            HStack(spacing: 8) {
                                if user.role == .user {
                                    Button(action: {
                                        viewModel.authStore.updateRole(email: user.email, newRole: .employee)
                                    }) {
                                        Text("సిబ్బందిగా మార్చండి (Promote to Staff)")
                                            .font(.system(size: 11, weight: .bold))
                                            .foregroundColor(.black)
                                            .frame(maxWidth: .infinity)
                                            .padding(.vertical, 7)
                                            .background(Color(hex: "38BDF8"))
                                            .cornerRadius(6)
                                    }
                                } else if user.role == .employee {
                                    Button(action: {
                                        viewModel.authStore.updateRole(email: user.email, newRole: .user)
                                    }) {
                                        Text("యూజర్‌గా మార్చండి (Demote to User)")
                                            .font(.system(size: 11, weight: .bold))
                                            .foregroundColor(.white)
                                            .frame(maxWidth: .infinity)
                                            .padding(.vertical, 7)
                                            .background(Color.white.opacity(0.12))
                                            .cornerRadius(6)
                                    }
                                }
                            }
                        }
                    }
                    .padding(14)
                    .background(Theme.navyCard)
                    .cornerRadius(12)
                    .padding(.horizontal, 16)
                }
            }
            .padding(.vertical, 10)
        }
    }
    
    private func roleColor(for role: UserRole) -> Color {
        switch role {
        case .admin: return Theme.goldColor
        case .employee: return Color(hex: "38BDF8")
        case .user: return Color(hex: "4ADE80")
        case .guest: return Color.gray
        }
    }
    
    private func workStatusColor(for status: String) -> Color {
        if status.contains("డ్యూటీ") || status.contains("On Duty") {
            return Color.green
        } else if status.contains("సెలవు") || status.contains("Leave") {
            return Color.orange
        } else {
            return Color(hex: "A78BFA")
        }
    }
}
