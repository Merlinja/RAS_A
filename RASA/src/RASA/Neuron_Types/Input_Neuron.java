//==========================================================================================
//
//	Neuron : Input Neuron
//
//	Overview: Is a manually activated neuron.
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
package RASA.Neuron_Types;

// Imports
import RASA.Channel;
import RASA.Charge_Node;
import RASA.Neuron;
import RASA.Time_Node;

//------------------------------------------------------------------------------------------
//
//	[END] Includes, Packages, Headers
//
//==========================================================================================
//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
//==========================================================================================
//
//	[START] Neuron: Input_Neuron Class
//
//------------------------------------------------------------------------------------------

public class Input_Neuron extends Neuron {

	boolean Active;
	
	public Input_Neuron() {
		super();
		Active = false;
	}

	public void Activate() {
		Activate(Parent_NN().Default_Input_CH);
	}
	
	public void Activate(Channel CH) {
		Time_Node TN = Parent_NN().First_IC.Get_TN();
		Charge_Node CN = Get_CN(TN, CH);
		CN.Current_Charge = 1.0;
		CN.Current_Weight = 1.0;
		Active = true;
	}
	
	public void Print_Report(String Tags) {

		super.Print_Report(Tags);		
		// Header + full info
		if ((!Tags.contains("SHORT") || Tags.contains("FULL"))
				&& !Tags.contains("HIDE_Neuron")) {
			Print_Header();
			System.out.print(" Type: Sensor");

			// Print attributes
		}

	}
	
	public void Print_Logic(int Indent) {
		Print_Indent(Indent);
		System.out.print("[Input] " + Alias);
	}
	
}

//------------------------------------------------------------------------------------------
//
//	[END] Neuron: Input_Neuron Class
//
//==========================================================================================