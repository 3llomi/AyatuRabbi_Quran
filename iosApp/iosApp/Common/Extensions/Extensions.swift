//
//  Extensions.swift
//  iosApp
//
//  Created by Devlomi on 15/07/2026.
//

// Helper extension to get app version
import Foundation

extension Bundle {
    var appVersion: String {
        let version = infoDictionary?["CFBundleShortVersionString"] as? String ?? "1.0.0"
        let build = infoDictionary?["CFBundleVersion"] as? String ?? "1"
        return "\(version) (Build \(build))"
    }
}
