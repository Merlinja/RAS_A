//==========================================================================================
//
//	Neural Network
//
//	Overview: The main class, acquires input, analyzes input, generates a decision, and
//		updates connections / associations.
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
import java.util.Iterator;
import java.util.List;

//------------------------------------------------------------------------------------------
//
//	[END] Includes, Packages, Headers
//
//==========================================================================================
//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
//==========================================================================================
//
//	[START] Neural_Network Class
//
//------------------------------------------------------------------------------------------

public class Neural_Network {
	
	// =====================================================================
	//
	// CLASS FIELDS
	//
	// ---------------------------------------------------------------------

	public String Alias;
	public Internal_Clock First_IC;

	// Neural Groups
	int Next_NG_ID;
	List<Neural_Group> NG_List = new ArrayList<Neural_Group>();
	public Neural_Group Default_Driver_NG;
	
	// Unique neurons
	public Neuron Reward_Driver_N;
	public Neuron Punish_Driver_N;
	
	// Channels
	int Next_CH_ID;
	List<Channel> CH_List = new ArrayList<Channel>();
	public Channel Default_Input_CH;
	public Channel Default_Logic_CH;
	public Channel Default_Bonding_CH;

	// =====================================================================
	//
	// CLASS Methods
	//
	// ---------------------------------------------------------------------

	// Constructor
	public Neural_Network() {
		Alias = "New NN";
		Next_NG_ID = 1;
		Next_CH_ID = 1;
		
		// Construct default channels
		Default_Input_CH = New_Channel();
		Default_Input_CH.Alias = "Default Input";
		Default_Logic_CH = New_Channel();
		Default_Logic_CH.Alias = "Default Logic";
		Default_Bonding_CH = Default_Input_CH;
		Default_Logic_CH.LT_Weight = 0.5;
		Default_Logic_CH.ST_Weight = 0.2;
		Default_Logic_CH.LC_Weight = 1.0;
		Default_Logic_CH.MC_Weight = 0.1;
		Default_Logic_CH.CH_Weight = 1.5;
		Default_Logic_CH.Analyze_Channel(Default_Input_CH);
		Default_Logic_CH.Feed_From(Default_Input_CH);
		
		// Construct default neurons / groups
		Default_Driver_NG = New_Neural_Group();
		Default_Driver_NG.Alias = "Driver Group";
		Default_Driver_NG.Add_N_Type("INPUT");
		Reward_Driver_N = Default_Driver_NG.New_Neuron("INPUT");
		Reward_Driver_N.Alias = "Reward Driver";
		Punish_Driver_N = Default_Driver_NG.New_Neuron("INPUT");
		Punish_Driver_N.Alias = "Punishment Driver";
		
		// Set up default internal clocks
		First_IC = new Internal_Clock();
		First_IC.Parent_NN = this;
		First_IC.Alias = "First Clock";
	}

	//==========================================
	// Reports
	//------------------------------------------
	
	public void Print_Report(String Tags) {

		// Header + full info
		if ((!Tags.contains("SHORT") || Tags.contains("FULL"))
				&& !Tags.contains("HIDE_NN")) {
			// Print border
			Print_Header();
			System.out.print("========================================");
			Print_Header();
			System.out.print(" Printing Neural Network: " + Alias);
			Print_Header();
			System.out.print("----------------------------------------");

			// Print attributes
			Print_Header();
			System.out.print(" Next NG ID = " + Next_NG_ID);
		}

		// Go through Channels
		for (int CH = 0; CH < CH_List.size(); CH++) {
			CH_List.get(CH).Print_Report(Tags);
		}
		
		// Go through Neural Groups
		for (int NG = 0; NG < NG_List.size(); NG++) {
			NG_List.get(NG).Print_Report(Tags);
		}
		
		// Go through Internal Clocks
		Internal_Clock IC = First_IC;
		while (IC != null) {
			IC.Print_Report(Tags);
			IC = IC.Next;
		}
		
		// Print border
		if ((!Tags.contains("SHORT") || Tags.contains("FULL"))
				&& !Tags.contains("HIDE_NN")) {
			Print_Header();
			System.out.print("========================================");
		}

	}

	public void Print_Label() {
		System.out.print("[" + Alias + "]");
	}

	public void Print_Header() {
		System.out.println("");
		Print_Label();
	}

	//==========================================
	// Neural Groups
	//------------------------------------------
	
	public Neural_Group New_Neural_Group() {
		
		// Construct new NG
		Neural_Group New_NG = new Neural_Group();
		New_NG.Parent_NN = this;
		New_NG.ID = Next_NG_ID;
		Next_NG_ID++;

		// Add to list
		NG_List.add(New_NG);

		// Return newly made NG
		return New_NG;
	}
	
	public Neural_Group Get_Neural_Group(int NG_ID) {
		// Go through Neural Groups
		for (int NG = 0; NG < NG_List.size(); NG++) {
			if (NG_List.get(NG).ID == NG_ID) 
				return NG_List.get(NG);
		} // Searched all Neural Groups
		return null;
	}

	//==========================================
	// Channels
	//------------------------------------------
	
	public Channel New_Channel() {
		
		// Construct new CH
		Channel New_CH = new Channel();
		New_CH.Parent_NN = this;
		New_CH.ID = Next_CH_ID;
		Next_CH_ID++;

		// Add to list
		CH_List.add(New_CH);

		// Return newly made NG
		return New_CH;
	}
	
	//==========================================
	// Internal Clocks
	//------------------------------------------
	
	public Internal_Clock Add_Internal_Clock() {
		return First_IC;
	}
	public Internal_Clock Get_Internal_Clock(int Level) {
		return First_IC;
	}
}

//------------------------------------------------------------------------------------------
//
//	[END] Neural_Network Class
//
//==========================================================================================