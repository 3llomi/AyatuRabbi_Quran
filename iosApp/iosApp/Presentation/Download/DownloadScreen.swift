//
//  DownloadPage.swift
//  iosApp
//
//  Created by Devlomi on 13/07/2026.
//
import SwiftUI
import sharedKit
import KMPObservableViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct DownloadScreen: View {
    @StateObject private var viewModel = DownloadViewModelSw()
    @StateViewModel private var viewModelKt:DownloadViewModel = KoinKt.getDownloadViewModel()



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
                if  viewModel.downlaodState is DownloadingResource.Loading{
                    
                    
                    Text("Downloading")
                        .font(.system(size: 25, weight: .medium))
                        .foregroundColor(Color(red: 0.2, green: 0.2, blue: 0.2)) // colorOnBackground
                        .multilineTextAlignment(.center)
                    
                    
                    // Progress Bar
                    
                    let loading = viewModel.downlaodState as! DownloadingResource.Loading
                    
                    ProgressView(value: Float(loading.progress))
                            .tint(Color(red: 0.2, green: 0.6, blue: 0.8)) // colorSecondary
                            .frame(height: 13)
                            .padding(.horizontal, 32)
                            .background(Color.white)
                            .cornerRadius(16)
                    
                    Button(action: {
                        //TODO Find a way to unify both VMs (Swift and Kt)
                        viewModel.stopDownload()
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
                
                // Download Button
                if viewModel.downlaodState is DownloadingResource.Error{
                    
                    Text("Error")
                        .font(.system(size: 25, weight: .medium))
                        .foregroundColor(Color(red: 0.2, green: 0.2, blue: 0.2)) // colorOnBackground
                        .multilineTextAlignment(.center)
                    
                   
                    
                    Button(action: {
                        viewModelKt.startDownloading()//TODO SHOW DIALOG instead
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

        

                Spacer()
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .onChange(of: viewModel.downlaodFinished){
                if viewModel.downlaodFinished{
                    viewModelKt.downloadFinished()
                }
            }
            .onAppear {
                viewModelKt.setDeviceWidth(deviceWidthPixels: 1280)//TODO
                viewModelKt.startDownloading()//TODO SHOW DIALOG instead
                
                Task {
                    do {
                        let downlaodChannelResult = asyncSequence(for: viewModelKt.startDownloadEvent)
                        
                        for try await result in downlaodChannelResult {
                            //TODO REAL PATH
                            let documentsURL = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
                            let file = documentsURL.appendingPathComponent("data.zip")
                            
                            
                            self.viewModel.download(width: Int(result), file:file)
                        }
                    } catch {
                        print("Failed with error: \(error)")
                    }
                }
                
                Task {
                    do {
                        let navigationResult = asyncSequence(for: viewModelKt.navigationEvent)
                        
                        for try await result in navigationResult {
                            //TODO NAVIGATE
                            print("Navigating to Next")
                        }
                    } catch {
                        print("Failed with error: \(error)")
                    }
                }
            }
        }
    }
}

#Preview {
    DownloadScreen()
}
