//
//  DownloadRepository.swift
//  iosApp
//
//  Created by Devlomi on 18/07/2026.
//
import SwiftUI
import FirebaseStorage
import sharedKit

class DownloadRepository{
    @Published
    var state:DownloadingResource = DownloadingResource.None()
    private var task:StorageDownloadTask?=nil
    private var file:URL?=nil
    
    private func deleteFile(){
        if let file = file {
            try? FileManager.default.removeItem(at: file)
        }
    }
    func cancel()  {
        task?.cancel()
        deleteFile()
    }
    
    func download(width:Int,file:URL,completion: @escaping () -> Void){
        task?.cancel()
        deleteFile()
        let ref = Storage.storage().reference(withPath: "quran_files/data_\(width).zip")
        
        self.file = file
        print("downloading to path \(file.pathCompat())")
        task = ref.write(toFile: file) { _, error in
            if let error = error{
                print("error downloading \(error.localizedDescription)")
                self.state = DownloadingResource.Error(e: KotlinException(message: error.localizedDescription))
                self.deleteFile()
            }else{
                print("Download finished successfully")
                self.state = DownloadingResource.Success()
                completion()
            }
        }
        task?.observe(.progress, handler: { snapshot in
            guard let progress = snapshot.progress else { return }
            let percentComplete = Double(progress.completedUnitCount) / Double(progress.totalUnitCount)
            print("progress \(percentComplete)")
            
            // Update your UI on the main thread
            DispatchQueue.main.async {
                self.state = DownloadingResource.Loading(progress: Int32(percentComplete))
            }
            
        })
        
        
        
        
    }
    
}
