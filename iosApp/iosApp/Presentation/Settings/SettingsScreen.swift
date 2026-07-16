//
//  SettingsScreen.swift
//  iosApp
//
//  Created by Devlomi on 15/07/2026.
//

import SwiftUI
import sharedKit
import KMPObservableViewModelSwiftUI

struct SettingsScreen: View {
    @State private var disableScreenLock: Bool = false
    @Environment(\.openURL) var openURL
    
    var body: some View {
        ZStack {
            Color.colorPrimary
                .ignoresSafeArea()
            
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    // Version Section
                    VStack(alignment: .leading, spacing: 8) {
                        Text("Version")
                            .font(.custom("Cairo-Bold", size: 14))
                            .foregroundColor(.white)
                        
                        Text(Bundle.main.appVersion)
                            .font(.custom("Cairo-Regular", size: 14))
                            .foregroundColor(.colorOnPrimary)
                    }
                    .padding(.horizontal, 8)
                    .padding(.top, 16)
                    .padding(.bottom, 8)
                    
                    Divider()
                        .frame(height: 0.3)
                        .background(Color(hex: "#e1e1e1"))
                        .padding(.vertical, 8)
                    
                    // Screen Lock Toggle
                    HStack(spacing: 8) {
                        Image("ic_screen_lock")
                            .resizable()
                            .frame(width: 24, height: 24)
                            .foregroundColor(.white)
                        
                        //TODO
                        Text("SharedString.getString(string: Strings.ScreenLockPrevent)")
                            .font(.custom("Cairo-Regular", size: 14))
                            .foregroundColor(.white)
                        
                        Spacer()
                        
                        Toggle("", isOn: $disableScreenLock)
                            .tint(.colorSecondary)
                    }
                    .padding(.horizontal, 8)
                    .padding(.vertical, 12)
                    
                    Divider()
                        .frame(height: 0.3)
                        .background(Color(hex: "#e1e1e1"))
                        .padding(.vertical, 8)
                    
                    // Share App
                    Button(action: shareApp) {
                        HStack(spacing: 8) {
                            Image("ic_share")
                                .resizable()
                                .frame(width: 24, height: 24)
                                .foregroundColor(.white)
                            
                            //TODO
                            Text("SharedString.getString(string: Strings.ShareApp)")
                                .font(.custom("Cairo-Regular", size: 14))
                                .foregroundColor(.white)
                            
                            Spacer()
                        }
                    }
                    .padding(.horizontal, 8)
                    .padding(.vertical, 12)
                    
                    // Website
                    Button(action: {
                        openURL(URL(string: "https://yourwebsite.com")!)
                    }) {
                        HStack(spacing: 8) {
                            Image("ic_website")
                                .resizable()
                                .frame(width: 24, height: 24)
                                .foregroundColor(.white)
                            
                            //TODO
                            Text("SharedString.getString(string: Strings.Website)")
                                .font(.custom("Cairo-Regular", size: 14))
                                .foregroundColor(.white)
                            
                            Spacer()
                        }
                    }
                    .padding(.horizontal, 8)
                    .padding(.top, 16)
                    .padding(.bottom, 12)
                    
                    // Follow Us (Twitter)
                    Button(action: {
                        openURL(URL(string: "https://twitter.com/yourhandle")!)
                    }) {
                        HStack(spacing: 8) {
                            Image("ic_twitter")
                                .resizable()
                                .frame(width: 24, height: 24)
                                .foregroundColor(.white)
                            
                            //TODO
                            Text("SharedString.getString(string: Strings.FollowUs)")
                                .font(.custom("Cairo-Regular", size: 14))
                                .foregroundColor(.white)
                            
                            Spacer()
                        }
                    }
                    .padding(.horizontal, 8)
                    .padding(.top, 16)
                    .padding(.bottom, 12)
                    
                    // GitHub
                    Button(action: {
                        openURL(URL(string: "https://github.com/yourrepo")!)
                    }) {
                        HStack(spacing: 8) {
                            Image("ic_github")
                                .resizable()
                                .frame(width: 24, height: 24)
                                .foregroundColor(.white)
                            //TODO
                            Text("SharedString.getString(string: Strings.GitHub)")
                                .font(.custom("Cairo-Regular", size: 14))
                                .foregroundColor(.white)
                            
                            Spacer()
                        }
                    }
                    .padding(.horizontal, 8)
                    .padding(.top, 16)
                    .padding(.bottom, 12)
                    
                    // Rate App
                    Button(action: rateApp) {
                        HStack(spacing: 8) {
                            Image("ic_star")
                                .resizable()
                                .frame(width: 24, height: 24)
                                .foregroundColor(.colorSecondary)
                            //TODO
                            Text("SharedString.getString(string: Strings.RateApp)")
                                .font(.custom("Cairo-Regular", size: 14))
                                .foregroundColor(.white)
                            
                            Spacer()
                        }
                    }
                    .padding(.horizontal, 8)
                    .padding(.top, 16)
                    .padding(.bottom, 12)
                    
                    Spacer()
                        .frame(height: 32)
                }
                .padding(.horizontal, 0)
            }
            .scrollIndicators(.hidden)
        }
    }
    
    private func shareApp() {
        let appURL = "https://apps.apple.com/app/id1234567890"
        let shareText = "Check out this amazing Quran app!"
        
        if let url = URL(string: "https://apps.apple.com/app/id1234567890") {
            let activityViewController = UIActivityViewController(
                activityItems: [shareText, url],
                applicationActivities: nil
            )
            
            if let scene = UIApplication.shared.connectedScenes.first as? UIWindowScene {
                scene.windows.first?.rootViewController?.present(activityViewController, animated: true)
            }
        }
    }
    
    private func rateApp() {
        let appID = "1234567890"
        if let url = URL(string: "itms-apps://apps.apple.com/app/id\(appID)?action=write-review") {
            openURL(url)
        }
    }
}




#Preview {
    SettingsScreen()
}
