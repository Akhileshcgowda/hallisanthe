#!/usr/bin/env python3
"""
Generate Android app launcher icons from a source image.
Usage: python generate_app_icon.py <path_to_source_image>
"""

import sys
import os
from pathlib import Path
from PIL import Image

# Launcher icon sizes (48dp base)
SIZES = {
    "mipmap-mdpi": 48,
    "mipmap-hdpi": 72,
    "mipmap-xhdpi": 96,
    "mipmap-xxhdpi": 144,
    "mipmap-xxxhdpi": 192,
}


def make_square(img):
    """Crop image to a centered square."""
    w, h = img.size
    if w == h:
        return img
    min_dim = min(w, h)
    left = (w - min_dim) // 2
    top = (h - min_dim) // 2
    return img.crop((left, top, left + min_dim, top + min_dim))


def create_round_icon(img, size):
    """Create a circular masked icon."""
    img = img.resize((size, size), Image.LANCZOS)
    mask = Image.new("L", (size, size), 0)
    from PIL import ImageDraw
    draw = ImageDraw.Draw(mask)
    draw.ellipse((0, 0, size, size), fill=255)
    img = img.convert("RGBA")
    result = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    result.paste(img, (0, 0), mask)
    return result


def main():
    if len(sys.argv) < 2:
        print("Usage: python generate_app_icon.py <path_to_source_image>")
        print("Example: python generate_app_icon.py app_icon.png")
        sys.exit(1)

    source_path = sys.argv[1]
    if not os.path.exists(source_path):
        print("Error: File not found: " + source_path)
        sys.exit(1)

    img = Image.open(source_path)
    img = make_square(img)
    print("Source image squared to: " + str(img.size))

    res_dir = Path(__file__).parent / "app" / "src" / "main" / "res"

    # Replace adaptive icon foreground drawable with user image
    drawable_dir = res_dir / "drawable"
    fg_xml = drawable_dir / "ic_launcher_foreground.xml"
    if fg_xml.exists():
        fg_xml.unlink()
        print("Removed old: " + str(fg_xml))

    fg_png = drawable_dir / "ic_launcher_foreground.png"
    adaptive_img = img.resize((288, 288), Image.LANCZOS).convert("RGBA")
    adaptive_img.save(str(fg_png), "PNG")
    print("Saved adaptive foreground: " + str(fg_png))

    # Generate square launcher icons
    for folder, size in SIZES.items():
        out_dir = res_dir / folder
        out_dir.mkdir(parents=True, exist_ok=True)
        resized = img.resize((size, size), Image.LANCZOS)
        out_path = out_dir / "ic_launcher.webp"
        resized.save(str(out_path), "WEBP", quality=90, method=6)
        print("Saved: " + str(out_path))

    # Generate round launcher icons
    for folder, size in SIZES.items():
        out_dir = res_dir / folder
        out_dir.mkdir(parents=True, exist_ok=True)
        round_img = create_round_icon(img, size)
        out_path = out_dir / "ic_launcher_round.webp"
        round_img.save(str(out_path), "WEBP", quality=90, method=6)
        print("Saved: " + str(out_path))

    print("\nDone! App icons generated successfully.")
    print("Rebuild the app to see the new icon.")


if __name__ == "__main__":
    main()
