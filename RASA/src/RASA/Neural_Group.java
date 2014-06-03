//==========================================================================================
//
//	Neural Groups
//
//	Overview: The major components of the neural networks, they contain groups of neurons
//		and may construct neurons from specified neuron types and other source neural
//		groups.
//
//==========================================================================================
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
//==========================================================================================
//
//	[START] Includes, Packages, Headers
//
//------------------------------------------------------------------------------------------

// Package
package RASA;

// Imports
import java.util.ArrayList;
import java.util.List;

import RASA.Neuron_Types.Input_Neuron;

//------------------------------------------------------------------------------------------
//
//	[END] Includes, Packages, Headers
//
//==========================================================================================
//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
//==========================================================================================
//
//	[START] Neural_Group Class
//
//------------------------------------------------------------------------------------------

public class Neural_Group {

	//=====================================================================
	//
	// 		CLASS FIELDS
	//
	//---------------------------------------------------------------------
	
	// Attributes
	public String Alias;
	public Neural_Network Parent_NN;
	public int ID;

	// Feeding groups
	private List<Neural_Group> Feed_To = new ArrayList<Neural_Group>();
	private List<Neural_Group> Feed_From = new ArrayList<Neural_Group>();
	
	// Neurons
	private int Next_N_ID;
	private int Max_N_Complexity; // Max complexity of constructed neurons
	private List<String> N_Types = new ArrayList<String>();
	public List<Neuron> N_List = new ArrayList<Neuron>();

	//=====================================================================
	//
	// 		CLASS METHODS
	//
	//---------------------------------------------------------------------
	
	// Constructor
	public Neural_Group() {
		Alias = "New NG";
		Next_N_ID = 1;
		ID = 0;
	}

	//==========================================
	// Reports
	//------------------------------------------
	
	public void Print_Report(String Tags) {
		if (Tags.contains("FULL") || !Tags.contains("HIDE_NG")) {
			if (Tags.contains("SHORT")) {
				// Print alias
				Print_Header();
				System.out.print("\"" + Alias + "\"");
			} else {
				// Print border
				System.out.print("\n");
				Print_Header();
				System.out.print("==================================");
				Print_Header();
				System.out.print(" Printing Neural Group: " + Alias);
				Print_Header();
				System.out.print("----------------------------------");
			}
			
			// Print construction info
			

			// Print feeding from info
			for (int F = 0; F < Feed_From.size(); F++) {
				if (F == 0) {
					Print_Header();
					Print_Header();
					System.out.print(" Feeding From:");
				}
				Print_Header();
				System.out.print("   - \"" + Feed_From.get(F).Alias + "\" ");
				Feed_From.get(F).Print_Label();
			}
			// Print feeding to info
			for (int F = 0; F < Feed_To.size(); F++) {
				if (F == 0) {
					Print_Header();
					Print_Header();
					System.out.print(" Feeding To:");
				}
				Print_Header();
				System.out.print("   - \"" + Feed_To.get(F).Alias + "\" ");
				Feed_To.get(F).Print_Label();
			}
			// Print neuron types
			for (int NT = 0; NT < N_Types.size(); NT++) {
				if (NT == 0) {
					Print_Header();
					Print_Header();
					System.out.print(" N Types:");
				}
				if ((NT + 2) % 5 == 0 ) Print_Header();
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
		
		// Print neurons
		if (!Tags.contains("HIDE_NG")){
			Print_Header();
			Print_Header();
			System.out.print(" << Neurons >>");
		}
		for (int N = 0; N < N_List.size(); N++) {
			N_List.get(N).Print_Report(Tags);
		}
	}

	public void Print_Label() {
		System.out.print("[NG:" + ID + "]");
	}
	
	public void Print_Header() {
		Parent_NN.Print_Header();
		Print_Label();
	}
	public void Print_Header(int Depth) {
		if (Depth > 1) Parent_NN.Print_Header();
		if (Depth > 0) Print_Label();
	}

	//==========================================
	// Neuron Management
	//------------------------------------------
	
	private int Generate_Next_N_ID (){
		return Next_N_ID++;
	}
	
	public void Add_Feeder(Neural_Group New_Feeder) {
		for (int NG = 0; NG < Feed_From.size(); NG++)
			if (Feed_From.get(NG) == New_Feeder) return;
		if (New_Feeder == null) return;
		Feed_From.add(New_Feeder);
		New_Feeder.Feed_To.add(this);
	}
	
	// Adds a neuron type for construction
	public void Add_N_Type(String New_Type) {
		for (int NT = 0; NT < N_Types.size(); NT++) {
			if (N_Types.get(NT).equals(New_Type)) return;
		}
		if (New_Type == null) return;
		N_Types.add(New_Type);
	}
	
	// Checks whether valid neuron type
	private boolean Valid_N_Type (String N_Type) {
		for (int NT = 0; NT < N_Types.size(); NT++) {
			if (N_Types.get(NT).equals(N_Type)) return true;
		}
		return false;
	}
	
	public Neuron New_Neuron(String Type) {
		// Check for valid type
		if (!Valid_N_Type(Type)) return null;
		Neuron N;
		switch (Type) {
			case "INPUT": case "SENSOR":
				N = new Input_Neuron(Generate_Next_N_ID(), this);
				break;
			default:
				N = new Neuron(Generate_Next_N_ID(), this);
				break;	
		}
		
		// Initialize
		N.Alias = "New " + Type + " Neuron";
		N_List.add(N);
		return N;
	}
	
	public boolean Feeds_From (Neural_Group NG) {
		for (int ng = 0; ng < Feed_From.size(); ng++)
			if (Feed_From.get(ng) == NG) return true;
		return false;
	}
	
	public boolean Feeds_To (Neural_Group NG) {
		for (int ng = 0; ng < Feed_To.size(); ng++)
			if (Feed_To.get(ng) == NG) return true;
		return false;
	}
	
}

//------------------------------------------------------------------------------------------
//
//	[END] Neural_Group Class
//
//==========================================================================================