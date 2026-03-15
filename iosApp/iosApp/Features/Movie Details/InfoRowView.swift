//
// Created by Aakash Mangukiya on 15/03/26.
//

import SwiftUI
import Shared

struct InfoRowView: View {

    let title: String
    let value: String

    var body: some View {

        VStack(alignment: .leading, spacing: 4) {

            Text(value)
                .font(.headline)

            Text(title)
                .font(.subheadline)
                .foregroundColor(.gray)
        }
    }
}