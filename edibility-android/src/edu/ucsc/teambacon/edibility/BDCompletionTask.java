package edu.ucsc.teambacon.edibility;

// Abstract class for passing a method to the execute function of BackgroundDownloader
// Accepts a downloaded string for processing in subclassed execute methods
public abstract class BDCompletionTask {
	abstract void execute(String s);
}
