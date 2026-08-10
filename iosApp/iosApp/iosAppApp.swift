//
//  iosAppApp.swift
//  iosApp
//
//  Created by Devlomi on 13/07/2026.
//

import SwiftUI
import Shared

@main
struct iosAppApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    init() {
        KoinInitIOSKt.doInitKoinIos()
        InitQuranPageDataSourceKt.doInitQuranPageDataSource()
      }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
