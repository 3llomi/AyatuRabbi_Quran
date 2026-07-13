//
//  DownloadPage.swift
//  iosApp
//
//  Created by Devlomi on 13/07/2026.
//
import SwiftUI
import sharedKit

struct DownloadScreen: View {
    var body: some View {
        ZStack {
            VStack{
                Image("app_icon").resizable()
                    .scaledToFit() // Or .scaledToFill()
                    .frame(width: 200, height: 200)
                Text("Downloading..")
               
                Button("Cancel") {
                    
                }
                
                Button("Download"){
                    
                }
                
            }
        }.background(Color.gray)
        
    }
}

#Preview {
    DownloadScreen()
}
