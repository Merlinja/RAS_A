package RASA;

import java.util.ArrayList;
import java.util.List;

public class Neural_Group {

	// =====================================================================
	// CLASS FIELDS
	// ---------------------------------------------------------------------
	
	// Attributes
	public String Alias;
	public Neural_Network Parent_NN;
	public int ID;

	// Feeding groups
	List<Neural_Group> Feed_To = new ArrayList<Neural_Group>();
	List<Neural_Group> Feed_From = new ArrayList<Neural_Group>();
	
	// Neurons
	int Next_N_ID;
	int Max_N_Complexity; // Max complexity of constructed neurons
	List<String> N_Types = new ArrayList<String>();

	// =====================================================================
	// CLASS Methods
	// ---------------------------------------------------------------------
	
	// Constructor
	public Neural_Group() {
		Alias = "New NG";
		Next_N_ID = 1;
		ID = 0;
	}

	// Reports
	public void Print_Report(String Tags) {
		if (Tags.contains("FULL") || !Tags.contains("HIDE_NG")) {
			if (Tags.contains("SHORT")) {
				// Print alias
				Print_Header();
				System.out.print("\"" + Alias + "\"");
			} else {
				// Print border
				Print_Header();
				System.out.print("==================================");
				Print_Header();
				System.out.print(" Printing Report: " + Alias);
				Print_Header();
				System.out.print("----------------------------------");
			}
			
			// Print construction info
			
			// Print feeding from info
			for (int F = 0; F < Feed_From.size(); F++) {
				Print_Header();
				System.out.print(" <-- ");
				Feed_From.get(F).Print_Label();
			}
			// Print feeding to info
			for (int F = 0; F < Feed_To.size(); F++) {
				Print_Header();
				System.out.print(" --> ");
				Feed_To.get(F).Print_Label();
			}
			// Print feeding to info
			for (int NT = 0; NT < N_Types.size(); NT++) {
				if (NT % 5 == 0 ) Print_Header();
				System.out.print(" {" + N_Types.get(NT) + "}");
				if (NT < (N_Types.size() - 1)) System.out.print(",");
			}
		}
		
		// Full info
		if ((!Tags.contains("SHORT") || Tags.contains("FULL"))
				&& !Tags.contains("HIDE_NG")) {
			// Neuron construction info
			Print_Header();
			System.out.print(" Next N ID = " + Next_N_ID);
			Print_Header();
			System.out.print(" Max Complexity = " + Max_N_Complexity);
		}

	}

	public void Print_Label() {
		System.out.print("[NG:" + ID + "]");
	}
	public void Print_Header() {
		Parent_NN.Print_Header();
		Print_Label();
	}

	public void Add_Feeder(Neural_Group New_Feeder) {
		// TODO check for edundancies, already exists
		if (New_Feeder == null) return;
		Feed_From.add(New_Feeder);
		New_Feeder.Feed_To.add(this);
	}
	
	// Adds a neuron type for construction
	public void Add_N_Type(String New_Type) {
		// TODO check for edundancies, already exists
		if (New_Type == null) return;
		N_Types.add(New_Type);
	}
	
}
