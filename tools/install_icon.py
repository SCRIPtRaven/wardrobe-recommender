"""Download a Material Symbols Rounded icon into app/src/main/res/drawable.

The icon comes from github.com/google/material-design-icons (Apache License 2.0).
The script drops the android:tint attribute, which refers to an AppCompat theme
attribute this app does not have. Compose tints icons itself.

Run from anywhere:
    python3 tools/install_icon.py delete            -> ic_delete.xml
    python3 tools/install_icon.py settings --filled -> ic_settings_filled.xml
"""

import argparse
import urllib.request
from pathlib import Path

BASE = "https://raw.githubusercontent.com/google/material-design-icons/master/symbols/android"
DRAWABLE = Path(__file__).resolve().parent.parent / "app/src/main/res/drawable"
NOTE = "<!-- Material Symbols Rounded, Apache License 2.0, https://github.com/google/material-design-icons -->\n"


def main():
    parser = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    parser.add_argument("name", help="Material Symbols icon name, for example calendar_month")
    parser.add_argument("--filled", action="store_true", help="install the filled variant")
    args = parser.parse_args()

    variant = "_fill1" if args.filled else ""
    url = f"{BASE}/{args.name}/materialsymbolsrounded/{args.name}{variant}_24px.xml"
    with urllib.request.urlopen(url, timeout=30) as response:
        text = response.read().decode("utf-8")

    if not text.lstrip().startswith("<vector"):
        raise SystemExit(f"{url} is not a vector drawable")
    text = text.replace('\n    android:tint="?attr/colorControlNormal"', "")
    if "?attr" in text:
        raise SystemExit(f"{url} still refers to a theme attribute")

    dest = DRAWABLE / f"ic_{args.name}{'_filled' if args.filled else ''}.xml"
    dest.write_text('<?xml version="1.0" encoding="utf-8"?>\n' + NOTE + text.lstrip())
    print(dest.relative_to(DRAWABLE.parent.parent.parent.parent.parent))


if __name__ == "__main__":
    main()
