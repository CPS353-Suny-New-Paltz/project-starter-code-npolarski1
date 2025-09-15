package api;

import project.annotations.ProcessAPIPrototype;

public class ComputeDataStorageAPIPrototype {

	@ProcessAPIPrototype
	public void prototype(ComputeDataStorageAPI processApi) {
		// compute engine will have input source from user passed through network API
		// data storage system will read input source here
		InputInts inputInts = processApi.readInput();
		
		ComputationResult compResult = processApi.processInputInts(inputInts);
		
		// data storage system will write to output source here
		processApi.writeOutput(compResult);
	}
}
