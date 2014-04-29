package Tests;

import RASA.Neural_Group;
import RASA.Neural_Network;

public class Test_1 {
	public static void main(String[] args) {
		// Initialize test
		System.out.println("Starting Test 1");

		Neural_Network NN = new Neural_Network();
		Neural_Group NG1 = NN.New_Neural_Group();
		Neural_Group NG2 = NN.New_Neural_Group();
		NN.Print_Report("SHORT");
		NG1.Add_Feeder(NG2);
		NG1.Add_Feeder(NG1);
		NG1.Add_N_Type("AND");
		NG1.Add_N_Type("INPUT");
		NN.Print_Report("FULL");
		if (NG1 == NG2) return;
	}
}
