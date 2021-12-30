package main;

import java.util.ArrayList;
import java.util.HashMap;

public class Note
{
	private String DefaultBeat = "";
	private String DefaultInst = "";
	private int DefaultBPM = 0;
	private ArrayList<Integer> DefaultSharp = null;
	private ArrayList<Integer> DefaultFlat = null;
	private HashMap<String, String> DefineInst = null;
	private ArrayList<String[]> NodeList = null;
	
	public Note(String b, String i, int bpm, ArrayList<Integer> s, ArrayList<Integer> f, HashMap<String, String> di, ArrayList<String[]> n)
	{
		this.DefaultBeat = b;
		this.DefaultInst = i;
		this.DefaultBPM = bpm;
		this.DefaultSharp = s;
		this.DefaultFlat = f;
		this.DefineInst = di;
		this.NodeList = n;
	}
	
	public String getDefaultBeat()
	{
		return this.DefaultBeat;
	}
	
	public String getDefaultInst()
	{
		return this.DefaultInst;
	}
	
	public int getDefaultBPM()
	{
		return this.DefaultBPM;
	}
	
	public ArrayList<Integer> getDefaultSharp()
	{
		return this.DefaultSharp;
	}
	
	public ArrayList<Integer> getDefaultFlat()
	{
		return this.DefaultFlat;
	}
	
	public HashMap<String, String> getDefineInst()
	{
		return this.DefineInst;
	}
	
	public ArrayList<String[]> getNode()
	{
		return this.NodeList;
	}
	
	public void setDefaultBeat(String b)
	{
		this.DefaultBeat = b;
	}
	
	public void setDefaultInst(String i)
	{
		this.DefaultInst = i;
	}
	
	public void setDefaultBPM(int b)
	{
		this.DefaultBPM = b;
	}
	
	public void setDefaultSharp(ArrayList<Integer> s)
	{
		this.DefaultSharp = s;
	}
	
	public void setDefaultFlat(ArrayList<Integer> f)
	{
		this.DefaultFlat = f;
	}
	
	public void setDefineInst(HashMap<String, String> di)
	{
		this.DefineInst = di;
	}
}
