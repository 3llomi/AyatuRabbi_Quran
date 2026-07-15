//
//  DownloadViewModel.swift
//  iosApp
//
//  Created by Devlomi on 15/07/2026.
//
import SwiftUI
import Observation
import sharedKit
import KMPNativeCoroutinesAsync

@Observable
final class DownloadViewModel{
    private let repository = KoinKt.getDownloadRepository()
//    let downloadStte = repository.downloadResource
    
    func download(width:Int,path:String){
        let handle = Task {
            do {
                let result: Void = try await asyncFunction(for: repository.download(width:Int32(width),path:path))
                print("Downlaod Success")
            } catch {
                print("Failed with error: \(error)")
            }
        }
    }
}
