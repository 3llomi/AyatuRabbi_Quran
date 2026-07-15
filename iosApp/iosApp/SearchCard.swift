//
//  SearchCard.swift
//  iosApp
//
//  Created by Devlomi on 15/07/2026.
//

import SwiftUI

struct SearchCard: View {
    @Binding var text: String
    
    var body: some View {
        HStack(spacing: 12) {
            TextField("Search for surah", text: $text)
                .font(.system(size: 16))
                .foregroundColor(.black)
            
            Image(systemName: "magnifyingglass")
                .foregroundColor(.gray)
        }
        .padding(16)
        .background(Color.white)
        .cornerRadius(14)
        .shadow(radius: 4)
    }
}
