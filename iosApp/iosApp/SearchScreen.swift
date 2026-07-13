//
//  SearchScreen.swift
//  iosApp
//
//  Created by Devlomi on 13/07/2026.
//

import SwiftUI

struct SearchScreen: View {
    var body: some View {
        ZStack{
            VStack{
                TextField(text: .constant("s")) {
                    Text("Search")
                }
                
                LazyVStack{
                    
                }
            }
        }
        
    }
}

#Preview {
    SearchScreen()
}
struct SearchItem:View{
    var body:some View{
        ZStack{
            VStack{
                
            }
        }
    }
}
