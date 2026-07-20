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
    private let repository = DownloadRepository()
    
    private var downloadTaskHandle:Task<(), Never>?
    
    var downlaodState:DownloadingResource{
        return repository.state
    }
    
    @Published
    var downlaodFinished = false
    
    init() {
//        let task = Task{
//            do {
//                let sequence = asyncSequence(for: repository.downloadResourceFlow)
//                for try await result in sequence {
//                    downloadResourceState = result
//                }
//            }catch{
//                
//            }
//        }
    }
    func stopDownload(){
        repository.cancel()
    }
    
    func download(width:Int,file:URL){
        print("file path \(file)")
        repository.download(width: width, file: file){
            let fm = FileManager.default
        let temp = fm.temporaryDirectory.appendingPathComponent(path: "quran_data")
            do{
                try? fm.removeItem(at: temp)
                try fm.unzipItem(at: file, to: temp)
                print("File Unzipped")
                try self.copyFiles(temp: temp, width: width)
                self.downlaodFinished = true
                
            }catch let error {
                print("error unzipping \(error.localizedDescription)")
                //TODO UPDATE WITH ERROR
            }
            

            
        }
        //TODO DELETE OLD FILE?
//        downloadTaskHandle?.cancel()
//        downloadTaskHandle = Task {
//            do {
//                try await asyncFunction(for: repository.download(width:Int32(width),path:path))
//                let fm = FileManager.default
//                let temp = fm.temporaryDirectory.appending(path: "quran_data")
//                
//                do {
//                    try copyFiles(temp: temp, width: width)
//                    //todo delete temp files
//                    
//                }catch let error{
//                    downloadResourceState = DownloadingResource.Error(e: KotlinException(message: error.localizedDescription))
//                    print("Error Unzippping")
//                }
//                print("Downlaod Success")
//            } catch {
//                print("Failed with error: \(error)")
//            }
//        }
    }
    
    private func copyFiles(temp: URL, width: Int) throws {
        let fm = FileManager.default
        let cwd = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
          
//        let cwd = URL(fileURLWithPath: fm.currentDirectoryPath)//TODO IS THIS OK?
        func copyAyahInfoNameDb() throws {
            let src = URL(fileURLWithPath: temp.appendingPathComponent(path: DBFileNames.shared.ayahInfoNameDbPath(width: Int32(width))).pathCompat())
            let dest = URL(fileURLWithPath: cwd.appendingPathComponent(path: DBFileNames.shared.ayahInfoNameDbPath(width: Int32(width))).pathCompat())
            try? fm.removeItem(at: dest)
            print("srcPath \(src.pathCompat()) - exists? \(fm.fileExists(atPath: src.pathCompat()))")
            print("destPath \(dest.pathCompat())")
            try fm.createDirectory(at: dest, withIntermediateDirectories: true)
            if fm.fileExists(atPath: dest.path){
                try fm.removeItem(at: dest)
            }
            try fm.copyItem(at: src, to: dest)
        }
        
        
        func copyQuranDbNameDb() throws {
            let src = URL(fileURLWithPath: temp.appendingPathComponent(path: DBFileNames.shared.quranDbPath).pathCompat())
            let dest = URL(fileURLWithPath: cwd.appendingPathComponent(path: DBFileNames.shared.quranDbPath).pathCompat())
            if fm.fileExists(atPath: dest.path){
                try fm.removeItem(at: dest)
            }
            try fm.copyItem(at: src, to: dest)
        }
        
        
        
        func copyImages() throws {
            let quranImagesDest = cwd.appendingPathComponent(path: "quran_images")
            try? fm.removeItem(atPath: quranImagesDest.pathCompat())
            try fm.createDirectory(atPath: quranImagesDest.pathCompat(), withIntermediateDirectories: true)
            let src = URL(fileURLWithPath: temp.appendingPathComponent(path: "width_\(width)").pathCompat())
            let dest = URL(fileURLWithPath: quranImagesDest.pathCompat())
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
