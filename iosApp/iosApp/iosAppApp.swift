//
//  iosAppApp.swift
//  iosApp
//
//  Created by Devlomi on 13/07/2026.
//

import SwiftUI
import sharedKit

@main
struct iosAppApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    init() {
        KoinKt.doInitKoinIos()
      }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
