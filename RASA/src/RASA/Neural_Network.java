package RASA;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Neural_Network {
	// =====================================================================
	// CLASS FIELDS
	// ---------------------------------------------------------------------

	public String Alias;

	// Neural Groups
	int Next_NG_ID;
	List<Neural_Group> NG_List = new ArrayList<Neural_Group>();

	// =====================================================================
	// CLASS Methods
	// ---------------------------------------------------------------------

	// Constructor
	public Neural_Network() {
		Alias = "New NN";
		Next_NG_ID = 1;
	}

	// Reports
	public void Print_Report(String Tags) {

		// Header + full info
		if ((!Tags.contains("SHORT") || Tags.contains("FULL"))
				&& !Tags.contains("HIDE_NN")) {
			// Print border
			Print_Header();
			System.out.print("========================================");
			Print_Header();
			System.out.print(" Printing Report: " + Alias);
			Print_Header();
			System.out.print("----------------------------------------");

			// Print attributes
			Print_Header();
			System.out.print(" Next NG ID = " + Next_NG_ID);
		}

		// Go through Neural Groups
		for (int NG = 0; NG < NG_List.size(); NG++) {
			NG_List.get(NG).Print_Report(Tags);
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

	// Neural Groups
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

}
