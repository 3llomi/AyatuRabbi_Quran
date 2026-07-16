//
//  SearchScreen.swift
//  iosApp
//
//  Created by Devlomi on 13/07/2026.
//

import SwiftUI
import sharedKit
import KMPObservableViewModelSwiftUI

struct SearchScreen: View {
    @State private var searchText: String = ""

    @StateViewModel var viewModel = KoinKt.getSearchViewModel()
    
    var body: some View {
        ZStack {
            Color.colorPrimary
                .ignoresSafeArea()
            
            VStack(spacing: 0) {
                // Search Card
                VStack {
                    SearchCard(text: $searchText).onChange(of: searchText) {
                        viewModel.searchForAyah(query:searchText)
                    }
                }
                
                // Search Results or Logo
                if searchText.isEmpty {
                    VStack {
                        Spacer()
                        Image("ic_quran_logo")
                            .resizable()
                            .scaledToFit()
                            .frame(width: 150, height: 150)
                            .opacity(0.7)
                        Spacer()
                    }
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                } else {
                    LazyVStack {
                        //TODO USE A REAL ID
                        ForEach(viewModel.searchResults,id:\.ayahNumber) { result in
                            SearchResultItemView(result: result)
                                .listRowBackground(Color.colorPrimary)
                                .listRowSeparator(.hidden)
                                .listRowInsets(EdgeInsets()).onTapGesture {
                                    //TODO NAVIGATE
                                }
                        }
                    }
                    .listStyle(.plain)
                    .scrollContentBackground(.hidden)
                    .background(Color.colorPrimary)
                }
            }
        }
    }
}

struct SearchResultItemView: View {
    let result: SearchResult
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            // Ayah Text
            Text(result.highlightedText)
                .font(.custom("Cairo-Regular", size: 16))
                .foregroundColor(.white)
                .lineLimit(3)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.vertical, 8)
            
            // Details Row (Page Number, Ayah Number, Surah Name)
            HStack(spacing: 0) {
                // Page Number (Left)
                HStack(spacing: 8) {
                    Image("ic_article")
                        .resizable()
                        .frame(width: 16, height: 16)
                        .foregroundColor(.white)
                    
                    Text("\(result.pageNumber)")
                        .font(.custom("Cairo-Regular", size: 13))
                        .foregroundColor(.colorOnSecondary)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                
                // Ayah Number (Center)
                HStack(spacing: 8) {
                    Image("ic_star_ayah")
                        .resizable()
                        .frame(width: 16, height: 16)
                        .foregroundColor(.white)
                    
                    Text("\(result.ayahNumber)")
                        .font(.custom("Cairo-Regular", size: 13))
                        .foregroundColor(.colorOnSecondary)
                }
                .frame(maxWidth: .infinity, alignment: .center)
                
                // Surah Name (Right)
                HStack(spacing: 8) {
                    Image("ic_reading_quran")
                        .resizable()
                        .frame(width: 16, height: 16)
                        .foregroundColor(.white)
                    
                    Text(result.surahName)
                        .font(.custom("Cairo-Regular", size: 13))
                        .foregroundColor(.colorOnSecondary)
                }
                .frame(maxWidth: .infinity, alignment: .trailing)
            }
            .padding(.top, 8)
        }
        .padding(.horizontal, 8)
        .padding(.vertical, 8)
        .background(Color.colorPrimary)
        .cornerRadius(4)
    }
}

#Preview {
    SearchScreen()
}
