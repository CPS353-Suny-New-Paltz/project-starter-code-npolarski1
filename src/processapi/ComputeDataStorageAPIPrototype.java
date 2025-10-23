package processapi;

import project.annotations.ProcessAPIPrototype;
import shared.ComputationResult;
import shared.InputInts;

public class ComputeDataStorageAPIPrototype {

	@ProcessAPIPrototype
	public void prototype(DataStorageProcessAPI processApi) {
		// compute engine will have input source from user passed through network API
		// data storage system will read input source here
		InputInts inputInts = processApi.readInput();
		
		// data storage system will write to output source here
		//processApi.writeOutput(new ComputationResult());
	}
}
