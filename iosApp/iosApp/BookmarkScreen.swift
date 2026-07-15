//
//  BookmarkScreen.swift
//  iosApp
//
//  Created by Devlomi on 15/07/2026.
//
import SwiftUI
import sharedKit
import KMPObservableViewModelSwiftUI

struct BookmarksScreen: View {
    @StateViewModel var viewModel = KoinKt.getBookmarksViewModel()
    
    var body: some View {
        ZStack {
            Color.colorPrimary
                .ignoresSafeArea()
            
            VStack(spacing: 0) {
                // Title
                HStack {
                    Image("ic_bookmarks")
                        .resizable()
                        .frame(width: 24, height: 24)
                    //KoinKt.getSharedString().getString(string: Strings.SavedBookmarks.shared)
                    //TODO USE SHARED STRING
                    Text("Saved Bookmarks")
                        .font(.custom("Cairo-Bold", size: 16))
                        .foregroundColor(.white)
                }
                .padding(.top, 16)
                .frame(maxWidth: .infinity, alignment: .center)
                
                // Bookmarks List
                LazyVStack {
                    //TODo USE A REAL ID INSTEAD OF THE NOTE
                    ForEach(viewModel.bookmarks, id:\.note) { bookmark in
                        BookmarkItemView(bookmark: bookmark) {
                            viewModel.onDeleteClick(bookmark:bookmark)
                        }
                        .listRowBackground(Color.colorPrimary)
                        .listRowSeparator(.hidden)
                        .listRowInsets(EdgeInsets())
                    }
                }
                .listStyle(.plain)
                .scrollContentBackground(.hidden)
                .background(Color.colorPrimary)
                .onAppear{
                    
                }
            }
        }
    }
    
}

struct BookmarkItemView: View {
    let bookmark: Bookmark
    let onDelete: () -> Void
    @State private var showDeleteConfirmation = false
    
    var body: some View {
        VStack(alignment: .trailing, spacing: 0) {
            // Surah Name
            HStack(spacing: 8) {
                Image("ic_reading_quran")
                    .resizable()
                    .frame(width: 20, height: 20)
                
                Text(bookmark.surahName)
                    .font(.custom("Cairo-Black", size: 14))
                    .foregroundColor(.white)
            }
            .frame(maxWidth: .infinity, alignment: .trailing)
            .padding(.trailing, 16)
            .padding(.top, 8)
            
            // Page Number
            HStack(spacing: 8) {
                Image("ic_article")
                    .resizable()
                    .frame(width: 20, height: 20)
                
                Text("\(bookmark.pageNumber)")
                    .font(.custom("Cairo-Regular", size: 13))
                    .foregroundColor(.colorOnSecondary)
            }
            .frame(maxWidth: .infinity, alignment: .trailing)
            .padding(.top, 8)
            .padding(.trailing, 16)
            
            if let note = bookmark.note{
                // Note
                HStack(spacing: 8) {
                    Image("ic_note")
                        .resizable()
                        .frame(width: 20, height: 20)
                    
                    Text(note)
                        .font(.custom("Cairo-Regular", size: 13))
                        .foregroundColor(.white)
                }
                .frame(maxWidth: .infinity, alignment: .trailing)
                .padding(.top, 8)
                .padding(.trailing, 16)
            }
            // Date (left aligned)
            //TODO USE REAL DATE
            //TODO COPY FONT
            Text("bookmark.timestamp")
                .font(.custom("Cairo-Regular", size: 13))
                .foregroundColor(.white)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.leading, 16)
                .padding(.top, 8)
            
            // Delete Button
            Button(action: {
                showDeleteConfirmation = true
            }) {
                HStack(spacing: 8) {
                    //TODO
                    Text("SharedString.getString(string: Strings.Delete)")
                        .font(.custom("Cairo-Regular", size: 14))
                    
                    Image("ic_clear")
                        .resizable()
                        .frame(width: 18, height: 18)
                }
                .foregroundColor(.white)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 10)
                .padding(.horizontal, 32)
                .background(Color.colorSecondary)
                .cornerRadius(4)
            }
            .padding(.horizontal, 32)
            .padding(.top, 16)
            .padding(.bottom, 8)
            .confirmationDialog(
                "Delete Bookmark",
                isPresented: $showDeleteConfirmation,
                presenting: bookmark
            ) { _ in
                Button("Delete", role: .destructive) {
                    onDelete()
                }
            } message: { _ in
                Text("Are you sure you want to delete this bookmark?")
            }
        }
        .background(Color.colorPrimary)
        .cornerRadius(8)
        .padding(.vertical, 4)
    }
}

#Preview {
    BookmarksScreen()
}
