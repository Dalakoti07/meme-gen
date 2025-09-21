# Meme Generator

Just generate Meme

## How to build the project?

Build the project in studio or write following commands
Open terminal and type the below command to generate debug build below are commands for Linux and Mac OS users
```
./gradlew assembleDebug
```

To build and install the app in running emulator
```
./gradlew installDebug
```


# Meme generator
- Functional Requirements
  - pick meme template from your device
  - should be as simple and sublte as 
    - snapseed
    - Image toolbox
      - Read codebase
  - put emojis
  - Share Emojis

- Non functional
  - State of art android practises
  - Use SDP for compose
  - Linter
  - CI CD using fastlane


# Todos
- 
- [ ] simple interface like snapseed
  - [ ] A blank page with + icon in center
  - [ ] Appbar at top
  - [ ] Editing screen like snapseed
    - [ ] One Main Menu
      - [ ] Edit
      - [ ] Export
        - [ ] Storage
        - [ ] Clipboard
        - [ ] Share
    - [ ] Looks
      - [ ] Different tints and styles
    - [ ] Edit Menu Details
      - [ ] Main Menu
        - [ ] Text
          - [ ] Text Colors
          - [ ] Text Categories and Fonts
          - [ ] Opacity of Text
        - [ ] Crop
        - [ ] Tint
        - [ ] Rotate
        - [ ] Frames
        - [ ] Emoji

# Fastlane
Gradle: pass -PreleaseDebuggable=true to make release debuggable.

Fastlane:
android build_release: builds Release with optional debuggable.
android upload_internal: builds and uploads AAB to Play internal track using GOOGLE_PLAY_JSON_KEY.

