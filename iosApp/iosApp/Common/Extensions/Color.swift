//
//  Color.swift
//  iosApp
//
//  Created by Devlomi on 15/07/2026.
//
import SwiftUI

func hexToColor(_ hex: String) -> Color {
    let hex = hex.trimmingCharacters(in: CharacterSet(charactersIn: "#"))
    let scanner = Scanner(string: hex)
    var rgb: UInt64 = 0
    scanner.scanHexInt64(&rgb)
    
    let red = Double((rgb >> 16) & 0xFF) / 255.0
    let green = Double((rgb >> 8) & 0xFF) / 255.0
    let blue = Double(rgb & 0xFF) / 255.0
    
    return Color(red: red, green: green, blue: blue)
}


// MARK: - Color Extensions
extension Color {
    init(hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet(charactersIn: "#"))
        let scanner = Scanner(string: hex)
        var rgb: UInt64 = 0
        scanner.scanHexInt64(&rgb)
        
        let red = Double((rgb >> 16) & 0xFF) / 255.0
        let green = Double((rgb >> 8) & 0xFF) / 255.0
        let blue = Double(rgb & 0xFF) / 255.0
        
        self.init(red: red, green: green, blue: blue)
    }
    
    // Primary Colors
    static let colorPrimary = hexToColor("#0F3250")
    static let colorPrimaryVariant = hexToColor("#0B263C")
    
    // Secondary Colors
    static let colorSecondary = hexToColor("#1CAD91")
    static let colorSecondaryVariant = hexToColor("#16856F")
    
    // Content Colors
    static let colorOnPrimary = hexToColor("#98ABBE")
    static let colorOnSecondary = hexToColor("#FFFFFF")
    static let colorOnBackground = hexToColor("#FFFFFF")
    static let colorOnError = hexToColor("#C50D28")
    static let onCard = hexToColor("#ADADAD")
    
    // Utility Colors
    static let beige = hexToColor("#F5F5DC")
    static let black = hexToColor("#000000")
    static let white = hexToColor("#FFFFFF")
    static let darkBlue = hexToColor("#0C2942")
    static let darkGray = hexToColor("#8F8F8F")
    static let bgPanel = hexToColor("#264B6A")
}
