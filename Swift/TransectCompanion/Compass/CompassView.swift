//
//  CompassView.swift
//  TransectCompanion
//
//  Created by Tyler Meester on 4/27/23.
//

import Foundation
import UIKit

class CompassView: UIView {
    var rotation: CGFloat = 0 {
        didSet {
            setNeedsDisplay()
        }
    }
    
    override func draw(_ rect: CGRect) {
        let context = UIGraphicsGetCurrentContext()
        
        context?.saveGState()
        context?.translateBy(x: rect.width / 2, y: rect.height / 2)
        context?.rotate(by: rotation * .pi / 180)
        context?.translateBy(x: -rect.width / 2, y: -rect.height / 2)
        
        let attributes: [NSAttributedString.Key: Any] = [
            .font: UIFont.systemFont(ofSize: 12),
            .foregroundColor: UIColor.black
        ]
        
        let north = NSAttributedString(string: "N", attributes: attributes)
        let south = NSAttributedString(string: "S", attributes: attributes)
        let east = NSAttributedString(string: "E", attributes: attributes)
        let west = NSAttributedString(string: "W", attributes: attributes)
        
        north.draw(at: CGPoint(x: rect.midX - (north.size().width / 2), y: 0))
        south.draw(at: CGPoint(x: rect.midX - (south.size().width / 2), y: rect.height - south.size().height))
        east.draw(at: CGPoint(x: rect.width - east.size().width, y: rect.midY - (east.size().height / 2)))
        west.draw(at: CGPoint(x: 0, y: rect.midY - (west.size().height / 2)))
        
        context?.restoreGState()
    }
}

