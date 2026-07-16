//
//  SurasPage.swift
//  iosApp
//
//  Created by Devlomi on 13/07/2026.
//

import SwiftUI
import sharedKit
import KMPObservableViewModelSwiftUI

struct SurasScreen: View {
    @StateViewModel var viewModel = KoinKt.getSurasViewModel()
    @State private var searchText = ""
    @State private var showGoToPageDialog = false
    @State private var pageNumberDialog = ""
    
    var body: some View {
        ZStack {
            Color(UIColor(red: 0.10, green: 0.12, blue: 0.20, alpha: 1.0))
                .ignoresSafeArea()
            
            VStack(spacing: 0) {
                // Search Card
                SearchCard(text: $searchText)
                    .padding(.horizontal, 32)
                    .padding(.top, 24)
                    .onChange(of: searchText) {
                        viewModel.searchForSura(query: searchText)
                    }
                
                // Navigation Buttons
                HStack(spacing: 8) {
                    Button(action: {
                        //TODO SHOW DIALOG
                        showGoToPageDialog = true
                    }) {
                        Label("Go To Page", systemImage: "doc.text")
                            .font(.system(size: 14, weight: .medium))
                    }
//                    .buttonStyle(.filled)
                    .tint(Color(UIColor(red: 1.0, green: 0.65, blue: 0.0, alpha: 1.0)))
                    .foregroundColor(.white)
                    
                    Button(action: {}) {
                        Label("Go To Juzoa", systemImage: "star.fill")
                            .font(.system(size: 14, weight: .medium))
                    }
//                    .buttonStyle(.filled)
                    .tint(Color(UIColor(red: 1.0, green: 0.65, blue: 0.0, alpha: 1.0)))
                    .foregroundColor(.white)
                }
                .padding(.top, 16)
                .padding(.horizontal, 32)
                
                // Suras List
                ScrollView {
                    LazyVStack(spacing: 0) {
                        ForEach(viewModel.surasState, id: \.surahNumber) { surah in
                            
                            SurahItemView(number: Int(surah.surahNumber), name: surah.surahName)
                                .frame(maxWidth: .infinity, alignment: .leading)
                        }
                    }
                }
                .padding(.top, 28)
                
                Spacer()
            }.alert("Enter Name", isPresented: $showGoToPageDialog) {
                TextField("Type here...", text: $pageNumberDialog)
                
                Button("Cancel", role: .cancel) { }
                
                Button("Go") {
                    if viewModel.isPageNumberValid(page: pageNumberDialog){
                        //TODO NAVIGATE
                    }
                }
            } message: {
                Text("Please enter your name below.")
            }
        }
    }
}



// MARK: - Surah Item
private struct SurahItemView: View {
    let number: Int
    let name: String
    
    var body: some View {
        HStack(spacing: 16) {
            VStack {
                Text(String(number))
                    .font(.system(size: 14, weight: .semibold))
                    .foregroundColor(.white)
            }
            .frame(width: 35, height: 35)
            .background(
                Circle()
                    .fill(Color(UIColor(red: 0.6, green: 0.3, blue: 0.8, alpha: 1.0)))
            )
            .padding(.trailing, 8)
            
            Text(name)
                .font(.system(size: 18, weight: .regular))
                .foregroundColor(.white)
            
            Spacer()
        }
        .padding(16)
        .contentShape(Rectangle())
    }
}

#Preview {
    SurasScreen()
}
