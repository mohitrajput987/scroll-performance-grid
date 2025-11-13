# Scroll Performance Grid

A compact Android demo that renders **10,000 square cells** laid out in rows.  
Each cell is a 32dp × 32dp square with a randomly chosen background color and shows its zero-based index. Rows contain a random number (1–100) of cells. Tapping a cell removes it; if a row becomes empty the row is removed. The app is implemented with Kotlin and Android Views (XML) and targets Android SDK 24+.

---

## Features

- **10,000 items** rendered as square cells (32dp × 32dp).
- **Rows with random widths**: each row contains between 1 and 100 squares (randomized at startup).
- **Random background color** per cell.
- **Zero-based index** shown centered in each square.
- **Tap to delete** a cell; empty rows are removed automatically.
- **Smooth scrolling** both vertically and horizontally.
- **State persistence on rotation** (scroll position, remaining items, and layout preserved).
- Implemented using **Kotlin + XML** only (no Jetpack Compose, no external libraries).
- Compatible with **Android SDK 24 (Nougat)** and above.

---

## Demo Video

📹 **Demo video** (shows app behavior and interactions):  

<img width="480" height="852" alt="Screenshot_20251113_173637" src="https://github.com/user-attachments/assets/9f5753bd-8936-4189-bf31-1f17d6786266" />




<video src="https://raw.githubusercontent.com/mohitrajput987/scroll-performance-grid/main/app/src/main/assets/app-performance.mp4" 
       controls 
       width="300">
  Your browser does not support the video tag.
</video>


---

## Performance & Profiler Screenshots

📈 **Profiler screenshots** (showing frame times, CPU/memory, overdraw):  
<img src="https://github.com/mohitrajput987/scroll-performance-grid/blob/main/app/src/main/assets/profiler.png">
---

## How it works — high level

1. **Data Model**
   - Creates a flat model of rows. Each row has a list of cell items.
   - Total number of cells across all rows equals 10,000 at startup.

2. **Layout**
   - Uses a single `RecyclerView` with a `LinearLayoutManager`-style approach:
     - Each *row* is represented by one RecyclerView item (a row container).
     - The row container hosts a single horizontally-scrollable `RecyclerView` (or a `HorizontalScrollView` with recycled child views), which renders the cells for that row.
   - This approach keeps recyclable viewholders small and enables efficient recycling per row and per cell.

3. **Cell**
   - Each cell is a lightweight view inflated from XML with:
     - Fixed dimensions (32dp × 32dp).
     - A `TextView` centred for index display.
     - Background color set at bind time.

4. **Deletion**
   - On cell click: the item is removed from the row's list and `notifyItemRemoved()` (or `DiffUtil` update) is invoked.
   - If a row becomes empty, that row is removed from the top-level adapter.

5. **State Persistence**
   - Uses `ViewModel` to hold the dataset across configuration changes.
   - `RecyclerView` scroll position is restored from its `LayoutManager` state.

---

## Implementation notes & design decisions

- **Kotlin + XML only:** Project avoids Java and any third-party libraries (image loaders, binding libs, etc.) to keep dependencies minimal and maximize portability.
- **RecyclerView-based design:** Using `RecyclerView` for both vertical and horizontal lists (row container + cell rows) leverages view recycling and minimizes view inflation and rebind overhead.
- **Stable IDs & Diffing:** Stable IDs and `DiffUtil` (or targeted `notifyItemXXX` calls) are used to minimize unnecessary rebinds and layout passes when items are removed.
- **Fixed-size items:** Each cell has fixed `layout_width` and `layout_height` (32dp). This helps the layout manager optimize measurement and reduces remeasure cost.
- **Avoid heavy work on main thread:** All non-UI work (data generation, random color creation list population) is done off the UI thread during app startup (or incrementalized) to avoid jank.
- **Lightweight view holders:** Cell view holders contain only the minimal views and avoid nested deep hierarchies to reduce overdraw and measure passes.
- **Memory usage:** The app keeps only the minimal data structures required. No large bitmaps or caches are held in memory.
- **Rotation handling:** Data is stored in a ViewModel; RecyclerView state is saved and restored to preserve the user's scroll positions and remaining items.

---

## Build & Run

1. Clone the repository:
   ```
   git clone https://github.com/mohitrajput987/scroll-performance-grid.git
   cd scroll-performance-grid
   ```

2. Open the project in **Android Studio** (Arctic Fox / Bumblebee or newer recommended, but project compiles with Android Gradle Plugin compatible with API 24).

3. Build and run on a device or emulator (API 24+ recommended). For realistic profiling, run on a physical device.

---

## Performance testing checklist

If you want to reproduce the performance measurements:

1. Use **Android Studio Profiler**:

    * Record CPU during fast scrolling to analyze main thread activity.
    * Record Memory to check allocations and retention.
    * Use **Profile GPU Rendering** (Developer Options) to track frame times.

2. Check for dropped frames / jank:

    * Ensure the onMainThread time per frame is under ~16ms for 60 FPS.
    * Verify that expensive operations are offloaded from the UI thread.

3. Verify rotation persistence:

    * Rotate the device and confirm grid state and scroll positions are preserved.

---

## Files of interest

* `app/src/main/java/.../MainActivity.kt` — Entry point and RecyclerView setup.
* `app/src/main/java/.../RowAdapter.kt` — Adapter for row containers.
* `app/src/main/java/.../SquareAdapter.kt` — Adapter/ViewHolder for individual cells within a row.
* `app/src/main/res/layout/row_item.xml` — XML layout for a row container.
* `app/src/main/res/layout/square_item.xml` — XML layout for a 32dp × 32dp cell.
* `app/src/main/java/.../MainViewModel.kt` — ViewModel holding the dataset and exposing LiveData / Flow for UI.


---

## Notes, caveats & future improvements

* Paging the data to avoid huge upfront allocation.
* Alternative layout strategies could pack the entire grid into a single custom `LayoutManager` (advanced) for even tighter control and possibly higher throughput; the current approach is chosen for clarity and modularity.

---

## License

This project is provided as-is for demonstration and benchmarking purposes.

---

### Author

Mohit Rajput
Blogs: [https://medium.com/p/a2b8755da2bb](https://medium.com/p/a2b8755da2bb)
GitHub: [https://github.com/mohitrajput987](https://github.com/mohitrajput987)
Email: [mohitrajput987@gmail.com](mailto:mohitrajput987@gmail.com)
