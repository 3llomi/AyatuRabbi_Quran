//
//  AppDelegate.swift
//  iosApp
//
//  Created by Devlomi on 18/07/2026.
//
import UIKit
import Firebase

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        FirebaseApp.configure()
        print("Application has successfully launched.")
        return true
    }
}
