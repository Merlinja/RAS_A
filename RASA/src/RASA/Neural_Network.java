//==========================================================================================
//
//	Neural Network
//
//	Overview: The main class, acquires input, analyzes input, generates a decision, and
//		updates connections / associations.
//
//	Primary Actions:
//		1. Process Input
//		2. Make Decision
//			- Add outputs as inputs
//		3. Advance Clock / Cycle
//			- Make bonds
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
	
	// Synapses
	List<Synapse> S_List = new ArrayList<Synapse>();
	
	// Charge nodes
	public Charge_Node First_CN;
	
	// Processing variables
	public int Default_Input_Cycles;
	public int Default_Output_Cycles;

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
		First_CN = null;
		
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
		Default_Driver_NG.Force_Alias = true;
		Default_Driver_NG.Add_N_Type("INPUT");
		Reward_Driver_N = Default_Driver_NG.New_Neuron("INPUT");
		Reward_Driver_N.Alias = "Reward Driver";
		Reward_Driver_N.Force_Alias = true;
		Reward_Driver_N.Shrink_Header = true;
		Punish_Driver_N = Default_Driver_NG.New_Neuron("INPUT");
		Punish_Driver_N.Alias = "Punishment Driver";
		Punish_Driver_N.Force_Alias = true;
		Punish_Driver_N.Shrink_Header = true;
		
		// Set up default internal clocks
		First_IC = new Internal_Clock();
		First_IC.Parent_NN = this;
		First_IC.Alias = "First Clock";
		
		// Processing variables
		Default_Input_Cycles = 10;
		Default_Output_Cycles = 10;
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
			System.out.print("=============================================");
			Print_Header();
			Print_Header();
			System.out.print(" Printing Neural Network: " + Alias);
			Print_Header();
			Print_Header();
			System.out.print("---------------------------------------------");

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
		
		// Print Synapses
		Print_Synapses(Tags);
		
		// Print Update Queue
		Print_Update_Queue(5);
		
		// Print border
		if ((!Tags.contains("SHORT") || Tags.contains("FULL"))
				&& !Tags.contains("HIDE_NN")) {
			Print_Header();
			System.out.print("=============================================");
		}

	}

	public void Print_Label() {
		System.out.print("[" + Alias + "]");
	}

	public void Print_Header() {
		System.out.println("");
		Print_Label();
	}
	
	public void Print_Synapses(String Tags) {
		System.out.print("\n");
		Print_Header();
		System.out.print("==================================");
		Print_Header();
		System.out.print(" Printing Synapses");
		Print_Header();
		System.out.print("----------------------------------");
		for (int S = 0; S < S_List.size(); S++) {
			System.out.print("\n");
			S_List.get(S).Print_Report(Tags);
		}
	}
	
	public void Print_Update_Queue(int Max) {
		System.out.print("\n");
		Print_Header();
		System.out.print("==================================");
		Print_Header();
		System.out.print(" Printing Update Queue");
		Print_Header();
		System.out.print("----------------------------------");
		Charge_Node CN = First_CN;
		int Current = 1;
		while (CN != null && Current <= Max) {
			Print_Header();
			//System.out.format("%3d%n", CN.Update_Rating());
			System.out.print(" " + CN.Update_Rating() + " ");
			CN.Print_Header(3);
			System.out.print(" " + CN.Active_Charge() * 100.0 + " ");
			CN = CN.NN_Next;
			Current++;
		}
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
	
	public Channel New_Channel(Neuron N) {
		
		// Construct new CH
		Channel New_CH = new Channel(N);
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
	
	public Time_Node Get_TN () {return Get_TN (First_IC);}
	public Time_Node Get_TN (Internal_Clock IC) {return IC.Get_TN();}
	public Time_Node Get_TN(int Time, Internal_Clock IC) {return IC.Get_TN(Time);}
	
	//==========================================
	// Synapses
	//------------------------------------------
	
	public void Update_Synapses() {
		Update_Synapses(Default_Bonding_CH, Get_TN(), "ABSOLUTE, DEBUG");
	}
	
	public void Update_Synapses(String Tags) {
		Update_Synapses(Default_Bonding_CH, Get_TN(), Tags);
	}
	
	public void Update_Synapses(Channel CH, Time_Node TN, String Tags) {
		// Methods for updating weights
		// 1: Binary "Absolute"
		// 2: Take from Short Term "ST"
		
		if (Tags.contains("DEBUG")) {
			System.out.print("\n\n==================================");
			System.out.print("\n [DEBUG] Updating Synapses");
			System.out.print("\n----------------------------------\n");
			System.out.print("\n Tags: " + Tags + "\n");
		}
		
		// Go through every NG as "NG From"
		for (int NG_From_Itr = 0; NG_From_Itr < NG_List.size(); NG_From_Itr++) {
			Neural_Group NG_From = NG_List.get(NG_From_Itr);
			
			// Go through every available NG as "NG To"
			for (int NG_To_Itr = 0; NG_To_Itr < NG_List.size(); NG_To_Itr++) {
				Neural_Group NG_To = NG_List.get(NG_To_Itr);
			
				// Go through every N as "N From"
				for (int N_From_Itr = 0; N_From_Itr < NG_From.N_List.size(); N_From_Itr++) {
					Neuron N_From = NG_From.N_List.get(N_From_Itr);
					
					// Go through every N as "N To"
					for (int N_To_Itr = 0; N_To_Itr < NG_To.N_List.size(); N_To_Itr++) {
						Neuron N_To = NG_To.N_List.get(N_To_Itr);
						if (Tags.contains("DEBUG")) {
							System.out.print("\n\nATTEMPTING: ");
							N_From.Get_CN(First_IC.Get_TN(), CH).Print_Header();
							System.out.print(" " + N_From.Get_CN(First_IC.Get_TN(), CH).Active_Charge());
							N_To.Get_CN(First_IC.Get_TN(), CH).Print_Header();
							System.out.print(" " + N_To.Get_CN(First_IC.Get_TN(), CH).Active_Charge());

						}
						
						// Don't bond with self
						if (N_From != N_To) {
							if (Tags.contains("ABSOLUTE")) {
								if (N_From.Get_CN(First_IC.Get_TN(), CH).Active_Charge() == 1.00) {
									if (Tags.contains("DEBUG")) {							
										System.out.print("\n\nUPDATING: ");
										N_From.Get_CN(First_IC.Get_TN(), CH).Print_Header();
										N_To.Get_CN(First_IC.Get_TN(), CH).Print_Header();
									}
									Synapse S = N_From.Get_Synapse(N_To);
									S.Update(CH, TN);
								}
							}
						}
					} // [END] Go through every N as "N To"
				} // [END] Go through every N as "N From"
			} // [END] Go through every available NG as "NG To"
		} // [END] Go through every NG as "NG From"
		
	}
	
	//==========================================
	// Charge Nodes
	//------------------------------------------
	public void Register_CN (Charge_Node CN) {
		CN.NN_Next = First_CN;
		if (First_CN == null) First_CN = CN;
		else First_CN.NN_Prev = CN;
		First_CN = CN;
		CN.Sort_NN();
	}
	
	//==========================================
	// Primary Procedures TODO
	//------------------------------------------
	public void Analyze_Input()	{Analyze_Input(Default_Input_Cycles);};
	public void Analyze_Input(int Cycles) {
		for (int C = 1; C <= Cycles; C++) {
			if (First_CN == null) return;
			First_CN.Update();
		}
		
	}
	
	public void Analyze_Output() {Analyze_Output(Default_Output_Cycles);};
	public void Analyze_Output(int Cycles){
		
	}
	
	public void Advance_Clock(){
		First_IC.Advance_Clock();
	}
}

//------------------------------------------------------------------------------------------
//
//	[END] Neural_Network Class
//
//==========================================================================================