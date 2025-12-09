package grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import grpc.computeengine.ComputeEngineServiceGrpc;
import grpc.computeengine.JobRequest;
import grpc.computeengine.JobResponse;
import grpc.computeengine.ShutdownRequest;
import grpc.computeengine.ShutdownResponse;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

class UserClient {
	private final ManagedChannel channel;
	private final ComputeEngineServiceGrpc.ComputeEngineServiceBlockingStub stub;
	private static final String RESOURCES_DIR = "Resources";

	public UserClient(String host, int port) {
		this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		this.stub = ComputeEngineServiceGrpc.newBlockingStub(channel);
		System.out.println("Connected to Compute Engine at " + host + ":" + port + "\n");
		createResourcesDirectory();
	}

	private void createResourcesDirectory() {
		try {
			Path resourcesPath = Paths.get(RESOURCES_DIR);
			if (!Files.exists(resourcesPath)) {
				Files.createDirectories(resourcesPath);
			}
		} catch (Exception e) {
			System.err.println("Warning: Could not create Resources directory: " + e.getMessage());
		}
	}

	private String getResourcesPath(String filename) {
		return Paths.get(RESOURCES_DIR, filename).toString();
	}

	public void submitJob(String input, String output, String pairDelim, String kvDelim) {
		System.out.println("Submitting job...\n");

		JobRequest.Builder requestBuilder = JobRequest.newBuilder().setInputSource(input).setOutputSource(output);

		if (pairDelim != null && !pairDelim.isEmpty()) {
			requestBuilder.setPairDelimiter(pairDelim);
		}
		if (kvDelim != null && !kvDelim.isEmpty()) {
			requestBuilder.setKeyValueDelimiter(kvDelim);
		}

		JobRequest request = requestBuilder.build();

		try {
			JobResponse response = stub.submitJob(request);

			System.out.println("========================================");
			System.out.println("           JOB RESULT");
			System.out.println("========================================");
			System.out.println("Job ID:  " + response.getJobId());
			System.out.println("Status:  " + (response.hasStatus() ? response.getStatus() : "N/A"));
			System.out.println("Success: " + (response.getSuccess() ? "YES" : "NO"));
			if (response.hasMessage()) {
				System.out.println("Message: " + response.getMessage());
			}
			System.out.println("========================================\n");

			if (response.getSuccess()) {
				showOutput(output);
			}

		} catch (Exception e) {
			System.err.println("Error submitting job: " + e.getMessage());
		}
	}

	public void shutdownServers() {
		try {
			System.out.println("\nSending shutdown signal to servers...");

			ShutdownRequest shutdownRequest = ShutdownRequest.newBuilder().build();
			ShutdownResponse shutdownResponse = stub.shutdown(shutdownRequest);

			if (shutdownResponse.getSuccess()) {
				System.out.println("Servers acknowledged shutdown");
			}
		} catch (Exception e) {
			System.out.println("(Servers may already be down)");
		}
	}

	// Display output with proper formatting for readability
	private void showOutput(String path) {
		try {
			File file = new File(path);
			if (file.exists()) {
				String content = Files.readString(file.toPath());
				System.out.println("Output File Content:");
				System.out.println("----------------------------------------");

				// Format output to wrap at 60 characters per line
				String[] pairs = content.split(",");
				int lineLength = 0;
				StringBuilder formattedOutput = new StringBuilder();

				for (int i = 0; i < pairs.length; i++) {
					String pair = pairs[i];

					// Add pair to line
					if (lineLength + pair.length() > 60 && lineLength > 0) {
						formattedOutput.append("\n");
						lineLength = 0;
					}

					formattedOutput.append(pair);
					lineLength += pair.length();

					// Add comma if not last item
					if (i < pairs.length - 1) {
						formattedOutput.append(", ");
						lineLength += 2;
					}
				}

				System.out.println(formattedOutput.toString());
				System.out.println("----------------------------------------");
				System.out.println("Saved to Resources/" + file.getName() + "\n");
			} else {
				System.out.println("(Output file not found)\n");
			}
		} catch (Exception e) {
			System.out.println("(Could not read output file: " + e.getMessage() + ")\n");
		}
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		String host = args.length > 0 ? args[0] : "localhost";
		int port = args.length > 1 ? Integer.parseInt(args[1]) : 9090;

		UserClient client = new UserClient(host, port);

		System.out.println("========================================");
		System.out.println("   NUMBER TO WORDS CONVERTER");
		System.out.println("========================================");

		boolean running = true;

		while (running) {
			System.out.println("\n========================================");
			System.out.println("            MAIN MENU               ");
			System.out.println("========================================");
			System.out.println("  1. Enter a Path for input file");
			System.out.println("  2. Type numbers manually");
			System.out.println("  3. Exit (and shutdown servers)");
			System.out.println("----------------------------------------");
			System.out.print("Choice (1, 2, or 3): ");

			int choice = scanner.nextInt();
			scanner.nextLine();

			if (choice == 3) {
				System.out.println("\nShutting down...");
				client.shutdownServers();
				running = false;
				break;
			}

			if (choice != 1 && choice != 2) {
				System.out.println("Invalid choice. Please enter 1, 2, or 3.\n");
				continue;
			}

			String inputPath;
			File tempFile = null;

			if (choice == 1) {
				// Choice 1: File input - no temp file
				System.out.print("Enter input file path: ");
				inputPath = scanner.nextLine();
			} else {
				// Choice 2: Type numbers - create temp file
				System.out.print("Enter numbers (comma-separated, e.g., 1,5,10,25): ");
				String numbers = scanner.nextLine();

				tempFile = File.createTempFile("input", ".txt", new File(RESOURCES_DIR));
				Files.write(tempFile.toPath(), numbers.getBytes());
				inputPath = tempFile.getAbsolutePath();

				System.out.println("Created temporary input file");
			}

			// Get output filename - always saves to Resources
			System.out.print("Enter output filename (e.g., output.txt): ");
			String outputFilename = scanner.nextLine();
			String outputPath = client.getResourcesPath(outputFilename);
			System.out.println("Saving to Resources/" + outputFilename);

			System.out.print("Pair delimiter (press Enter for default ','): ");
			String pairDelim = scanner.nextLine();

			System.out.print("Key-value delimiter (press Enter for default ':'): ");
			String kvDelim = scanner.nextLine();

			System.out.println();
			client.submitJob(inputPath, outputPath, pairDelim, kvDelim);

			// Delete temp input file for choice 2
			if (choice == 2 && tempFile != null && tempFile.exists()) {
				tempFile.delete();
			}

			// Delete output file for choice 2 after displaying
			if (choice == 2) {
				File outputFile = new File(outputPath);
				if (outputFile.exists()) {
					outputFile.delete();
				}
			}
		}

		client.shutdown();
		scanner.close();
		System.out.println("EXITING...");
		System.exit(0);
	}
}
