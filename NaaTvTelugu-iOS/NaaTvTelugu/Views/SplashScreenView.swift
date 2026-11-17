import SwiftUI

public struct SplashScreenView: View {
    public var onDismiss: () -> Void
    
    public init(onFinished: @escaping () -> Void) {
        self.onDismiss = onFinished
    }
    
    public init(onDismiss: @escaping () -> Void) {
        self.onDismiss = onDismiss
    }
    
    public var body: some View {
        ZStack {
            Color.naaNavy
                .ignoresSafeArea()
            
            VStack(spacing: 20) {
                Spacer()
                
                // Official NAA TV Telugu Logo Card (Vibrant, high contrast & immediately visible)
                VStack {
                    Image("naa_tv_logo")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 250, height: 80)
                        .padding(.horizontal, 28)
                        .padding(.vertical, 18)
                }
                .background(Color.white)
                .cornerRadius(22)
                .overlay(
                    RoundedRectangle(cornerRadius: 22)
                        .stroke(Color.naaGold, lineWidth: 2.5)
                )
                .shadow(color: Color.black.opacity(0.35), radius: 12, x: 0, y: 6)
                
                Text("NAA TV Telugu")
                    .font(.system(size: 28, weight: .bold))
                    .foregroundColor(.white)
                    .tracking(0.5)
                
                Text("క్షణాల్లో తాజా వార్తలు • ఎప్పటికప్పుడు ప్రత్యక్ష ప్రసారం")
                    .font(.system(size: 14, weight: .semibold))
                    .foregroundColor(Color.naaGold)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 24)
                
                Spacer()
                
                VStack(spacing: 6) {
                    Text(NaaTvBrand.copyright)
                        .font(.system(size: 11, weight: .regular))
                        .foregroundColor(.white.opacity(0.65))
                    
                    Text(NaaTvBrand.portal)
                        .font(.system(size: 11, weight: .medium))
                        .foregroundColor(Color.naaGold.opacity(0.9))
                }
                .padding(.bottom, 28)
            }
        }
        .contentShape(Rectangle())
        .onTapGesture {
            onDismiss()
        }
        .onAppear {
            DispatchQueue.main.asyncAfter(deadline: .now() + 2.2) {
                onDismiss()
            }
        }
    }
}
