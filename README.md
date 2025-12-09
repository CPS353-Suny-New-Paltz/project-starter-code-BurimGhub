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

## Docker & Build Instructions

This project uses Docker to containerize the gRPC services (Compute Engine, Data Store, and User Client). It utilizes the **Gradle Shadow Plugin** to create a "Fat Jar" that bundles all dependencies (including gRPC libraries) into a single executable. This approach ensures stability and prevents `NoClassDefFoundError` issues within the containerized environment. The system runs on **Java 21 (Eclipse Temurin)**.

### How to Run

Follow these steps in order to build and launch the system.

**1. Generate Protobuf Files:**  
Generate the gRPC Java code from the `.proto` definitions.
```bash
gradlew generateProto
```
**2. Build the Application:**  
Run the Eclipse task. The build script is configured so that this command automatically triggers the creation of the Shadow Jar (Fat Jar) required for Docker.
```Bash
gradlew eclipse
```
**3. Start the Servers:**  
Start the Data Store and Compute Engine services in the background. The --build flag ensures Docker picks up the latest JAR file.
```Bash
docker-compose up --build
```
*Wait until you see "Server started" logs for both the Data Store and Compute Engine.*  
**4. Run the Client:**  
Open a new terminal window to run the interactive client in the foreground.
```Bash
docker-compose run client
```
**_Note on File Input_**   
*When using the "Enter a Path for input file" option in the Client:  
*-Place your text file inside the Resources folder in your project root.*   
*-Enter the path as: Resources/filename.txt*  
*-Do not use absolute paths (e.g., C:\Users...).**  
**Cleaning Up**  
To stop all services and remove containers:
```Bash
docker-compose down
```
