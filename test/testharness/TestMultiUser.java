package testharness;

//import API_Package.MultithreadedNetworkAPI;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import networkapi.UserRequestNetworkAPI;
import implementapi.UserRequestNetworkImpl;
import implementapi.MultithreadedUserRequestNetworkImpl;

public class TestMultiUser {
	
	private UserRequestNetworkAPI coordinator;
	
	@BeforeEach
	public void initializeComputeEngine() {
		coordinator = new MultithreadedUserRequestNetworkImpl();
	}
	public void cleanup() {
//        if (networkAPI != null) {
//            networkAPI.shutdown();
//        }
    }
	@Test
	public void compareMultiAndSingleThreaded() throws Exception {
		int numThreads = 4;
		List<TestUser> testUsers = new ArrayList<>();
		for (int i = 0; i < numThreads; i++) {
			testUsers.add(new TestUser(coordinator));
		}
		
		// Run single threaded
		String singleThreadFilePrefix = "testMultiUser.compareMultiAndSingleThreaded.test.singleThreadOut.tmp";
		for (int i = 0; i < numThreads; i++) {
			File singleThreadedOut = 
					new File(singleThreadFilePrefix + i);
			singleThreadedOut.deleteOnExit();
			testUsers.get(i).run(singleThreadedOut.getCanonicalPath());
		}
		
		// Run multi threaded
		ExecutorService threadPool = Executors.newCachedThreadPool();
		List<Future<?>> results = new ArrayList<>();
		String multiThreadFilePrefix = "testMultiUser.compareMultiAndSingleThreaded.test.multiThreadOut.tmp";
		for (int i = 0; i < numThreads; i++) {
			File multiThreadedOut = 
					new File(multiThreadFilePrefix + i);
			multiThreadedOut.deleteOnExit();
			String multiThreadOutputPath = multiThreadedOut.getCanonicalPath();
			TestUser testUser = testUsers.get(i);
			results.add(threadPool.submit(() -> testUser.run(multiThreadOutputPath)));
		}
		
		results.forEach(future -> {
			try {
				future.get();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		
		
		// Check that the output is the same for multi-threaded and single-threaded
		List<String> singleThreaded = loadAllOutput(singleThreadFilePrefix, numThreads);
		List<String> multiThreaded = loadAllOutput(multiThreadFilePrefix, numThreads);
		Assertions.assertEquals(singleThreaded, multiThreaded);
	}

	private List<String> loadAllOutput(String prefix, int numThreads) throws IOException {
		List<String> result = new ArrayList<>();
		for (int i = 0; i < numThreads; i++) {
			File multiThreadedOut = 
					new File(prefix + i);
			result.addAll(Files.readAllLines(multiThreadedOut.toPath()));
		}
		return result;
	}
	@Test
	public void smokeTest() throws Exception {
        // smoke test uses the single-threaded implementation
        UserRequestNetworkAPI single = new UserRequestNetworkImpl();
        TestUser testUser = new TestUser(single);
        File out = new File("testMultiUser.smokeTest.singleThreadOut.tmp");
        out.deleteOnExit();
        testUser.run(out.getCanonicalPath());
        List<String> lines = Files.readAllLines(out.toPath());
        Assertions.assertFalse(lines.isEmpty(), "Smoke test output should not be empty");
    }
}