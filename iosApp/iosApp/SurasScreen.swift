//
//  SurasPage.swift
//  iosApp
//
//  Created by Devlomi on 13/07/2026.
//

import SwiftUI

struct SurasScreen: View {
    var body: some View {
        ZStack{
            VStack{
                TextField(text: .constant("s")) {
                    Text("Search")
                }
                HStack{
                    Button("Go To Page"){
                        
                    }
                    Button("Go To Juzoa"){
                        
                    }
                }
                LazyVStack {
                    
                }
            }
        }
    }
}

#Preview {
    SurasScreen()
}

private struct SurahItem:View{
    var body:some View {
        HStack{
            ZStack{
                Text("1")
            }.background(Color.green)
            
            Text("AlFatiha")
        }
    }
}
