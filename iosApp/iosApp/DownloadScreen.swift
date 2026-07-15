//
//  DownloadPage.swift
//  iosApp
//
//  Created by Devlomi on 13/07/2026.
//
import SwiftUI
import sharedKit

struct DownloadScreen: View {
    @State private var isDownloading = false
    @State private var downloadProgress: Double = 0.5
    @State private var showDownloadButton = false
    @State private var showCancelButton = false
    @State private var showDownloadingText = false

    var body: some View {
        ZStack {
            // Background color
            Color(red: 0.1, green: 0.1, blue: 0.3) // colorPrimary
                .ignoresSafeArea()

            VStack(spacing: 16) {
                Spacer()
                    .frame(height: 32)

                // App Icon
                Image("app_icon")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 150, height: 150)
                    .foregroundColor(.white)

                Spacer()

                // Downloading Text
                if showDownloadingText {
                    Text("Downloading Files")
                        .font(.system(size: 25, weight: .medium))
                        .foregroundColor(Color(red: 0.2, green: 0.2, blue: 0.2)) // colorOnBackground
                        .multilineTextAlignment(.center)
                }

                // Progress Bar
                if showDownloadingText {
                    ProgressView(value: downloadProgress)
                        .tint(Color(red: 0.2, green: 0.6, blue: 0.8)) // colorSecondary
                        .frame(height: 13)
                        .padding(.horizontal, 32)
                        .background(Color.white)
                        .cornerRadius(16)
                }

                // Download Button
                if showDownloadButton && !isDownloading {
                    Button(action: {
                        isDownloading = true
                        showDownloadingText = true
                    }) {
                        Text("Download")
                            .font(.system(size: 18, weight: .medium))
                            .frame(maxWidth: .infinity)
                    }
//                    .buttonStyle(.filled)
                    .tint(Color(red: 0.15, green: 0.15, blue: 0.25)) // colorPrimaryVariant
                    .padding(.horizontal, 32)
                    .shadow(radius: 8)
                }

                // Cancel Button
                if showCancelButton && isDownloading {
                    Button(action: {
                        isDownloading = false
                        showDownloadingText = false
                    }) {
                        Text("Cancel")
                            .font(.system(size: 18, weight: .medium))
                            .frame(maxWidth: .infinity)
                    }
//                    .buttonStyle(.filled)
                    .tint(Color(red: 0.15, green: 0.15, blue: 0.25)) // colorPrimaryVariant
                    .padding(.horizontal, 32)
                    .shadow(radius: 8)
                }

                Spacer()
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
    }
}

#Preview {
    DownloadScreen()
}
