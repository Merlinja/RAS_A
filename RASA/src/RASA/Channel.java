package RASA;

import java.util.ArrayList;
import java.util.List;

public class Channel {

	int ID;
	String Alias;
	Neural_Network Parent_NN;
	boolean Active;
	Neuron Seed;
	
	// Source weights
	double LT_Weight; // Long term synapses
	double ST_Weight; // Short term synapses
	double LC_Weight; // Logical components (children & parents)
	double MC_Weight; // Meta-components
	double CH_Weight; // Weight of feeding channels
	
	List<Channel> Feeds_To = new ArrayList<Channel>();
	List<Channel> Feeds_From = new ArrayList<Channel>();
	
	// Comparisons
	List<Channel> Analyzes = new ArrayList<Channel>();
	List<Channel> Analyzed_By = new ArrayList<Channel>();
	
	public Channel() {
		ID = 0;
		Alias = "New Channel";
		Parent_NN = null;
		Active = true;
		Seed = null;
		
		// Initialize weights
		LT_Weight = 0.0;
		ST_Weight = 0.0;
		LC_Weight = 0.0;
		MC_Weight = 0.0;
		CH_Weight = 0.0;
	}
	
	// Reports
	public void Print_Report(String Tags) {

		// Header + full info
		if ((!Tags.contains("SHORT") || Tags.contains("FULL"))
				&& !Tags.contains("HIDE_CH")) {
			// Print border
			System.out.print("\n");
			Print_Header();
			System.out.print("========================================");
			Print_Header();
			System.out.print(" Printing Channel: " + Alias);
			Print_Header();
			System.out.print("----------------------------------------");

			// Print attributes
			Print_Header();
			if (Active)
				System.out.print(" ACTIVE");
			else
				System.out.print(" INACTIVE");
			Print_Header();
			Print_Header();
			System.out.print(" Weights:");
			Print_Header();
			System.out.print("  Long Term: " + LT_Weight);
			Print_Header();
			System.out.print(" Short Term: " + ST_Weight);
			Print_Header();
			System.out.print("      Logic: " + LC_Weight);
			Print_Header();
			System.out.print("       Meta: " + MC_Weight);
			Print_Header();
			System.out.print("    CH Feed: " + CH_Weight);
			
			// Go through feeding
			if (Feeds_From.size() + Feeds_To.size() > 0) {
				Print_Header();
				Print_Header();
				System.out.print(" Feeding:");
			}
			for (int CH = 0; CH < Feeds_From.size(); CH++) {
				Print_Header();
				System.out.print(" <-- \"" + Feeds_From.get(CH).Alias + "\" ");
				Feeds_From.get(CH).Print_Label();
			}
			for (int CH = 0; CH < Feeds_To.size(); CH++) {
				Print_Header();
				System.out.print(" --> \"" + Feeds_To.get(CH).Alias + "\" ");
				Feeds_To.get(CH).Print_Label();
			}
			if (Feeds_From.size() + Feeds_To.size() > 0) Print_Header();
			
			// Go through analyzed channels
			for (int CH = 0; CH < Analyzes.size(); CH++) {
				if (CH == 0) {
					Print_Header();
					System.out.print(" Channels Analyzed:");
				}
				Print_Header();
				System.out.print("   - \"" + Analyzes.get(CH).Alias + "\" ");
				Analyzes.get(CH).Print_Label();
			}
			
			// Go through channels that Analyze this channel
			if (!Tags.contains("SHORT") && Tags.contains("FULL")) {
				for (int CH = 0; CH < Analyzed_By.size(); CH++) {
					if (CH == 0) {
						Print_Header();
						System.out.print(" Analyzed By:");
					}
					Print_Header();
					System.out.print("   - \"" + Analyzed_By.get(CH).Alias + "\" ");
					Analyzed_By.get(CH).Print_Label();
				}
			}
		}


		

	}

	public void Print_Label() {
		System.out.print("[CH:" + ID + "]");
	}

	public void Print_Header() {
		Parent_NN.Print_Header();
		Print_Label();
	}

	public void Analyze_Channel(Channel CH) {
		if (CH == null) return;
		Analyzes.add(CH);
		CH.Analyzed_By.add(this);
	}
	
	public void Feed_From(Channel CH) {
		if (CH == null) return;
		Feeds_From.add(CH);
		CH.Feeds_To.add(this);
	}
	
}
