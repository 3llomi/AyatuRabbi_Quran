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
import ZIPFoundation


final class DownloadViewModelSw:ObservableObject{
    private let repository = KoinKt.getDownloadRepository()
    
    private var downloadTaskHandle:Task<(), Never>?
    @Published
    var downloadResourceState:DownloadingResource = .None()
    
    init() {
        let task = Task{
            do {
                let sequence = asyncSequence(for: repository.downloadResourceFlow)
                for try await result in sequence {
                    downloadResourceState = result
                }
            }catch{
                
            }
        }
    }
    
    func download(width:Int,path:String){
        //TODO DELETE OLD FILE?
        downloadTaskHandle?.cancel()
        downloadTaskHandle = Task {
            do {
                try await asyncFunction(for: repository.download(width:Int32(width),path:path))
                let fm = FileManager.default
                let temp = fm.temporaryDirectory.appending(path: "quran_data")
                
                do {
                    try copyFiles(temp: temp, width: width)
                    //todo delete temp files
                    
                }catch let error{
                    downloadResourceState = DownloadingResource.Error(e: KotlinException(message: error.localizedDescription))
                    print("Error Unzippping")
                }
                print("Downlaod Success")
            } catch {
                print("Failed with error: \(error)")
            }
        }
    }
    
    private func copyFiles(temp: URL, width: Int) throws {
        let fm = FileManager.default
        let cwd = URL(fileURLWithPath: fm.currentDirectoryPath)//TODO IS THIS OK?
        func copyAyahInfoNameDb() throws {
            let src = URL(fileURLWithPath: temp.appending(path: DBFileNames.shared.ayahInfoNameDbPath(width: Int32(width))).path())
            let dest = URL(fileURLWithPath: cwd.appending(path: DBFileNames.shared.ayahInfoNameDbPath(width: Int32(width))).path())
            if fm.fileExists(atPath: dest.path){
                try fm.removeItem(at: dest)
            }
            try fm.copyItem(at: src, to: dest)
        }
        
        
        func copyQuranDbNameDb() throws {
            let src = URL(fileURLWithPath: temp.appending(path: DBFileNames.shared.quranDbPath).path())
            let dest = URL(fileURLWithPath: cwd.appending(path: DBFileNames.shared.quranDbPath).path())
            if fm.fileExists(atPath: dest.path){
                try fm.removeItem(at: dest)
            }
            try fm.copyItem(at: src, to: dest)
        }
        
        
        
        func copyImages() throws {
            let quranImagesDest = cwd.appending(path: "quran_images")
            try fm.removeItem(atPath: quranImagesDest.path())
            let src = URL(fileURLWithPath: temp.appending(path: "width_\(width)").path())
            let dest = URL(fileURLWithPath: quranImagesDest.path())
            if fm.fileExists(atPath: dest.path){
                try fm.removeItem(at: dest)
            }
            try fm.copyItem(at: src, to: dest)
        }
        
        
        do {
            try copyAyahInfoNameDb()
            try copyQuranDbNameDb()
            try copyImages()
        }catch let e{
            throw e
        }
        
        
        
        
        
    }
}
