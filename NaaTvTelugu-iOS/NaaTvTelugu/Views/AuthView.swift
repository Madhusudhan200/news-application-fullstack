import SwiftUI

public struct AuthView: View {
    @ObservedObject var viewModel: NewsViewModel
    var onDismiss: () -> Void
    
    @State private var isSignUp: Bool = false
    @State private var name: String = ""
    @State private var email: String = ""
    @State private var pass: String = ""
    @State private var selectedRole: UserRole = .user
    @State private var errorMessage: String?
    
    public init(viewModel: NewsViewModel, onDismiss: @escaping () -> Void) {
        self.viewModel = viewModel
        self.onDismiss = onDismiss
    }
    
    public var body: some View {
        ZStack {
            Theme.darkBackground.ignoresSafeArea()
            
            VStack(spacing: 0) {
                // Top Close Button
                HStack {
                    Spacer()
                    Button(action: onDismiss) {
                        Image(systemName: "xmark.circle.fill")
                            .font(.system(size: 24))
                            .foregroundColor(.white.opacity(0.6))
                    }
                }
                .padding(.horizontal, 20)
                .padding(.top, 16)
                
                ScrollView {
                    VStack(spacing: 24) {
                        // Brand Logo Card (Vibrant NAA TV logo, NO duplicate logo text below, NO "verified news real time updates" tagline)
                        VStack(spacing: 8) {
                            ZStack {
                                Circle()
                                    .fill(Color.white)
                                    .frame(width: 88, height: 88)
                                    .overlay(
                                        Circle()
                                            .stroke(Theme.goldColor, lineWidth: 2.5)
                                    )
                                    .shadow(color: Theme.goldColor.opacity(0.3), radius: 10, x: 0, y: 4)
                                
                                Image("naa_tv_logo")
                                    .resizable()
                                    .scaledToFit()
                                    .frame(width: 68, height: 68)
                            }
                        }
                        .padding(.top, 10)
                        
                        // Auth Card Form
                        VStack(spacing: 16) {
                            Text(isSignUp ? "కొత్త ఖాతాను సృష్టించండి" : "ఖాతాలోకి ప్రవేశించండి")
                                .font(.system(size: 18, weight: .bold))
                                .foregroundColor(Theme.goldColor)
                            
                            if let error = errorMessage {
                                Text(error)
                                    .font(.system(size: 12, weight: .medium))
                                    .foregroundColor(.red)
                                    .multilineTextAlignment(.center)
                                    .padding(.horizontal, 10)
                            }
                            
                            if isSignUp {
                                VStack(alignment: .leading, spacing: 6) {
                                    Text("మీ పూర్తి పేరు (Full Name)")
                                        .font(.system(size: 12))
                                        .foregroundColor(.white.opacity(0.8))
                                    
                                    TextField("పూర్తి పేరు నమోదు చేయండి", text: $name)
                                        .padding(12)
                                        .background(Theme.navyPrimary)
                                        .cornerRadius(8)
                                        .foregroundColor(.white)
                                }
                            }
                            
                            VStack(alignment: .leading, spacing: 6) {
                                Text("ఇమెయిల్ చిరునామా (Email)")
                                    .font(.system(size: 12))
                                    .foregroundColor(.white.opacity(0.8))
                                
                                TextField("name@example.com", text: $email)
                                    .keyboardType(.emailAddress)
                                    .autocapitalization(.none)
                                    .padding(12)
                                    .background(Theme.navyPrimary)
                                    .cornerRadius(8)
                                    .foregroundColor(.white)
                            }
                            
                            VStack(alignment: .leading, spacing: 6) {
                                Text("పాస్‌వర్డ్ (Password)")
                                    .font(.system(size: 12))
                                    .foregroundColor(.white.opacity(0.8))
                                
                                SecureField("పాస్‌వర్డ్", text: $pass)
                                    .padding(12)
                                    .background(Theme.navyPrimary)
                                    .cornerRadius(8)
                                    .foregroundColor(.white)
                            }
                            
                            if isSignUp {
                                VStack(alignment: .leading, spacing: 6) {
                                    Text("పాత్ర (Account Role)")
                                        .font(.system(size: 12))
                                        .foregroundColor(.white.opacity(0.8))
                                    
                                    Picker("Role", selection: $selectedRole) {
                                        Text("పాఠకుడు (Reader / User)").tag(UserRole.user)
                                        Text("జర్నలిస్ట్ / సిబ్బంది (Reporter)").tag(UserRole.employee)
                                    }
                                    .pickerStyle(SegmentedPickerStyle())
                                }
                            }
                            
                            // Primary Submit Action
                            Button(action: handleAuthSubmit) {
                                Text(isSignUp ? "నమోదు చేసుకోండి (Sign Up)" : "లాగిన్ అవ్వండి (Sign In)")
                                    .font(.system(size: 15, weight: .bold))
                                    .foregroundColor(.black)
                                    .frame(maxWidth: .infinity)
                                    .padding(.vertical, 14)
                                    .background(Theme.goldColor)
                                    .cornerRadius(8)
                            }
                            .padding(.top, 4)
                            
                            // Toggle Mode
                            Button(action: {
                                isSignUp.toggle()
                                errorMessage = nil
                            }) {
                                Text(isSignUp ? "ఖాతా ఉందా? లాగిన్ అవ్వండి" : "ఖాతా లేదా? ఇప్పుడే సైన్ అప్ అవ్వండి")
                                    .font(.system(size: 13, weight: .semibold))
                                    .foregroundColor(Theme.goldColor.opacity(0.9))
                            }
                        }
                        .padding(20)
                        .background(Theme.navyCard)
                        .cornerRadius(16)
                        .overlay(
                            RoundedRectangle(cornerRadius: 16)
                                .stroke(Color.white.opacity(0.1), lineWidth: 1)
                        )
                        .padding(.horizontal, 20)
                        
                        // Demo Accounts Shortcut
                        VStack(spacing: 10) {
                            Text("త్వరిత డెమో లాగిన్స్ (Quick Access):")
                                .font(.system(size: 11, weight: .semibold))
                                .foregroundColor(.white.opacity(0.6))
                            
                            HStack(spacing: 10) {
                                Button(action: {
                                    _ = viewModel.authStore.login(email: "admin@naatvtelugu.com", pass: "admin123")
                                    onDismiss()
                                }) {
                                    Text("🔑 Admin")
                                        .font(.system(size: 12, weight: .bold))
                                        .foregroundColor(.black)
                                        .padding(.horizontal, 14)
                                        .padding(.vertical, 8)
                                        .background(Theme.goldColor)
                                        .cornerRadius(8)
                                }
                                
                                Button(action: {
                                    _ = viewModel.authStore.login(email: "reporter@naatvtelugu.com", pass: "reporter123")
                                    onDismiss()
                                }) {
                                    Text("🎙️ Reporter")
                                        .font(.system(size: 12, weight: .bold))
                                        .foregroundColor(.white)
                                        .padding(.horizontal, 14)
                                        .padding(.vertical, 8)
                                        .background(Color(hex: "38BDF8"))
                                        .cornerRadius(8)
                                }
                                
                                Button(action: {
                                    _ = viewModel.authStore.guestLogin()
                                    onDismiss()
                                }) {
                                    Text("👤 Guest")
                                        .font(.system(size: 12, weight: .bold))
                                        .foregroundColor(.white)
                                        .padding(.horizontal, 14)
                                        .padding(.vertical, 8)
                                        .background(Color.white.opacity(0.12))
                                        .cornerRadius(8)
                                }
                            }
                        }
                        
                        Spacer(minLength: 20)
                        
                        // Copyright line (strictly 2018)
                        Text(NaaTvBrand.copyright)
                            .font(.system(size: 11))
                            .foregroundColor(.white.opacity(0.4))
                            .padding(.bottom, 20)
                    }
                }
            }
        }
    }
    
    private func handleAuthSubmit() {
        if isSignUp {
            guard !name.trimmingCharacters(in: .whitespaces).isEmpty,
                  !email.trimmingCharacters(in: .whitespaces).isEmpty,
                  !pass.isEmpty else {
                errorMessage = "దయచేసి అన్ని వివరాలను నమోదు చేయండి."
                return
            }
            let res = viewModel.authStore.register(name: name, email: email, pass: pass, role: selectedRole)
            switch res {
            case .success:
                onDismiss()
            case .failure(let err):
                errorMessage = err.localizedDescription
            }
        } else {
            guard !email.trimmingCharacters(in: .whitespaces).isEmpty,
                  !pass.isEmpty else {
                errorMessage = "దయచేసి ఇమెయిల్ మరియు పాస్‌వర్డ్ నమోదు చేయండి."
                return
            }
            let res = viewModel.authStore.login(email: email, pass: pass)
            switch res {
            case .success:
                onDismiss()
            case .failure(let err):
                errorMessage = err.localizedDescription
            }
        }
    }
}
