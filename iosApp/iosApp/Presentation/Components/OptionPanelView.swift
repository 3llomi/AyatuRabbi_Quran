//
//  QuranPanelLayout.swift
//  iosApp
//
//  Created by Devlomi on 15/07/2026.
//
import SwiftUI
import sharedKit
import KMPObservableViewModelSwiftUI
import SwiftUIPager
import KMPNativeCoroutinesAsync

struct OptionsPanelScreen: View {

    @State private var currentPage: Int = 0
    @State private var showOptionsPanel: Bool = true
    @State private var selectedColor: ColorItem = .dkblue
    @State private var showColorPicker: Bool = false
    

    @StateViewModel var viewModel = KoinKt.getQuranPageViewModel()
    
    @ObservedObject var page: Page = .first()
    
    var body: some View {
        ZStack {
            // Background
            Color.colorPrimary
                .ignoresSafeArea()
            
            let _ = print("page count \(viewModel.quranPages.count)")
            
            VStack(spacing: 0) {
                // Pager with Quran Pages
                Pager(page: page,
                      data: Array(_immutableCocoaArray: viewModel.quranPages),id:\.self,
                      content: { content in
                    QuranPageView(page: content)
                        .scaleEffect(x: -1, y: 1, anchor: .center)  // RTL flip
                })
//                .multiplePagesPerScreen(0.9)
                .padding(.vertical, 16)
                
                Spacer()
                
                // Animated Options Panel
                if showOptionsPanel {
                    OptionsPanelView(
                        selectedColor: $selectedColor,
                        showColorPicker: $showColorPicker,
                        onSettingsTap: { print("Settings tapped") },
                        onShareTap: { print("Share tapped") },
                        onBookmarksTap: { print("Bookmarks tapped") },
                        onColorTap: { showColorPicker = true },
                        onBookmarkTap: { print("Bookmark tapped") },
                        onZoomTap: { print("Zoom tapped") },
                        onSurasTap: { print("Suras tapped") }
                    )
                    .transition(.move(edge: .bottom).combined(with: .opacity))
                }
            }
            
            // Tap to toggle options panel
            VStack {
                HStack {
                    Spacer()
                    Button(action: { withAnimation(.easeInOut(duration: 0.3)) { showOptionsPanel.toggle() } }) {
                        Image(systemName: "chevron.up")
                            .foregroundColor(.colorOnSecondary)
                            .rotationEffect(.degrees(showOptionsPanel ? 180 : 0))
                    }
                    .padding()
                }
                Spacer()
            }
        }.onAppear{
            Task {
                do {
                    let sequence = asyncSequence(for: viewModel.currentIndexFlow)
                    
                    for try await result in sequence {
                        page.update(.new(index: Int(truncating: result)))
                    }
                } catch {
                    print("Failed with error: \(error)")
                }
            }
            
            
        }
    }
}

struct QuranPageView: View {
    let page: QuranPageItem
    
    var body: some View {
        VStack(spacing: 0) {
            // Top bar with surah name and juz
            HStack {
                // Juz name (right aligned)
                Text(page.juzoaNumberText ?? "")
                    .font(.custom("Naskh", size: 12))
                    .foregroundColor(.colorOnBackground)
                    .padding(.leading, 8)
                
                Spacer()
                
                // Surah name (left aligned)
                Text(page.surahName)
                    .font(.custom("Naskh", size: 12))
                    .foregroundColor(.colorOnBackground)
                    .padding(.trailing, 8)
            }
            .padding(.vertical, 2)
            .padding(.horizontal, 8)
            
            // Quran page image
            //TODO HANDLE
            Image(page.imageFilePath)
                .resizable()
                .scaledToFit()
                .padding(.top, 40)
            
            Spacer()
            
            // Page number at bottom
            Text(page.pageNumberLocalized)
                .font(.custom("Naskh", size: 16))
                .foregroundColor(.colorOnBackground)
                .padding(.bottom, 20)
        }
        .background(Color.white)
        .cornerRadius(8)
        .shadow(radius: 4)
        .padding(.horizontal, 16)
    }
}

struct OptionsPanelView: View {
    @Binding var selectedColor: ColorItem
    @Binding var showColorPicker: Bool
    
    let onSettingsTap: () -> Void
    let onShareTap: () -> Void
    let onBookmarksTap: () -> Void
    let onColorTap: () -> Void
    let onBookmarkTap: () -> Void
    let onZoomTap: () -> Void
    let onSurasTap: () -> Void
    
    var body: some View {
        VStack(spacing: 0) {
            // Color Picker (shown/hidden based on state)
            if showColorPicker {
                ColorPickerView(selectedColor: $selectedColor)
                    .transition(.move(edge: .top).combined(with: .opacity))
                    .padding(.top, 8)
                    .padding(.bottom, 24)
            }
            
            // Search Card
            SearchCardView(onTap: { print("Search tapped") })
                .padding(.horizontal, 30)
                .padding(.bottom, 24)
            
            // Action Buttons (horizontal spread layout)
            HStack(spacing: 8) {
                IconButton(icon: "ic_settings", action: onSettingsTap)
                IconButton(icon: "ic_share", action: onShareTap)
                IconButton(icon: "ic_bookmarks", action: onBookmarksTap)
                IconButton(icon: "ic_color", action: onColorTap)
                IconButton(icon: "ic_bookmark", action: onBookmarkTap)
                IconButton(icon: "ic_zoom", action: onZoomTap)
                IconButton(icon: "ic_article", action: onSurasTap)
            }
            .padding(.horizontal, 16)
            .padding(.bottom, 12)
        }
        .background(Color.colorPrimary)
    }
}

struct ColorPickerView: View {
    @Binding var selectedColor: ColorItem
    
    var body: some View {
        HStack(spacing: 0) {
            ColorCircle(color: .darkGray, colorItem: .dkgray, isSelected: selectedColor == .dkgray) {
                selectedColor = .dkgray
            }
            
            Spacer()
            
            ColorCircle(color: .beige, colorItem: .beige, isSelected: selectedColor == .beige) {
                selectedColor = .beige
            }
            
            Spacer()
            
            ColorCircle(color: .white, colorItem: .white, isSelected: selectedColor == .white) {
                selectedColor = .white
            }
            
            Spacer()
            
            ColorCircle(color: .darkBlue, colorItem: .dkblue, isSelected: selectedColor == .dkblue) {
                selectedColor = .dkblue
            }
        }
        .padding(.horizontal, 30)
    }
}

struct ColorCircle: View {
    let color: Color
    let colorItem: ColorItem
    let isSelected: Bool
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            Circle()
                .fill(color)
                .frame(width: 35, height: 35)
                .overlay(
                    Circle()
                        .stroke(Color.colorSecondary, lineWidth: isSelected ? 3 : 0)
                )
        }
    }
}

struct SearchCardView: View {
    let onTap: () -> Void
    
    var body: some View {
        Button(action: onTap) {
            HStack(spacing: 8) {
                Spacer()
                
                //TODO
                Text("SharedString.getString(string: Strings.Search)")
                    .font(.custom("Cairo-Regular", size: 16))
                    .foregroundColor(.onCard)
                
                Image("ic_search")
                    .resizable()
                    .frame(width: 16, height: 16)
                    .foregroundColor(.onCard)
                
                Spacer()
                    .frame(width: 8)
            }
            .padding(8)
            .background(Color.white)
            .cornerRadius(14)
            .shadow(radius: 8)
        }
    }
}

struct IconButton: View {
    let icon: String
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            Image(icon)
                .resizable()
                .scaledToFit()
                .frame(width: 24, height: 24)
                .foregroundColor(.white)
                .padding(.vertical, 16)
                .frame(maxWidth: .infinity)
        }
    }
}

#Preview {
    OptionsPanelScreen()
}
