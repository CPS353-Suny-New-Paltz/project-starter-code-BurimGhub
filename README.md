# Software Engineering Project Starter Code
The compute engine will convert a positive integer to its English word representation. This includes handling units, tens, hundreds, thousands, millions, and billions.

**Input/Output Format:**

   **Input stream:** A sequence of integers, such as 6, 21, 105, 1000000.

  **Delimiters:** The user can specify a pair delimiter such as separating input/result pairs and a key-value delimiter separating an input from its result.

   **Example Output:** With a pair delimiter of ' , ' and a key-value delimiter of ' : ' the output would be:

      6:six,21:twenty one,105:one hundred five,1000000:one million
![System Diagram](https://github.com/CPS353-Suny-New-Paltz/project-starter-code-BurimGhub/blob/feature/images/SystemDiagram.png)

## Checkpoint 8: Performance Tuning

For this checkpoint, we identified a CPU bottleneck in the core logic and optimized it to increase performance.

### 1. Bottleneck Identification
Using **JConsole**, we monitored the application during a heavy load.
* **Observation:** High CPU usage was detected in the `ConceptualAPIImpl.convertNumberToWords()` method.
* **Root Cause:** The method was relying on repeated **String concatenation** (using the `+` operator) inside recursive loops.

### 2. Optimizations Applied
We created a new implementation, `OptimizedConceptualAPIImpl`, with three specific improvements:

1.  **StringBuilder:** Replaced String concatenation with `StringBuilder`. This allows modifying the string in place, significantly reducing memory allocations.
2.  **Memoization (Caching):** Added a `HashMap` cache. If a number (e.g., `100`) appears multiple times in the dataset, the result is retrieved from the map instead of recomputing it.
3.  **Cache Management:** Implemented a limit (`MAX_CACHE_SIZE = 20,000`). If the cache becomes too full, it clears itself to prevent memory leaks and `OutOfMemoryErrors` during long-running jobs.

### 3. Benchmark Results

We validated the performance improvements using two benchmark tests.

1. **Unit Benchmark** (Algorithm Only)
   * Original (Slow): 211 ms
   * Optimized (Fast): 54 ms
   * Improvement: **290.74%**

2. **Integration Benchmark** (Full Engine)
   * Original (Slow): 1625 ms
   * Optimized (Fast): 1021 ms
   * Improvement: **59.16%**

*Note: The Unit benchmark measures the raw algorithm speed. The Integration benchmark includes the overhead of the Coordinator, Thread Pool, and Data Store, resulting in a realistic system-wide speedup.*

### Links to Benchmark Tests
* **Unit Benchmark:** [ConceptualApiBenchmarkTest.java](https://github.com/CPS353-Suny-New-Paltz/project-starter-code-BurimGhub/blob/main/test/performancetesting/ConceptualApiBenchmarkTest.java)
* **Integration Benchmark:** [ComputeEngineIntegrationBenchmarkTest.java](https://github.com/CPS353-Suny-New-Paltz/project-starter-code-BurimGhub/blob/main/test/performancetesting/ComputeEngineIntegrationBenchmarkTest.java)
