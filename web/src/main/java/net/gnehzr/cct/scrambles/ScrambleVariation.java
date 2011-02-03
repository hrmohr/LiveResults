package net.gnehzr.cct.scrambles;

public class ScrambleVariation {

	private ScramblePlugin scramblePlugin;
	
	public ScrambleVariation(ScramblePlugin plugin, String variation) {
		this.scramblePlugin = plugin;
	}
	
	public ScramblePlugin getScramblePlugin() {
		return scramblePlugin;
	}
}
